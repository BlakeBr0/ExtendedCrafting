package com.blakebr0.extendedcrafting.crafting;

import java.util.ArrayList;

import com.blakebr0.extendedcrafting.config.ModConfig;

import net.minecraft.item.ItemStack;

public class CombinationRecipeManager {

	private static final CombinationRecipeManager INSTANCE = new CombinationRecipeManager();

	private ArrayList<CombinationRecipe> recipes = new ArrayList<CombinationRecipe>();

	public static final CombinationRecipeManager getInstance() {
		return INSTANCE;
	}
	
	public void addRecipe(ItemStack output, long cost, ItemStack input, Object... pedestals) {
		if (ModConfig.confCraftingCoreEnabled) {
			this.recipes.add(new CombinationRecipe(output, cost, input, pedestals));
		}
	}

	public void addRecipe(ItemStack output, long cost, int perTick, ItemStack input, Object... pedestals) {
		if (ModConfig.confCraftingCoreEnabled) {
			this.recipes.add(new CombinationRecipe(output, cost, perTick, input, pedestals));
		}
	}

	public ArrayList<CombinationRecipe> getRecipes() {
		return this.recipes;
	}
	
	public void removeRecipes(ItemStack stack) {
		this.recipes.removeIf(o -> o.getOutput().isItemEqual(stack));
	}
}
