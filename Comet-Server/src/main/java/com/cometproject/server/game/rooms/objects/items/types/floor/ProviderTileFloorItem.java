package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

import java.util.*;


public class ProviderTileFloorItem extends RoomItemFloor {
    public ProviderTileFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    public void updateColor(int state) {
        this.setExtraData("state" + state + "effectId" + this.getEffectId());
        this.sendUpdate();
    }

    @Override
    public void onPlaced(){
        this.setExtraData("state0effectId0");
        this.saveData();
    }

    @Override
    public void onEntityPreStepOn(RoomEntity entity) {
        this.updateColor(1);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if(entity.getEntityType().equals(RoomEntityType.PET) || entity == null) { return; }

        int effectId = this.getDefinition().getEffectId() != 0 ? this.getDefinition().getEffectId() : this.getEffectId();

        entity.applyEffect(new PlayerEffect(effectId, 0));
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        this.updateColor(0);
    }

    private List<String> getExtradataInfo() {
        List<String> values = new ArrayList<String>();

        for(String value : this.getExtraData().replace("state", "").split("effectId")) {
            values.add(value.trim());
        }

        return values;
    }

    public int getState() {
        return new Integer(this.getExtradataInfo().get(0)) == 0 ? 1 : 0;
    }

    public int getEffectId() {
        return this.getDefinition().getEffectId() != 0 ? this.getDefinition().getEffectId() : new Integer(this.getExtradataInfo().get(1));
    }
}