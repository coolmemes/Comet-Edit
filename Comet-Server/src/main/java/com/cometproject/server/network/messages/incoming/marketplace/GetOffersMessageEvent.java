package com.cometproject.server.network.messages.incoming.marketplace;

import com.cometproject.server.game.marketplace.MarketplaceManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.marketplace.OffersMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class GetOffersMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int minPrice = msg.readInt();
        int maxPrice = msg.readInt();

        String query = msg.readString();

        int sort = msg.readInt();

        client.send(new OffersMessageComposer(MarketplaceManager.getInstance().getOffers(minPrice, maxPrice, query, sort)));
    }
}