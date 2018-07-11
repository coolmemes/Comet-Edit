package com.cometproject.server.game.rooms.objects.items.types.floor.wired.custom.conditions;

import com.cometproject.server.game.rooms.types.Room;

/**
 * Created by Salinas on 05/02/2018.
 */
public class WiredCustomConditionNegativePlayerHasDance extends WiredCustomConditionPlayerHasDance {
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
    public WiredCustomConditionNegativePlayerHasDance(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }
}
