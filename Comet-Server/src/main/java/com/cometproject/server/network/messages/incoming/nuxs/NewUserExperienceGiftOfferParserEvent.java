package com.cometproject.server.network.messages.incoming.nuxs;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.nuxs.NuxGift;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.google.common.collect.Sets;

public class NewUserExperienceGiftOfferParserEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int Int1 = msg.readInt();
        final int Int2 = msg.readInt();
        final int Int3 = msg.readInt();
        final int giftId = msg.readInt();

        NuxGift selectedGift = CatalogManager.getInstance().getNuxGifts().get(giftId);

        switch(selectedGift.getType()) {
            case ITEM:
                int baseId = Integer.valueOf(selectedGift.getRandomData());
                long itemId = ItemDao.createItem(client.getPlayer().getId(), baseId, "");
                final PlayerItem playerItem = new InventoryItem(itemId, baseId, "");
                client.getPlayer().getInventory().addItem(playerItem);
                client.send(new UpdateInventoryMessageComposer());
                client.send(new UnseenItemsMessageComposer(Sets.newHashSet(playerItem)));
                break;
            case DIAMONDS:
                int diamonds = Integer.valueOf(selectedGift.getRandomData());
                client.getPlayer().getData().increasePoints(diamonds);
                client.getPlayer().sendBalance();
                break;
            case SEASONAL:
                int seasonal = Integer.valueOf(selectedGift.getRandomData());
                client.getPlayer().getData().increaseSeasonalPoints(seasonal);
                client.getPlayer().sendBalance();
                break;
            case BADGE:
                client.getPlayer().getInventory().addBadge(selectedGift.getRandomData(), true);
                break;
        }
    }
}
