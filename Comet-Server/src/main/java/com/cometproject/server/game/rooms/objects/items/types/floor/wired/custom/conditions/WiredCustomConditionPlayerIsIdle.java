package com.cometproject.server.game.rooms.objects.items.types.floor.wired.custom.conditions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;

public class WiredCustomConditionPlayerIsIdle extends WiredConditionItem {

    public WiredCustomConditionPlayerIsIdle(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public int getInterface() {
        return 10;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (entity == null) return false;


        boolean isIdle = entity.isIdle();

        return isNegative ? !isIdle : isIdle;
    }
}
