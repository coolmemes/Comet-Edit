package com.cometproject.server.network.messages.incoming.crafting;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.crafting.CraftingMachine;
import com.cometproject.server.game.items.crafting.CraftingRecipe;
import com.cometproject.server.game.items.rares.LimitedEditionItemData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.crafting.CraftableProductsToGetResultMessageComposer;
import com.cometproject.server.network.messages.outgoing.crafting.CraftingFinalResultMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.items.LimitedEditionDao;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;

public class ExecuteCraftingRecipeMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        CraftingMachine machine = client.getPlayer().getLastCraftingMachine();
        if(machine == null) { return; }

        final int machineId = msg.readInt();
        final String result = msg.readString();
        CraftingRecipe recipe = machine.getRecipeByProductData(result);

        if(recipe == null) { return; }

        for(Integer elementId : recipe.getComponents()) {
            for(PlayerItem item : client.getPlayer().getInventory().getFloorItems().values()) {
                if(item.getBaseId() == elementId) {
                    client.getPlayer().getInventory().removeFloorItem(item.getId());
                    RoomItemDao.deleteItem(item.getId());
                    break;
                }
            }

            for(PlayerItem item : client.getPlayer().getInventory().getWallItems().values()) {
                if(item.getBaseId() == elementId) {
                    client.getPlayer().getInventory().removeWallItem(item.getId());
                    RoomItemDao.deleteItem(item.getId());
                    break;
                }
            }
        }

        int nIb = recipe.getResultBaseId();
        long nId = ItemDao.createItem(client.getPlayer().getData().getId(), nIb, "");

        if(recipe.getAchievement() != null) { client.getPlayer().getAchievements().progressAchievement(recipe.getAchievement(), 1); }
        if(recipe.getBadge() != null) { client.getPlayer().getInventory().addBadge(recipe.getBadge(), true); }

        client.send(new CraftingFinalResultMessageComposer(true, recipe));
        client.send(new UnseenItemsMessageComposer(Sets.newHashSet(client.getPlayer().getInventory().add(nId, nIb, "", null, null))));
        client.send(new UpdateInventoryMessageComposer());
    }
}
