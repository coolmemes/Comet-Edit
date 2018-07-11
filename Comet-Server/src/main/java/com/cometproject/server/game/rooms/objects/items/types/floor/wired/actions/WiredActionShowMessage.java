package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.tasks.CometThreadManager;

import java.util.concurrent.TimeUnit;


public class WiredActionShowMessage extends WiredActionItem {

    protected boolean isWhisperBubble = false;

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
    public WiredActionShowMessage(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
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
        this.entity = entity;

        if (!(entity instanceof PlayerEntity)) return false;

        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }

    public void onTickComplete() {

        PlayerEntity playerEntity = ((PlayerEntity) entity);

        String wiredResponse = this.getWiredData().getText().replace("%username%", playerEntity.getPlayer().getSession().getPlayer().getData().getUsername())
                .replace("%roomname%", playerEntity.getRoom().getData().getName())
                .replace("%userscount%", playerEntity.getRoom().getEntities().getPlayerEntities().size() + "")
                .replace("%owner%", playerEntity.getRoom().getData().getOwner());

        playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(playerEntity.getId(), wiredResponse, isWhisperBubble ? 0 : 34));

        return;
    }
}
