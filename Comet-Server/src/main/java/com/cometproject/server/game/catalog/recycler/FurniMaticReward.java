package com.cometproject.server.game.catalog.recycler;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.server.game.items.ItemManager;

public class FurniMaticReward {
    private int displayId;
    private int baseId;
    private int level;

    public FurniMaticReward(int displayId, int baseId, int level) {
        this.displayId = displayId;
        this.baseId = baseId;
        this.level = level;
    }

    public FurnitureDefinition getDefinition() {
        return ItemManager.getInstance().getDefinition(this.baseId);
    }

    public int getDisplayId() { return this.displayId; }

    public int getBaseId() { return this.baseId; }

    public int getLevel() { return this.level; }
}
