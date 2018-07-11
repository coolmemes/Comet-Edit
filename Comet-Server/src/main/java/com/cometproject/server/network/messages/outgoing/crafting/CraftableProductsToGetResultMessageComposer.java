package com.cometproject.server.network.messages.outgoing.crafting;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.crafting.CraftingMachine;
import com.cometproject.server.game.items.crafting.CraftingRecipe;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.storage.queries.items.ItemDao;

public class CraftableProductsToGetResultMessageComposer extends MessageComposer {
    private final CraftingRecipe recipe;

    public CraftableProductsToGetResultMessageComposer(CraftingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public short getId() {
        return Composers.CraftableProductsToGetResultMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.recipe.getComponents().size());

        for(int itemBase : this.recipe.getComponents()) {
            ItemDefinition item = ItemManager.getInstance().getByBaseId(itemBase);
            msg.writeInt(1);
            msg.writeString(item.getItemName());
        }
    }
}