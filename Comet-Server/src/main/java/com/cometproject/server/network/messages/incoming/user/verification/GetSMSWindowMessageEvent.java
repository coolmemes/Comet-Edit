package com.cometproject.server.network.messages.incoming.user.verification;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.verification.SMSVerificationOfferMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class GetSMSWindowMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (client == null || client.getPlayer() == null || client.getPlayer().getData() == null) {
            return;
        }
        client.send(new SMSVerificationOfferMessageComposer());
    }
}
