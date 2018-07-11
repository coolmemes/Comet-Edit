package com.cometproject.server.game.rooms.objects.items.types.wall;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;

import java.util.Random;


public class WheelWallItem extends RoomItemWall {
    private boolean isInUse = false;

    private final Random r = new Random();

    public WheelWallItem(long id, int itemId, Room room, int owner, String ownerName, String position, String data) {
        super(id, itemId, room, owner, ownerName, position, data);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (this.isInUse) {
            return false;
        }

        if (entity instanceof PlayerEntity) {
            PlayerEntity pEntity = (PlayerEntity) entity;
            if (!this.getRoom().getRights().hasRights(pEntity.getPlayerId())) {
                return false;
            }
        }

        this.isInUse = true;

        this.setExtraData("-1");
        this.sendUpdate();

        this.setTicks(RoomItemFactory.getProcessTime(4));
        return true;
    }

    @Override
    public void onTickComplete() {
        int wheelPos = r.nextInt(10) + 1;

        this.setExtraData(Integer.toString(wheelPos));
        this.sendUpdate();

        this.isInUse = false;
    }

    @Override
    public void onPickup() {
        this.cancelTicks();
    }

    @Override
    public void onPlaced() {
        if (!"0".equals(this.getExtraData())) {
            this.setExtraData("0");
        }
    }
}
