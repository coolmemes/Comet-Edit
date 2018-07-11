package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerAtGivenTimeLong extends WiredTriggerAtGivenTime {
    private static final int PARAM_TICK_LENGTH = 0;

    private boolean needsReset = false;

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
    public WiredTriggerAtGivenTimeLong(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if (this.getWiredData().getParams().get(PARAM_TICK_LENGTH) == null) {
            this.getWiredData().getParams().put(PARAM_TICK_LENGTH, 2);
        }
    }

    @Override
    public int getTime() {
        return this.getWiredData().getParams().get(PARAM_TICK_LENGTH) * 10;
    }

    @Override
    public int getInterface() {
        return 3;
    }

    public static boolean executeTriggers(Room room, int timer) {
        boolean wasExecuted = false;

        for (RoomItemFloor wiredItem : room.getItems().getByClass(WiredTriggerAtGivenTimeLong.class)) {
            WiredTriggerAtGivenTimeLong trigger = ((WiredTriggerAtGivenTimeLong) wiredItem);

            if (timer >= trigger.getTime() && !trigger.needsReset) {
                if (trigger.evaluate(null, null)) {
                    wasExecuted = true;

                    trigger.needsReset = true;
                }
            }
        }

        return wasExecuted;
    }

    public void setNeedsReset(boolean needsReset) {
        this.needsReset = needsReset;
    }

    public boolean needsReset() {
        return this.needsReset;
    }
}
