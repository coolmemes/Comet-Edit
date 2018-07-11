package com.cometproject.server.game.rooms;

import com.cometproject.api.game.rooms.settings.RoomAccessType;
import com.cometproject.api.game.rooms.settings.RoomTradeState;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Configuration;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.filter.WordFilter;
import com.cometproject.server.game.rooms.models.CustomFloorMapData;
import com.cometproject.server.game.rooms.models.types.StaticRoomModel;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.game.rooms.types.RoomReloadListener;
import com.cometproject.server.game.rooms.types.misc.ChatEmotionsManager;
import com.cometproject.server.network.messages.outgoing.room.events.RoomPromotionMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.cache.CacheManager;
import com.cometproject.server.storage.cache.objects.RoomDataObject;
import com.cometproject.server.storage.queries.rooms.RoomDao;
import com.cometproject.server.utilities.Initialisable;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;
import org.apache.solr.util.ConcurrentLRUCache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;


public class RoomManager implements Initialisable {
    private static RoomManager roomManagerInstance;
    public static final Logger log = Logger.getLogger(RoomManager.class.getName());

    public static final int LRU_MAX_ENTRIES = Integer.parseInt(Configuration.currentConfig().getProperty("comet.game.rooms.data.max"));
    public static final int LRU_MAX_LOWER_WATERMARK = Integer.parseInt(Configuration.currentConfig().getProperty("comet.game.rooms.data.lowerWatermark"));

    private ConcurrentLRUCache<Integer, RoomData> roomDataInstances;

    private Map<Integer, Room> loadedRoomInstances;
    private Map<Integer, Room> unloadingRoomInstances;

    private Map<Integer, RoomPromotion> roomPromotions;

    private Map<String, StaticRoomModel> models;
    private WordFilter filterManager;

    private RoomCycle globalCycle;
    private ChatEmotionsManager emotions;

    private ExecutorService executorService;

    private Map<Integer, RoomReloadListener> reloadListeners;

    public RoomManager() {

    }

    @Override
    public void initialize() {
        this.roomDataInstances = new ConcurrentLRUCache<>(LRU_MAX_ENTRIES, LRU_MAX_LOWER_WATERMARK);

        this.loadedRoomInstances = new ConcurrentHashMap<>();
        this.unloadingRoomInstances = new ConcurrentHashMap<>();
        this.roomPromotions = new ConcurrentHashMap<>();
        this.reloadListeners = new ConcurrentHashMap<>();

        this.emotions = new ChatEmotionsManager();
        this.filterManager = new WordFilter();

        this.globalCycle = new RoomCycle();

        this.loadPromotedRooms();
        this.loadModels();

        this.globalCycle.start();

        this.executorService = Executors.newFixedThreadPool(4, r -> {
            final Thread roomThread = new Thread(r, "Room-Worker-" + UUID.randomUUID());

            roomThread.setUncaughtExceptionHandler((t, e) -> e.printStackTrace());

            return roomThread;
        });


        log.info("RoomManager initialized");
    }

    public static RoomManager getInstance() {
        if (roomManagerInstance == null)
            roomManagerInstance = new RoomManager();

        return roomManagerInstance;
    }

    public void loadPromotedRooms() {
        RoomDao.deleteExpiredRoomPromotions();
        RoomDao.getActivePromotions(this.roomPromotions);

        log.info("Loaded " + this.getRoomPromotions().size() + " room promotions");
    }

    public void initializeRoom(Session initializer, int roomId, String password) {
        this.executorService.submit(() -> {
            if (initializer != null && initializer.getPlayer() != null) {
                initializer.getPlayer().loadRoom(roomId, password);
            }
        });
    }

    public void loadModels() {
        if (this.models != null && this.getModels().size() != 0) {
            this.getModels().clear();
        }

        this.models = RoomDao.getModels();

        log.info("Loaded " + this.getModels().size() + " room models");
    }

    public StaticRoomModel getModel(String id) {
        if (this.models.containsKey(id))
            return this.models.get(id);

        log.debug("Couldn't find model: " + id);

        return null;
    }

    public Room get(int id) {
        if (id == 0) return null;

        if (this.getRoomInstances().containsKey(id)) {
            return this.getRoomInstances().get(id);
        }

        if (this.getRoomInstances().containsKey(id)) {
            return this.getRoomInstances().get(id);
        }

        Room room = null;

        if (CacheManager.getInstance().isEnabled() && CacheManager.getInstance().exists("rooms." + id)) {
            final RoomDataObject roomDataObject = CacheManager.getInstance().get(RoomDataObject.class, "rooms." + id);

            if (roomDataObject != null) {
                room = new Room(roomDataObject);
            }
        }

        if (room == null) {
            RoomData data = this.getRoomData(id);

            if (data == null) {
                return null;
            }

            room = new Room(data);
        }

        room.load();

        this.loadedRoomInstances.put(id, room);

        this.finalizeRoomLoad(room);

        return room;
    }

    private void finalizeRoomLoad(Room room) {
        if (room == null) {
            return;
        }
        room.getItems().onLoaded();
    }

    public RoomData getRoomData(int id) {
        if (this.getRoomDataInstances().getMap().containsKey(id)) {
            return this.getRoomDataInstances().get(id).setLastReferenced(Comet.getTime());
        }

        RoomData roomData = RoomDao.getRoomDataById(id);

        if (roomData != null) {
            this.getRoomDataInstances().put(id, roomData);
        }

        return roomData;
    }

    public void unloadIdleRooms() {
        for (Room room : this.unloadingRoomInstances.values()) {
            this.executorService.submit(() -> {
                room.dispose();

                if(room.isReloading()) {
                    Room newRoom = this.get(room.getId());

                    if(newRoom != null) {
                        if(this.reloadListeners.containsKey(room.getId())) {
                            final RoomReloadListener reloadListener = this.reloadListeners.get(newRoom.getId());

                            reloadListener.onReloaded(newRoom);
                            this.reloadListeners.remove(room.getId());
                        }
                    }
                }
            });
        }

        this.unloadingRoomInstances.clear();

        List<Room> idleRooms = new ArrayList<>();

        for (Room room : this.loadedRoomInstances.values()) {
            if (room.isIdle()) {
                idleRooms.add(room);
            }
        }

        for (Room room : idleRooms) {
            this.loadedRoomInstances.remove(room.getId());
            this.unloadingRoomInstances.put(room.getId(), room);
        }
    }

    public void forceUnload(int id) {
        if (this.loadedRoomInstances.containsKey(id)) {
            this.loadedRoomInstances.remove(id).dispose();
        }
    }

    public void removeData(int roomId) {
        if (this.getRoomDataInstances().get(roomId) == null) {
            return;
        }

        this.getRoomDataInstances().remove(roomId);
    }

    public void addReloadListener(int roomId, RoomReloadListener listener) {
        this.reloadListeners.put(roomId, listener);
    }

    public void loadRoomsForUser(Player player) {
        player.getRooms().clear();

        Map<Integer, RoomData> rooms = RoomDao.getRoomsByPlayerId(player.getId());

        for (Map.Entry<Integer, RoomData> roomEntry : rooms.entrySet()) {
            player.getRooms().add(roomEntry.getKey());

            if (!this.getRoomDataInstances().getMap().containsKey(roomEntry.getKey())) {
                this.getRoomDataInstances().put(roomEntry.getKey(), roomEntry.getValue());
            }
        }
    }

    public List<RoomData> getRoomsByQuery(String query) {
        ArrayList<RoomData> rooms = new ArrayList<>();

        if (query.equals("tag:")) return rooms;

        if (query.startsWith("roomname:")) {
            query = query.substring(9);
        }

        List<RoomData> roomSearchResults = RoomDao.getRoomsByQuery(query);

        for (RoomData data : roomSearchResults) {
            if (!this.getRoomDataInstances().getMap().containsKey(data.getId())) {
                this.getRoomDataInstances().put(data.getId(), data);
            }

            rooms.add(data);
        }

        if (rooms.size() == 0 && !query.toLowerCase().startsWith("owner:")) {
            return this.getRoomsByQuery("owner:" + query);
        }

        return rooms;
    }

    public boolean isActive(int id) {
        return this.getRoomInstances().containsKey(id);
    }

    public int createRoom(String name, String description, CustomFloorMapData model, int category, int maxVisitors, int tradeState, Session client, int wallTickness, int floorThickness, String decorations, boolean hideWalls) {
        int roomId = RoomDao.createRoom(name, model, description, category, maxVisitors, RoomTradeState.valueOf(tradeState), client.getPlayer().getId(), client.getPlayer().getData().getUsername(), wallTickness, floorThickness, decorations, hideWalls);

        this.loadRoomsForUser(client.getPlayer());

        return roomId;
    }

    public int createRoom(String name, String description, String model, int category, int maxVisitors, int tradeState, Session client) {
        int roomId = RoomDao.createRoom(name, model, description, category, maxVisitors, RoomTradeState.valueOf(tradeState), client.getPlayer().getId(), client.getPlayer().getData().getUsername());

        this.loadRoomsForUser(client.getPlayer());

        return roomId;
    }

    private List<Integer> getActiveAvailableRooms() {
        final List<Integer> rooms = new ArrayList<>();

        for(Room activeRoom : this.loadedRoomInstances.values()) {
            if(!this.unloadingRoomInstances.containsKey(activeRoom.getId())) {
                final int playerCount = activeRoom.getEntities().playerCount();

                if(playerCount != 0 && playerCount < activeRoom.getData().getMaxUsers() &&
                        activeRoom.getData().getAccess() == RoomAccessType.OPEN) {
                    rooms.add(activeRoom.getId());
                }
            }
        }

        return rooms;
    }

    public int getRandomActiveRoom() {
        final List<Integer> rooms = this.getActiveAvailableRooms();
        final Integer roomId = WiredUtil.getRandomElement(rooms);

        rooms.clear();

        if(roomId != null) {
            return roomId;
        }

        return -1;
    }

    public List<RoomData> getRoomsByCategory(int category) {
        List<RoomData> rooms = new ArrayList<>();

        for (Room room : this.getRoomInstances().values()) {
            if (category != -1 && (room.getData().getCategory() == null || room.getData().getCategory().getId() != category)) {
                continue;
            }

            rooms.add(room.getData());
        }

        return rooms;
    }

    public void promoteRoom(int roomId, String name, String description) {
        if (this.roomPromotions.containsKey(roomId)) {
            RoomPromotion promo = this.roomPromotions.get(roomId);
            promo.setTimestampFinish(promo.getTimestampFinish() + (RoomPromotion.DEFAULT_PROMO_LENGTH * 60));

            RoomDao.updatePromotedRoom(promo);
        } else {
            RoomPromotion roomPromotion = new RoomPromotion(roomId, name, description);
            RoomDao.createPromotedRoom(roomPromotion);

            this.roomPromotions.put(roomId, roomPromotion);
        }

        final Room room = this.get(roomId);

        if (room != null) {
            if (room.getEntities() != null && room.getEntities().realPlayerCount() >= 1) {
                room.getEntities().broadcastMessage(new RoomPromotionMessageComposer(room.getData(), this.roomPromotions.get(roomId)));
            }
        }
    }

    public boolean hasPromotion(int roomId) {
        return this.roomPromotions.containsKey(roomId) && !this.roomPromotions.get(roomId).isExpired();
    }

    public final ChatEmotionsManager getEmotions() {
        return this.emotions;
    }

    public final Map<Integer, Room> getRoomInstances() {
        return this.loadedRoomInstances;
    }

    private ConcurrentLRUCache<Integer, RoomData> getRoomDataInstances() {
        return this.roomDataInstances;
    }

    public final Map<String, StaticRoomModel> getModels() {
        return this.models;
    }

    public final RoomCycle getGlobalCycle() {
        return this.globalCycle;
    }

    public final WordFilter getFilter() {
        return filterManager;
    }

    public Map<Integer, RoomPromotion> getRoomPromotions() {
        return roomPromotions;
    }
}
