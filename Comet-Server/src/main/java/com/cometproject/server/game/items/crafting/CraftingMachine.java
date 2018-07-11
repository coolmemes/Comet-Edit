package com.cometproject.server.game.items.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingMachine {
    private int baseId;
    private Map<Integer, String> allowedItems;
    private List<CraftingRecipe> publicRecipes;
    private List<CraftingRecipe> secretRecipes;

    public CraftingMachine(int baseId) {
        this.baseId = baseId;
        this.allowedItems = new HashMap<Integer, String>();
        this.publicRecipes = new ArrayList<CraftingRecipe>();
        this.secretRecipes = new ArrayList<CraftingRecipe>();
    }

    public int getBaseId() { return this.baseId; }

    public Map<Integer, String> getAllowedItems() { return this.allowedItems; }

    public List<CraftingRecipe> getPublicRecipes() { return this.publicRecipes; }

    public List<CraftingRecipe> getSecretRecipes() { return this.secretRecipes; }

    public CraftingRecipe getRecipeByProductData(String productdata) {
        CraftingRecipe result = null;

        for(CraftingRecipe recipe : this.publicRecipes) {
            if(recipe.getResultProductData().equals(productdata))
                result = recipe;
        }

        return result;
    }
}
