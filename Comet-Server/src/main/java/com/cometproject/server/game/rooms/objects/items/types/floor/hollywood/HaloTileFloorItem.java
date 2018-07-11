package com.cometproject.server.game.rooms.objects.items.types.floor.hollywood;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;


public class HaloTileFloorItem extends RoomItemFloor {
    public HaloTileFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
        this.setExtraData("0");
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        this.setExtraData("1");
        this.sendUpdate();

        if(this.getDefinition().canSit() == true) {
            double height = (entity instanceof PetEntity || entity.hasAttribute("transformation")) ? this.getSitHeight() / 2 : this.getSitHeight();

            entity.setBodyRotation(this.getRotation());
            entity.setHeadRotation(this.getRotation());
            entity.addStatus(RoomEntityStatus.SIT, String.valueOf(height).replace(',', '.'));
            entity.markNeedsUpdate();
        }
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (this.ticksTimer < 1) {
            this.setTicks(RoomItemFactory.getProcessTime(0.5));
        }

        if(this.getDefinition().canSit() == true) {
            if (entity.hasStatus(RoomEntityStatus.SIT)) {
                entity.removeStatus(RoomEntityStatus.SIT);
            }
            entity.markNeedsUpdate();
        }
    }

    @Override
    public void onTickComplete() {
        this.setExtraData("0");
        this.sendUpdate();
    }

    public double getSitHeight() {
        return this.getDefinition().getHeight();
    }
}
