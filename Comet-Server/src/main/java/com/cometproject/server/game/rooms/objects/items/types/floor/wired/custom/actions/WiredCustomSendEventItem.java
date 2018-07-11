package com.cometproject.server.game.rooms.objects.items.types.floor.wired.custom.actions;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NuxAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;

public class WiredCustomSendEventItem extends WiredActionItem {
    private int ownerRank;

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
    public WiredCustomSendEventItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);


        final PlayerData playerData = PlayerManager.getInstance().getDataByPlayerId(this.ownerId);

        if (playerData != null) {
            this.ownerRank = playerData.getRank();
        } else {
            this.ownerRank = 1;
        }
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

        if (CometSettings.roomWiredRewardMinimumRank > this.ownerRank) return false;

        String[] mode = this.getWiredData().getText().split("_");
        switch (mode[0]){
            case "event":
                playerEntity.getPlayer().getSession().send(new NuxAlertMessageComposer("CUSTOM", mode[1]));
                break;
            case "alert":
                playerEntity.getPlayer().getSession().send(new SimpleAlertMessageComposer(mode[1]));
                break;
            case "bubble":
                playerEntity.getPlayer().getSession().send(new NotificationMessageComposer(mode[1], mode[2], mode[3]));
                break;
        }
        return true;
    }
}