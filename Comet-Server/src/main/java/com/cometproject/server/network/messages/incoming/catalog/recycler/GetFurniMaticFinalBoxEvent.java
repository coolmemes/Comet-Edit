package com.cometproject.server.network.messages.incoming.catalog.recycler;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.recycler.RecyclerSuccessMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salinas on 01/02/2018.
 */
public class GetFurniMaticFinalBoxEvent implements Event {
    public void handle(Session client, MessageEvent msg) {

        if(client == null || client.getPlayer() == null) { return; }

        List<Long> realId = new ArrayList<Long>();
        List<Integer> items = new ArrayList<Integer>();
        List<PlayerItem> inventoryItems = new ArrayList<PlayerItem>();

        int itemsCount = msg.readInt(); // should be 8 all time

        for(int i = 0; i < itemsCount; i++){

            int itemId = msg.readInt();

            realId.add(ItemManager.getInstance().getItemIdByVirtualId(itemId));
            PlayerItem nI = client.getPlayer().getInventory().getFloorItem(ItemManager.getInstance().getItemIdByVirtualId(itemId));
            items.add(nI.getDefinition().getId());
            inventoryItems.add(nI);

        }

        inventoryItems.forEach((item) -> {
            client.getPlayer().getInventory().removeItem(item);
            realId.forEach(RoomItemDao::deleteItem);
        });

        int furniMaticBoxId = CometSettings.furniMaticBoxItemId;
        String boxTime = DateTime.now().getDayOfMonth() + "-" + DateTime.now().getMonthOfYear() + "-" + DateTime.now().getYear();
        long boxId = ItemDao.createItem(client.getPlayer().getData().getId(), furniMaticBoxId, "");

        client.send(new UnseenItemsMessageComposer(Sets.newHashSet(client.getPlayer().getInventory().add(boxId, furniMaticBoxId, boxTime, null, null))));
        client.send(new UpdateInventoryMessageComposer());
        client.send(new RecyclerSuccessMessageComposer());
    }
}
