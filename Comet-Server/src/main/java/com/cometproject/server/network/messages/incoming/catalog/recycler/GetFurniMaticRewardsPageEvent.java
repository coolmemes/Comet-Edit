package com.cometproject.server.network.messages.incoming.catalog.recycler;

import com.cometproject.server.game.catalog.recycler.RecycleManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogIndexMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.recycler.RecyclerRewardsMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GetFurniMaticRewardsPageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {

        client.send(new RecyclerRewardsMessageComposer(RecycleManager.getInstance().getRewards(), RecycleManager.getInstance().getLevels()));
    }
}
