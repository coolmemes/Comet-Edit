package com.cometproject.server.game.items;

import com.cometproject.server.game.items.crackable.CrackableItem;
import com.cometproject.server.game.items.crafting.CraftingMachine;
import com.cometproject.server.game.items.music.MusicData;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.storage.queries.items.*;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ItemManager implements Initializable {
    private static ItemManager itemManagerInstance;

    private Logger log = Logger.getLogger(ItemManager.class.getName());

    private Map<Integer, ItemDefinition> itemDefinitions;

    private Map<Integer, Integer> itemSpriteIdToDefinitionId;
    private Map<Integer, MusicData> musicData;

    private Map<Integer, CrackableItem> crackableRewards;
    private Map<Long, Integer> itemIdToVirtualId;
    private Map<Integer, Long> virtualIdToItemId;
    private List<CraftingMachine> craftingMachines;

    private AtomicInteger itemIdCounter;
    private Integer saddleId;

    public ItemManager() {

    }

    @Override
    public void initialize() {
        this.itemDefinitions = new HashMap<>();
        this.musicData = new HashMap<>();
        this.crackableRewards = new HashMap<>();
        this.craftingMachines = new ArrayList<CraftingMachine>();

        this.itemIdToVirtualId = new ConcurrentHashMap<>();
        this.virtualIdToItemId = new ConcurrentHashMap<>();

        this.itemIdCounter = new AtomicInteger(1);

        this.loadItemDefinitions();
        this.loadMusicData();
        this.loadCrackableRewards();
        this.loadCraftingMachines();

        log.info("ItemManager initialized");
    }

    public static ItemManager getInstance() {
        if (itemManagerInstance == null) {
            itemManagerInstance = new ItemManager();
        }

        return itemManagerInstance;
    }

    public void loadItemDefinitions() {
        Map<Integer, ItemDefinition> tempMap = this.itemDefinitions;
        Map<Integer, Integer> tempSpriteIdItemMap = this.itemSpriteIdToDefinitionId;

        try {
            this.itemDefinitions = ItemDao.getDefinitions();
            this.itemSpriteIdToDefinitionId = new HashMap<>();
        } catch (Exception e) {
            log.error("Error while loading item definitions", e);
        }

        if (tempMap.size() >= 1) {
            tempMap.clear();
            tempSpriteIdItemMap.clear();
        }

        if (this.itemDefinitions != null) {
            for (ItemDefinition itemDefinition : this.itemDefinitions.values()) {
                if(itemDefinition.getItemName().equals("horse_saddle1")) {
                    this.saddleId = itemDefinition.getId();
                }

                this.itemSpriteIdToDefinitionId.put(itemDefinition.getSpriteId(), itemDefinition.getId());
            }
        }

        log.info("Loaded " + this.getItemDefinitions().size() + " item definitions");
    }

    public void loadMusicData() {
        if (!this.musicData.isEmpty()) {
            this.musicData.clear();
        }

        MusicDao.getMusicData(this.musicData);
        log.info("Loaded " + this.musicData.size() + " songs");
    }

    public int getItemVirtualId(long itemId) {
        if(this.itemIdToVirtualId.containsKey(itemId)) {
            return this.itemIdToVirtualId.get(itemId);
        }

        int virtualId = this.itemIdCounter.getAndIncrement();

        this.itemIdToVirtualId.put(itemId, virtualId);
        this.virtualIdToItemId.put(virtualId, itemId);

        return virtualId;
    }

    public void loadCrackableRewards() {
        if(!this.crackableRewards.isEmpty()) {
            this.crackableRewards.clear();
        }

        for(CrackableItem crackable : CrackableDao.getCrackableRewards()){
            this.crackableRewards.put(crackable.getItemId(), crackable);
        }

        log.info("Loaded " + this.crackableRewards.size() + " crackable rewards");
    }

    public void loadCraftingMachines() {
        if(!this.craftingMachines.isEmpty()) {
            this.craftingMachines.clear();
        }

        for(ItemDefinition item : this.itemDefinitions.values()) {
            if(item.getInteraction().equals("crafting")) {
                CraftingMachine machine = new CraftingMachine(item.getId());
                CraftingDao.loadAllowedItems(machine);
                CraftingDao.loadRecipes(machine);
                this.craftingMachines.add(machine);
                machine = null;
            }
        }
    }


    public void disposeItemVirtualId(long itemId) {
        int virtualId = this.getItemVirtualId(itemId);

        this.itemIdToVirtualId.remove(itemId);
        this.virtualIdToItemId.remove(virtualId);
    }

    public Long getItemIdByVirtualId(int virtualId) {
        return this.virtualIdToItemId.get(virtualId);
    }

    public long getTeleportPartner(long itemId) {
        return TeleporterDao.getPairId(itemId);
    }

    public int roomIdByItemId(long itemId) {
        return RoomItemDao.getRoomIdById(itemId);
    }

    public ItemDefinition getDefinition(int itemId) {
        if (this.getItemDefinitions().containsKey(itemId)) {
            return this.getItemDefinitions().get(itemId);
        }

        return null;
    }

    public MusicData getMusicData(int songId) {
        if (this.musicData.containsKey(songId)) {
            return this.musicData.get(songId);
        }

        return null;
    }

    public MusicData getMusicDataByName(String name) {
        for (MusicData musicData : this.musicData.values()) {
            if (musicData.getName().equals(name)) {
                return musicData;
            }
        }

        return null;
    }

    public CrackableItem getCrackableRewards(int crackableId) {
        CrackableItem crI = null;

        if(this.crackableRewards.containsKey(crackableId)) {
            crI = this.crackableRewards.get(crackableId);
        }

        return crI;
    }


    public CraftingMachine getCraftingMachine(int itemId) {
        final CraftingMachine[] machine = {null};
        for(CraftingMachine machineX : this.craftingMachines){
            if(machineX.getBaseId() == itemId) { machine[0] = machineX; }
        }

        return machine[0];
    }

    public Map<Long, Integer> getItemIdToVirtualIds() {
        return itemIdToVirtualId;
    }

    public ItemDefinition getBySpriteId(int spriteId) {
        return this.itemDefinitions.get(this.itemSpriteIdToDefinitionId.get(spriteId));
    }

    public ItemDefinition getByBaseId(int baseId) {
        return this.itemDefinitions.get(baseId);
    }

    public Logger getLogger() {
        return log;
    }

    public Map<Integer, ItemDefinition> getItemDefinitions() {
        return this.itemDefinitions;
    }

    public Integer getSaddleId() {
        return saddleId;
    }
}
