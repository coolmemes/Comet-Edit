package com.cometproject.server.network.messages.outgoing.crafting;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.crafting.CraftingMachine;
import com.cometproject.server.game.items.crafting.CraftingRecipe;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Map;

public class CraftableProductsMessageComposer extends MessageComposer {
    private final CraftingMachine machine;

    public CraftableProductsMessageComposer(CraftingMachine machine) {
        this.machine = machine;
    }

    @Override
    public short getId() {
        return Composers.CraftableProductsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.machine.getPublicRecipes().size());
        for(CraftingRecipe recipe : this.machine.getPublicRecipes()) {
            msg.writeString(recipe.getResultProductData());
            msg.writeString(recipe.getResultProductData());
        }

        msg.writeInt(this.machine.getAllowedItems().size());
        for(String productdata : this.machine.getAllowedItems().values()) {
            msg.writeString(productdata);
        }
    }
}
