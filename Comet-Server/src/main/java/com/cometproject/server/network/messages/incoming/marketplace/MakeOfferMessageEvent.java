package com.cometproject.server.network.messages.incoming.marketplace;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.marketplace.MarketplaceManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.marketplace.*;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.RemoveObjectFromInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.marketplace.MarketplaceDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

public class MakeOfferMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int price = msg.readInt();
        msg.readInt();
        int itemId = msg.readInt();

        PlayerItem playerItem = client.getPlayer().getInventory().getFloorItem(itemId);
        if (playerItem == null || price >= 70000000 || price <= 0) {
            client.send(new MakeOfferMessageComposer(0));
            return;
        }

        if (playerItem.getLimitedEditionItem() == null && !CometSettings.defaultMarketplaceType.equals("all")) {
            client.send(new AdvancedAlertMessageComposer("Oops! Nossa feira livre est\u00e1 limitada \u00e0 raros."));
            return;
        }

        int totalPrice = this.getComission(price);
        int type = this.getItemType(playerItem.getDefinition());
        if (playerItem.getLimitedEditionItem() != null) {
            type = 3;
        }

        MarketplaceManager.getInstance().offerItem(MarketplaceDao.createOffer(client.getPlayer().getId(), playerItem, price, totalPrice, type));
        client.getPlayer().getInventory().removeItem(playerItem);

        RoomItemDao.deleteItem(playerItem.getId());

        client.send(new RemoveObjectFromInventoryMessageComposer(itemId));
        client.send(new MakeOfferMessageComposer(1));
    }

    private int getComission(int price) {
        return price + (int)Math.ceil((double)price / 100.0);
    }

    private int getItemType(FurnitureDefinition definition) {
        int type = 0;
        String string = definition.getType();
        switch (string.hashCode()) {
            case 105: {
                if (string.equals("i")) break;
                return type;
            }
            case 115: {
                if (string.equals("s")) return 1;
                return type;
            }
        }

        type = 2;
        return type;
    }
}
