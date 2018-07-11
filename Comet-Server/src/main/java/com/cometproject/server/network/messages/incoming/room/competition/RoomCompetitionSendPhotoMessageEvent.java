package com.cometproject.server.network.messages.incoming.room.competition;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class RoomCompetitionSendPhotoMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        client.send(new AdvancedAlertMessageComposer("testerino pavo"));
    }
}