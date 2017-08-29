package com.blakebr0.extendedcrafting.crafting;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class CombinationRecipeManager {
	
	private static final CombinationRecipeManager INSTANCE = new CombinationRecipeManager();
	
    private static ArrayList<CombinationRecipe> recipes = new ArrayList<CombinationRecipe>();

    public static final CombinationRecipeManager getInstance(){
    	return INSTANCE;
    }
    
    public static void addRecipe(ItemStack output, int cost, int perTick, ItemStack input, Object... pedestals){
        recipes.add(new CombinationRecipe(output, cost, perTick, input, pedestals));
    }
    
    public static ArrayList<CombinationRecipe> getRecipes(){
        return recipes;
    }
}
