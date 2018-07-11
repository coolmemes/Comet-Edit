package com.cometproject.server.network.messages.incoming.catalog.recycler;

import com.cometproject.server.game.catalog.recycler.RecycleManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.recycler.RecyclerErrorMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.recycler.RecyclerRewardsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

/**
 * Created by Salinas on 01/02/2018.
 */
public class GetFurniMaticBoxesPageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {

        if(client == null || client.getPlayer() == null) { return; }

        client.send(new RecyclerErrorMessageComposer());
    }
}
