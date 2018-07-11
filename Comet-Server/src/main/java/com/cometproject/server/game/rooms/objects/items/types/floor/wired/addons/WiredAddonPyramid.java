package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.RandomInteger;


public class WiredAddonPyramid extends RoomItemFloor {
    private boolean hasEntity = false;

    public WiredAddonPyramid(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);

        this.setTicks(RandomInteger.getRandom(5, 8) * 2);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        this.hasEntity = true;
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        this.hasEntity = false;
    }

    @Override
    public void onTickComplete() {
        if (this.hasEntity) {
            this.setTicks(RoomItemFactory.getProcessTime(1.0));
            return;
        }

        if (this.getExtraData().equals("1")) {
            this.setExtraData("0");
        } else {
            this.setExtraData("1");
        }

        this.sendUpdate();
        this.setTicks(RoomItemFactory.getProcessTime(RandomInteger.getRandom(5, 8)));

        this.getRoom().getMapping().updateTile(this.getPosition().getX(), this.getPosition().getY());
    }
}
