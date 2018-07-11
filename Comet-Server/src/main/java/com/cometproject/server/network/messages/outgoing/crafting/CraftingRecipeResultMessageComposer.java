package com.cometproject.server.network.messages.outgoing.crafting;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.items.crafting.CraftingMachine;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class CraftingRecipeResultMessageComposer extends MessageComposer {
    private int recipes;
    private boolean found;

    public CraftingRecipeResultMessageComposer(Integer recipes, boolean found) {
        this.recipes = recipes;
        this.found = found;
    }

    @Override
    public short getId() {
        return Composers.CraftingRecipeResultMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(recipes);
        msg.writeBoolean(found);
    }
}
