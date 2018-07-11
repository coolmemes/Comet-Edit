package com.cometproject.server.game.catalog.recycler;

/**
 * Created by Salinas on 01/02/2018.
 */
public class FurniMaticLevel{
    private int levelId;
    private int chance;

    public FurniMaticLevel(int levelId, int chance) {
        this.levelId = levelId;
        this.chance = chance;
    }

    public int getChance() { return this.chance; }

    public int getLevelId() { return this.levelId; }
}
