package com.blakebr0.extendedcrafting.crafting.endercrafter;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.table.ITieredRecipe;
import com.blakebr0.extendedcrafting.crafting.table.TableCrafting;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShaped;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShapeless;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class EnderCrafterRecipeManager {

	private static final EnderCrafterRecipeManager INSTANCE = new EnderCrafterRecipeManager();
	private List recipes = new ArrayList();

	public static final EnderCrafterRecipeManager getInstance() {
		return INSTANCE;
	}

	public TableRecipeShaped addShaped(ItemStack result, int time, Object... recipe) {
		TableRecipeShaped craft = new TableRecipeShaped(1, result, recipe);
		
		if (ModConfig.confEnderEnabled) {
			craft.enderCrafterRecipeTimeRequired = time;
			this.recipes.add(craft);
		}
		
		return craft;
	}
	
	public TableRecipeShapeless addShapeless(ItemStack result, int time, Object... ingredients) {
		TableRecipeShapeless recipe = new TableRecipeShapeless(1, result, ingredients);
		
		if (ModConfig.confEnderEnabled) {
			recipe.enderCrafterRecipeTimeRequired = time;
			this.recipes.add(recipe);
		}
		
		return recipe;
	}
	
	public IEnderCraftingRecipe findMatchingRecipe(TableCrafting grid, World world) {
		for (int i = 0; i < this.recipes.size(); i++) {
			IRecipe recipe = (IRecipe) this.recipes.get(i);
			if (recipe.matches(grid, world)) {
				return (IEnderCraftingRecipe) recipe;
			}
		}
		
		return null;
	}

	public List getRecipes() {
		return this.recipes;
	}
	
	public void removeRecipes(ItemStack stack) {
		this.recipes.removeIf(o -> o instanceof IRecipe && ((IRecipe) o).getRecipeOutput().isItemEqual(stack));
	}

	public List getRecipes(int size) {
		List recipes = new ArrayList<>();
		for (Object o : getRecipes()) {
			IRecipe recipe = (IRecipe) o;
			if (recipe.canFit(size, size)) {
				recipes.add(recipe);
			}
		}
		
		return recipes;
	}

	/**
	 * Gets all the recipes for the specified tier Basic is tier 1, Advanced
	 * tier 2, etc
	 * 
	 * @param tier the tier of the recipe
	 * @return a list of recipes for this tier
	 */
	public List getRecipesTiered(int tier) {
		List recipes = new ArrayList<>();
		for (Object o : getRecipes()) {
			if (o instanceof ITieredRecipe) {
				ITieredRecipe recipe = (ITieredRecipe) o;
				if (recipe.getTier() == tier) {
					recipes.add(recipe);
				}
			}
		}
		
		return recipes;
	}
}
