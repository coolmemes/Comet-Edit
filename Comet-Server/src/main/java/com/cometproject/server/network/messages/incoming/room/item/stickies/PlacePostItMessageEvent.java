package com.cometproject.server.network.messages.incoming.room.item.stickies;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class PlacePostitMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int itemId = msg.readInt();

        if (client.getPlayer() == null || client.getPlayer().getEntity() == null) {
            return;
        }

        String position = Position.validateWallPosition(msg.readString());

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId())
                && !client.getPlayer().getPermissions().getRank().roomFullControl()) {
            return;
        }

        if (position == null) {
            return;
        }

        PlayerItem item = client.getPlayer().getInventory().getWallItem(itemId);

        if (item == null) {
            return;
        }

        Session roomOwner = NetworkManager.getInstance().getSessions().getByPlayerId(client.getPlayer().getEntity().getRoom().getData().getOwnerId());

        if(roomOwner != null && roomOwner.getPlayer().getId() != client.getPlayer().getId()) {
            roomOwner.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_73, 1);
        }

        client.getPlayer().getEntity().getRoom().getItems().placeWallItem(item, position, client.getPlayer());
        client.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_74, 1);
    }
}
