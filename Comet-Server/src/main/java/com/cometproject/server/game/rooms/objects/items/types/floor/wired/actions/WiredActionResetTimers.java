package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerAtGivenTime;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerAtGivenTimeLong;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerPeriodically;
import com.cometproject.server.game.rooms.types.Room;

import java.util.List;


public class WiredActionResetTimers extends WiredActionItem {
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
    public WiredActionResetTimers(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 1;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }

    public void onTickComplete() {
        final List<RoomItemFloor> items = this.getRoom().getItems().getByClass(WiredTriggerAtGivenTime.class);
        items.addAll(this.getRoom().getItems().getByClass(WiredTriggerAtGivenTimeLong.class));

        for (RoomItemFloor floorItem : items) {
            if (floorItem instanceof WiredTriggerAtGivenTime) {
                ((WiredTriggerAtGivenTime) floorItem).setNeedsReset(false);
            } else if(floorItem instanceof WiredTriggerAtGivenTimeLong) {
                ((WiredTriggerAtGivenTimeLong) floorItem).setNeedsReset(false);
            } else if(floorItem instanceof WiredTriggerPeriodically) {
                ((WiredTriggerPeriodically) floorItem).disableTicks();
            }
        }

        this.getRoom().resetWiredTimer();
    }
}
