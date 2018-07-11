package com.cometproject.server.game.rooms.objects.items.types.floor.wired.custom.actions;

import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;

public class WiredCustomGiveAchievement extends WiredActionItem {
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
    public WiredCustomGiveAchievement(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 7;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (!(entity instanceof PlayerEntity)) return false;

        PlayerEntity playerEntity = ((PlayerEntity) entity);
        String text = this.getWiredData().getText();

        switch (text){
            case "ACH_20":
                playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_20, 1);
                break;
            case "ACH_66":
                playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_66, 1);
                break;
            case "ACH_67":
                playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_67, 1);
                break;
            case "ACH_68":
                playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_68, 1);
                break;
            case "ACH_247":
                playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_101, 1);
                break;
        }

        return true;
    }
}