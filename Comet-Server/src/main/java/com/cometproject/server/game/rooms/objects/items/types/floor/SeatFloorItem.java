package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;


public class SeatFloorItem extends RoomItemFloor {

    public SeatFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity instanceof PlayerEntity)) {
                return false;
            }

            PlayerEntity pEntity = (PlayerEntity) entity;

            if (!pEntity.getRoom().getRights().hasRights(pEntity.getPlayerId())
                    && !pEntity.getPlayer().getPermissions().getRank().roomFullControl()) {
                return false;
            }
        }

        this.toggleInteract(true);
        this.sendUpdate();

        if (this instanceof AdjustableHeightSeatFloorItem) {
            for (RoomEntity sitter : this.getEntitiesOnItem()) {
                this.onEntityStepOn(sitter, true);
            }
        }

        this.saveData();
        return true;
    }

    public void onEntityStepOn(RoomEntity entity, boolean instantUpdate) {
        double height = (entity instanceof PetEntity || entity.hasAttribute("transformation")) ? this.getSitHeight() / 2 : this.getSitHeight();

        entity.setBodyRotation(this.getRotation());
        entity.setHeadRotation(this.getRotation());
        entity.addStatus(RoomEntityStatus.SIT, String.valueOf(height).replace(',', '.'));

        if(this.getDefinition().getInteraction().equals("toggle_state")){
            this.setExtraData("1");
            this.sendUpdate();
        }


        if (instantUpdate)
            this.getRoom().getEntities().broadcastMessage(new AvatarUpdateMessageComposer(entity));
        else
            entity.markNeedsUpdate();
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        this.onEntityStepOn(entity, false);
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (entity.hasStatus(RoomEntityStatus.SIT)) {
            entity.removeStatus(RoomEntityStatus.SIT);
        }

        if(this.getDefinition().getInteraction().equals("toggle_state")){
            this.setExtraData("0");
            this.sendUpdate();
        }

        entity.markNeedsUpdate();
    }

    public double getSitHeight() {
        return this.getDefinition().getHeight();
    }
}
