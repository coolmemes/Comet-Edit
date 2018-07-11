package com.cometproject.server.network.messages.incoming.marketplace;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.items.rares.LimitedEditionItemData;
import com.cometproject.server.game.marketplace.MarketplaceManager;
import com.cometproject.server.game.marketplace.types.MarketplaceOfferItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.marketplace.*;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.items.LimitedEditionDao;
import com.cometproject.server.storage.queries.marketplace.MarketplaceDao;
import com.google.common.collect.Sets;

public class CancelOfferMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int offerId = msg.readInt();

        MarketplaceOfferItem offer = MarketplaceManager.getInstance().getOfferById(offerId);
        if (offer == null || offer.getPlayerId() != client.getPlayer().getId() || MarketplaceDao.getItemIdByOffer(offerId) <= 0) {
            client.send(new CancelOfferMessageComposer(offerId, false));
            return;
        }

        long itemId = ItemDao.createItem(client.getPlayer().getId(), offer.getDefinition().getId(), "");
        PlayerItem playerItem = client.getPlayer().getInventory().add(itemId, offer.getDefinition().getId(), "", null, (offer.getLimitedNumber() > 0 ? new LimitedEditionItemData(itemId, offer.getLimitedStack(), offer.getLimitedNumber()) : null));
        if (offer.getLimitedStack() > 0) {
            LimitedEditionDao.save(new LimitedEditionItemData(itemId, offer.getLimitedStack(), offer.getLimitedNumber()));
        }

        client.send(new UnseenItemsMessageComposer(Sets.newHashSet(playerItem)));
        client.send(new UpdateInventoryMessageComposer());

        MarketplaceManager.getInstance().endOffer(offerId);

        client.send(new CancelOfferMessageComposer(offerId, true));
    }
}