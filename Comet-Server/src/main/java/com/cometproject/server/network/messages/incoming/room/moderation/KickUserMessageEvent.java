package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.api.game.rooms.settings.RoomKickState;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.alerts.RoomConnectionErrorMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class KickUserMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int playerId = msg.readInt();

        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();
        PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(playerId);

        if (playerEntity == null) {
            return;
        }

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId())
                && !client.getPlayer().getPermissions().getRank().roomFullControl() && room.getData().getKickState() != RoomKickState.EVERYONE) {
            return;
        }


        if (playerEntity.getPlayer().getData().getRank() > client.getPlayer().getData().getRank() || room.getData().getOwnerId() == playerEntity.getPlayerId() || !playerEntity.getPlayer().getPermissions().getRank().roomKickable()) {
            return;
        }

        playerEntity.kick();
        playerEntity.getPlayer().getSession().send(new RoomConnectionErrorMessageComposer(2, ""));
    }
}
