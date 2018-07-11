package com.cometproject.server.network.messages.incoming.marketplace;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.items.rares.LimitedEditionItemData;
import com.cometproject.server.game.marketplace.MarketplaceManager;
import com.google.common.collect.Sets;
import com.cometproject.server.game.marketplace.types.MarketplaceOfferItem;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.*;
import com.cometproject.server.network.messages.outgoing.marketplace.*;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.UpdateActivityPointsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.items.LimitedEditionDao;

/**
 * Created by brend on 31/01/2017.
 */
public class BuyOfferMessageEvent implements Event {
    @Override
    public void handle(final Session client, final MessageEvent msg) throws Exception {
        int offerId = msg.readInt();

        MarketplaceOfferItem offer = MarketplaceManager.getInstance().getOfferById(offerId);

        if (offer == null) {
            client.send(new OffersMessageComposer(MarketplaceManager.getInstance().getOffers(0, 0, "", 1)));
            return;
        }

        if (offer.getState() == 2) {
            client.send(new OffersMessageComposer(MarketplaceManager.getInstance().getOffers(0, 0, "", 1)));
            return;
        }

        if (this.getCorrectTime(offer.getTime()) <= 0) {
            offer.stateUpdate(3);
            client.send(new OffersMessageComposer(MarketplaceManager.getInstance().getOffers(0, 0, "", 1)));
            return;
        }

        if (offer.getPlayerId() == client.getPlayer().getId()) {
            long itemId = ItemDao.createItem(client.getPlayer().getId(), offer.getDefinition().getId(), "");

            PlayerItem playerItem = client.getPlayer().getInventory().add(itemId, offer.getDefinition().getId(), "",
                    null, (offer.getLimitedNumber() > 0 ?
                            new LimitedEditionItemData(itemId, offer.getLimitedStack(), offer.getLimitedNumber()) : null));

            if (offer.getLimitedStack() > 0) {
                LimitedEditionDao.save(new LimitedEditionItemData(itemId, offer.getLimitedStack(), offer.getLimitedNumber()));
            }

            client.send(new UnseenItemsMessageComposer(Sets.newHashSet(playerItem)));
            client.send(new UpdateInventoryMessageComposer());

            MarketplaceManager.getInstance().endOffer(offerId);

            client.send(new CancelOfferMessageComposer(offerId, true));
            client.send(new OffersMessageComposer(MarketplaceManager.getInstance().getOffers(0, 0, "", 1)));
            return;
        }

        int balance = 0;
        String defaultMarketplaceCoin;
        switch (defaultMarketplaceCoin = CometSettings.defaultMarketplaceCoin) {
            case "diamonds": {
                balance = client.getPlayer().getData().getVipPoints();
                break;
            }
            case "credits": {
                balance = client.getPlayer().getData().getCredits();
                break;
            }
            case "duckets": {
                balance = client.getPlayer().getData().getActivityPoints();
                break;
            }
            default:
                break;
        }

        if (balance < offer.getFinalPrice()) {
            client.send(new BuyOfferMessageComposer(3, offerId, offer.getFinalPrice()));
            return;
        }

        long itemId2 = ItemDao.createItem(client.getPlayer().getId(), offer.getDefinition().getId(), "");

        offer.stateUpdate(2);

        PlayerItem playerItem2 = client.getPlayer().getInventory().add(itemId2, offer.getDefinition().getId(),"", null, ((offer.getLimitedNumber() > 0) ? new LimitedEditionItemData(itemId2, offer.getLimitedStack(), offer.getLimitedNumber()) : null));

        if (offer.getLimitedStack() > 0) {
            LimitedEditionDao.save(new LimitedEditionItemData(itemId2, offer.getLimitedStack(), offer.getLimitedNumber()));
        }

        String defaultMarketplaceCoin2;
        switch (defaultMarketplaceCoin2 = CometSettings.defaultMarketplaceCoin) {
            case "diamonds": {
                client.getPlayer().getData().decreasePoints(offer.getFinalPrice());
                client.getPlayer().sendBalance();
                break;
            }
            case "credits": {
                client.getPlayer().getData().decreaseCredits(offer.getFinalPrice());
                client.getPlayer().sendBalance();
                break;
            }
            case "duckets": {
                client.getPlayer().getData().decreaseActivityPoints(offer.getFinalPrice());
                client.send(new UpdateActivityPointsMessageComposer(client.getPlayer().getData().getActivityPoints(), offer.getFinalPrice()));
                break;
            }
            default:
                break;
        }

        client.send(new UnseenItemsMessageComposer(Sets.newHashSet(playerItem2)));
        client.send(new UpdateInventoryMessageComposer());

        if (MarketplaceManager.getInstance().getOffersSize(offer.getDefinition().getSpriteId()) > 1L) {
            MarketplaceManager.getInstance().addPurchasedOffer(offer.getDefinition().getSpriteId());
        }

        client.send(new BuyOfferMessageComposer(1, offerId, offer.getFinalPrice()));
        Session user = NetworkManager.getInstance().getSessions().getByPlayerId(offer.getPlayerId());
        if (user != null) {
            user.send(new CatalogPublishMessageComposer(false));
            user.send(new NotificationMessageComposer("furni_placement_error", Locale.get("marketplace.sell.message")));
        }
    }

    private int getCorrectTime(final int time) {
        return time + 172800 - (int)Comet.getTime();
    }
}