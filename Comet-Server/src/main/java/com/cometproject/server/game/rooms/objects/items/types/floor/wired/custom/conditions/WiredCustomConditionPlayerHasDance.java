package com.cometproject.server.game.rooms.objects.items.types.floor.wired.custom.conditions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;

/**
 * Created by Salinas on 05/02/2018.
 */
public class WiredCustomConditionPlayerHasDance extends WiredConditionItem {
    public static int PARAM_DANCE_ID = 0;

    public WiredCustomConditionPlayerHasDance(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public int getInterface() {
        return 12;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (entity == null) return false;

        if (this.getWiredData().getParams().size() != 1) {
            return false;
        }

        final int danceId = this.getWiredData().getParams().get(PARAM_DANCE_ID);
        boolean isDancing = false;

        if (entity.getDanceId() == danceId) {
            isDancing = true;
        }


        return isNegative ? !isDancing : isDancing;
    }
}
