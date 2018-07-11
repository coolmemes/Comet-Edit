package com.cometproject.server.network.messages.incoming.crafting;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.crafting.CraftingMachine;
import com.cometproject.server.game.items.crafting.CraftingRecipe;
import com.cometproject.server.game.items.rares.LimitedEditionItemData;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.crafting.CraftingFinalResultMessageComposer;
import com.cometproject.server.network.messages.outgoing.crafting.CraftingRecipeResultMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.items.CraftingDao;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.items.LimitedEditionDao;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;
import com.cometproject.server.storage.queries.rooms.RoomDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;

public class CraftSecretMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        CraftingMachine machine = client.getPlayer().getLastCraftingMachine();
        if(machine == null) { return; }

        final int MachineInRoomId = msg.readInt();
        final int totalItems = msg.readInt();
        final CraftingRecipe[] recipe = {null};
        List<Long> realId = new ArrayList<Long>();
        List<Integer> items = new ArrayList<Integer>();
        List<PlayerItem> inventoryItems = new ArrayList<PlayerItem>();

        for(int i=0;i<totalItems;i++) {
            final int itemId = msg.readInt();
            realId.add(ItemManager.getInstance().getItemIdByVirtualId(itemId));
            PlayerItem nI = client.getPlayer().getInventory().getFloorItem(ItemManager.getInstance().getItemIdByVirtualId(itemId));
            items.add(nI.getDefinition().getId());
            inventoryItems.add(nI);
        }

        for(CraftingRecipe recipeX : machine.getSecretRecipes()){
            if(recipeX.getComponents().equals(items) && recipe[0] == null) {
                recipe[0] = recipeX;
            }
        }

        if(recipe[0] == null) { return; }

        CraftingRecipe finalRecipe = recipe[0];

        LimitedEditionItemData ltdItem = null;

        for(PlayerItem item : inventoryItems){
            client.getPlayer().getInventory().removeItem(item);
            realId.forEach(RoomItemDao::deleteItem);
        }

        int nIb = finalRecipe.getResultBaseId();
        long nId = ItemDao.createItem(client.getPlayer().getData().getId(), nIb, "");

        if(finalRecipe.getResultLimitedSells() > 0) {
            if(finalRecipe.getResultTotalCrafted() == finalRecipe.getResultLimitedSells()) { return; }

            ltdItem = new LimitedEditionItemData(nId, finalRecipe.getResultTotalCrafted(), finalRecipe.getResultLimitedSells());
            LimitedEditionDao.save(ltdItem);
            finalRecipe.increateTotalCrafted();
            CraftingDao.updateLimitedRecipe(finalRecipe);
        }

        if(finalRecipe.getAchievement() != null) { client.getPlayer().getAchievements().progressAchievement(finalRecipe.getAchievement(), 1); }
        if(finalRecipe.getBadge() != null) {  client.getPlayer().getInventory().addBadge(finalRecipe.getBadge(), true); }

        client.send(new CraftingFinalResultMessageComposer(true, recipe[0]));
        client.send(new UnseenItemsMessageComposer(Sets.newHashSet(client.getPlayer().getInventory().add(nId, nIb, "", null, null))));
        client.send(new UpdateInventoryMessageComposer());

    }
}


