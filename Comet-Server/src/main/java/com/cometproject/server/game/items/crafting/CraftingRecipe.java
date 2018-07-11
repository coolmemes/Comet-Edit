package com.cometproject.server.game.items.crafting;

import com.cometproject.server.game.achievements.types.AchievementType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingRecipe {

    public enum RecipeMode {
        PUBLIC,
        PRIVATE
    }

    private int id;
    private RecipeMode mode;
    private List<Integer> components = new ArrayList<Integer>();
    private String resultProductData;
    private int resultBaseId;
    private int resultLimitedSells;
    private int resultTotalCrafted;
    private String badge;
    private AchievementType achievement;

    public CraftingRecipe(int recipeId, String components, String result, int resultLimitedSells, int resultTotalCrafted, String badge, AchievementType achievement) {
        this.id = recipeId;

        for(String component : components.split(",")) {
            String[] c = component.split(":");
            for(int i=0;i<Integer.valueOf(c[1]);i++) {
                this.components.add(Integer.valueOf(c[0]));
            }
        }

        String[] r = result.split(":");
        this.resultProductData = r[0];
        this.resultBaseId = Integer.valueOf(r[1]);
        this.resultLimitedSells = resultLimitedSells;
        this.resultTotalCrafted = resultTotalCrafted;
        this.badge = badge;
        this.achievement = achievement;
    }

    public int getId() { return this.id; }

    public RecipeMode getMode() { return this.mode; }

    public List<Integer> getComponents() { return this.components; }

    public String getResultProductData() { return this.resultProductData; }

    public int getResultBaseId() { return this.resultBaseId; }

    public int getResultLimitedSells() { return this.resultLimitedSells; }

    public int getResultTotalCrafted() { return this.resultTotalCrafted; }

    public void increateTotalCrafted() { this.resultTotalCrafted++; }

    public String getBadge() { return this.badge; }

    public AchievementType getAchievement() { return this.achievement; }
}
