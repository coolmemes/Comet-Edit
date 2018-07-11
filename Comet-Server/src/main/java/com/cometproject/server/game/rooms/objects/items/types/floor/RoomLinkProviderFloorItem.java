package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.access.GetGuestRoomResultMessageComposer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salinas on 06/02/2018.
 */
public class RoomLinkProviderFloorItem extends DefaultFloorItem {

    public RoomLinkProviderFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onPlaced(){
        this.setExtraData("state0internalLink0");
        this.saveData();
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if(!(entity instanceof PlayerEntity)) return;

        PlayerEntity playerEntity = ((PlayerEntity) entity);
        playerEntity.getPlayer().getSession().send(new GetGuestRoomResultMessageComposer(new Integer(this.getExtradataInfo().get(1))));

        return;
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if(!(entity instanceof PlayerEntity))
            return false;

        PlayerEntity playerEntity = ((PlayerEntity) entity);
        System.out.println("holaaaaaa");
        playerEntity.getPlayer().getSession().send(new GetGuestRoomResultMessageComposer(new Integer(this.getExtradataInfo().get(1))));

        return false;
    }

    private List<String> getExtradataInfo() {
        List<String> values = new ArrayList<String>();

        for(String value : this.getExtraData().replace("state", "").split("internalLink")) {
            values.add(value.trim());
        }

        return values;
    }
}
