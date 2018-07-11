package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.ProviderTileFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class SaveBrandingMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int brandingId = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || (!room.getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().getRank().roomFullControl())) {
            return;
        }

        int length = msg.readInt();
        String data = "state" + (char) 9 + "0";

        for (int i = 1; i <= length; i++) {
            data = data + (char) 9 + msg.readString();
        }

        RoomItemFloor item = room.getItems().getFloorItem(brandingId);
        item.setExtraData(data);
        item.sendUpdate();
        item.saveData();

        if(item instanceof ProviderTileFloorItem) {
            int effectId = new Integer(data.split("effectId")[1].trim());
            int danceId = new Integer(data.split("danceId")[1].trim());
            int handitemId = new Integer(data.split("handitemId")[1].trim());

            if(effectId == 102 || effectId == 178 || effectId == 187) { return; }

            for(RoomEntity entity : item.getEntitiesOnItem()) {
                item.onEntityPreStepOn(entity);
                item.onEntityStepOn(entity);
            }
        }
    }
}
