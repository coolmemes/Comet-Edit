package com.cometproject.server.game.rooms.objects.items.types.wall;

import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;


public final class DecorationWallItem extends RoomItemWall {
    public DecorationWallItem(long id, int itemId, Room room, int owner, String position, String data) {
        super(id, itemId, room, owner, position, data);
    }
}
