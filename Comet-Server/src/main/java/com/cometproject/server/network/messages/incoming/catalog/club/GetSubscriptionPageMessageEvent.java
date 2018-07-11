package com.cometproject.server.network.messages.incoming.catalog.club;

import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogPageMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GetSubscriptionPageMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int pageId = msg.readInt();

        if (CatalogManager.getInstance().pageExists(pageId) && CatalogManager.getInstance().getPage(pageId).isEnabled()) {
            client.send(new CatalogPageMessageComposer(CatalogManager.getInstance().getPage(pageId), client.getPlayer()));
        }
    }
}
