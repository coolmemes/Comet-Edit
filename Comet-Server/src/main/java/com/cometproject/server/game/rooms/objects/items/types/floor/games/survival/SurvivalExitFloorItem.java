package com.cometproject.server.game.rooms.objects.items.types.floor.games.survival;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class SurvivalExitFloorItem extends RoomItemFloor {
    public SurvivalExitFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.setExtraData("0");
    }
}
