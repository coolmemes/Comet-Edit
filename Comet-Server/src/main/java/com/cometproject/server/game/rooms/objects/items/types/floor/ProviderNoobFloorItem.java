package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

import java.util.ArrayList;
import java.util.List;

public class ProviderNoobFloorItem extends RoomItemFloor {
    public ProviderNoobFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityPreStepOn(RoomEntity entity) {
        if(!entity.getEntityType().equals(RoomEntityType.PLAYER)) { return; }
        this.setExtraData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if(entity.getEntityType().equals(RoomEntityType.PET) || entity == null) { return; }

        entity.applyEffect(new PlayerEffect(this.getDefinition().getEffectId(), 0));
        this.setTicks(RoomItemFactory.getProcessTime(0.5));
    }

    @Override
    public void onTickComplete() {
        this.setExtraData("0");
        this.sendUpdate();
    }
}