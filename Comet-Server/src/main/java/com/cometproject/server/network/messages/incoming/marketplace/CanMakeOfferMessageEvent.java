package com.cometproject.server.network.messages.incoming.marketplace;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.marketplace.CanMakeOfferMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class CanMakeOfferMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new CanMakeOfferMessageComposer(1));
    }
}