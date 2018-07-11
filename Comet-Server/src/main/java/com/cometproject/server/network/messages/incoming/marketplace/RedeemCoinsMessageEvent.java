package com.cometproject.server.network.messages.incoming.marketplace;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.marketplace.MarketplaceManager;
import com.cometproject.server.game.marketplace.types.MarketplaceOfferItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.marketplace.*;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.RemoveObjectFromInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.UpdateActivityPointsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.marketplace.MarketplaceDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

import java.util.List;

public class RedeemCoinsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        List<MarketplaceOfferItem> myOffers = MarketplaceManager.getInstance().getOwnSoldOffers(client.getPlayer().getId());
        int owedCoins = this.getOwedCoins(myOffers);

        if (owedCoins >= 1) {
            final String defaultMarketplaceCoin;
            switch (defaultMarketplaceCoin = CometSettings.defaultMarketplaceCoin) {
                case "diamonds": {
                    client.getPlayer().getData().increasePoints(owedCoins);
                    client.getPlayer().sendBalance();
                    break;
                }

                case "credits": {
                    client.getPlayer().getData().increaseCredits(owedCoins);
                    client.getPlayer().sendBalance();
                    break;
                }

                case "duckets": {
                    client.getPlayer().getData().increaseActivityPoints(owedCoins);
                    client.send(new UpdateActivityPointsMessageComposer(client.getPlayer().getData().getActivityPoints(), owedCoins));
                    break;
                }

                default:
                    break;
            }
        }

        myOffers.forEach(offer -> MarketplaceManager.getInstance().endOffer(offer.getOfferId()));
    }

    private int getOwedCoins(List<MarketplaceOfferItem> offers) {
        return MarketplaceManager.getInstance().getSoldPriceForPlayer(offers);
    }
}