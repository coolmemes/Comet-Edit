package com.cometproject.server.network.messages.incoming.catalog.club;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.google.common.collect.Sets;

public class GetClubPresentMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {

        if(client.getPlayer().getSubscription().getPresents() <= 0)
            return;

        String item = msg.readString();

        int id = ItemDao.getItemByName(item);
        long nId = ItemDao.createItem(client.getPlayer().getData().getId(), id, "");

        client.getPlayer().getSubscription().decrementPresents(client.getPlayer().getData().getId());
        client.send(new UnseenItemsMessageComposer(Sets.newHashSet(client.getPlayer().getInventory().add(nId, id, "", null, null))));
        client.send(new UpdateInventoryMessageComposer());
    }
}
