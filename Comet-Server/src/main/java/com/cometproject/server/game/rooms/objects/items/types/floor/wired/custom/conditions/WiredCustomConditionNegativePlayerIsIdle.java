package com.cometproject.server.game.rooms.objects.items.types.floor.wired.custom.conditions;

import com.cometproject.server.game.rooms.types.Room;

public class WiredCustomConditionNegativePlayerIsIdle extends WiredCustomConditionPlayerIsIdle {

    public WiredCustomConditionNegativePlayerIsIdle(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

    }
}
