package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.data.ScoreboardItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore.HighscoreClassicFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;

import java.util.ArrayList;
import java.util.List;


public class WiredActionGiveScore extends WiredActionItem {
    private final static int PARAM_SCORE = 0;
    private final static int PARAM_PER_GAME = 1;
    private PlayerEntity playerEntity;

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionGiveScore(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if (this.getWiredData().getParams().size() < 2) {
            this.getWiredData().getParams().clear();

            this.getWiredData().getParams().put(PARAM_SCORE, 1);
            this.getWiredData().getParams().put(PARAM_PER_GAME, 1);
        }
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 6;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (!(entity instanceof PlayerEntity)) { return false; }

        PlayerEntity playerEntity = ((PlayerEntity) entity);

        if (playerEntity.getGameTeam() == null) { return false; }

        this.playerEntity = playerEntity;

        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }

    public void onTickComplete() {
        final List<RoomItemFloor> scoreboards = this.getRoom().getItems().getByClass(HighscoreClassicFloorItem.class);

        if (scoreboards.size() != 0) {
            for (RoomItemFloor scoreboard : scoreboards) {
                HighscoreClassicFloorItem item = ((HighscoreClassicFloorItem) scoreboard);

                boolean found = false;

                for(ScoreboardItemData.HighscoreEntry entry : item.getScoreData().getEntries()) {
                    if(entry.getUsers().get(0).equals(this.playerEntity.getUsername())) {
                        entry.setScore(entry.getScore() + this.getFinalScore());
                        found = true;
                        break;
                    }
                }

                if(!found) { item.addEntry(new ArrayList<String>(){{add(playerEntity.getUsername());}}, this.getFinalScore()); }

                item.sendUpdate();
                item.saveData();
            }
        }
    }

    public int getScore() {
        return this.getWiredData().getParams().get(PARAM_SCORE);
    }

    public int getScorePerGame() {
        return this.getWiredData().getParams().get(PARAM_PER_GAME);
    }

    public int getFinalScore() {
        return this.getScore() * this.getScorePerGame();
    }
}
