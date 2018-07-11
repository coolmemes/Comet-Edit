package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.handshake.HomeRoomMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class ChangeHomeRoomMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        int roomId = room.getId();

        client.send(new HomeRoomMessageComposer(client.getPlayer().getSettings().getHomeRoom(), roomId));
        client.getPlayer().getSettings().setHomeRoom(roomId);

        PlayerDao.updateHomeRoom(roomId, client.getPlayer().getId());
    }
}
