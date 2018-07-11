package com.cometproject.server.network.messages.incoming.user.club;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.club.ClubStatusMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.club.SubscriptionCenterInfoMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GetSubscriptionCenterMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        client.send(new SubscriptionCenterInfoMessageComposer(client.getPlayer().getSubscription()));
        client.send(new ClubStatusMessageComposer(client.getPlayer().getSubscription()));
    }
}
