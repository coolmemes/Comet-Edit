package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class UseWallItemMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) {
        int itemId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        RoomItemWall item = client.getPlayer().getEntity().getRoom().getItems().getWallItem(itemId);

        if (item == null) {
            return;
        }

        int requestData = msg.readInt();

        if(!client.getPlayer().getEntity().isVisible()) {
            return;
        }

        if(item.getDefinition().getItemName().contains("habbowheel")) {
            client.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_97, 1);
        }

        item.onInteract(client.getPlayer().getEntity(), requestData, false);
    }
}
