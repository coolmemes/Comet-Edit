package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class GenericTimerFloorItem extends RoomItemFloor {

    public GenericTimerFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTriggered) {
        if (!isWiredTriggered) {
            if (!(entity instanceof PlayerEntity)) {
                return false;
            }

            PlayerEntity pEntity = (PlayerEntity) entity;
            if (!pEntity.getRoom().getRights().hasRights(pEntity.getPlayerId()) && !pEntity.getPlayer().getPermissions().getRank().roomFullControl()) {
                return false;
            }
        }

        if (this.getExtraData().isEmpty() || Integer.parseInt(this.getExtraData()) < 0) {
            this.setExtraData("0");
        }

        int time = Integer.getInteger(this.getExtraData());
        time++;

        if (isWiredTriggered) {
          trigger();
        }

        this.setExtraData(String.valueOf(time));
        this.sendUpdate();
        this.saveData();

        return true;
    }

    @Override
    public void onPickup() {

    }

    public void trigger() {
        int time = Integer.getInteger(this.getExtraData());

        this.setExtraData(String.valueOf(time));
        this.sendUpdate();
        this.saveData();
    }
}