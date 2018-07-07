package com.blakebr0.extendedcrafting.crafting.endercrafter;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.table.ITieredRecipe;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShaped;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShapeless;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

public class EnderCrafterRecipeManager {

	private static final EnderCrafterRecipeManager INSTANCE = new EnderCrafterRecipeManager();
	private static final InventoryCrafting DUMMY_INVENTORY = new InventoryCrafting(null, 0, 0);
	private List recipes = new ArrayList();

	public static final EnderCrafterRecipeManager getInstance() {
		return INSTANCE;
	}

	public TableRecipeShaped addShaped(ItemStack result, Object... recipe) {
		TableRecipeShaped craft = new TableRecipeShaped(1, result, recipe);
		
		if (ModConfig.confEnderEnabled) {
			this.recipes.add(craft);
		}
		
		return craft;
	}
	
	public TableRecipeShapeless addShapeless(ItemStack result, Object... ingredients) {
		TableRecipeShapeless recipe = new TableRecipeShapeless(1, result, ingredients);
		
		if (ModConfig.confEnderEnabled) {
			this.recipes.add(recipe);
		}
		
		return recipe;
	}
	
	public ItemStack findMatchingRecipe(IItemHandlerModifiable grid) {
		int i = 0;
		int j;
		for (j = 0; j < this.recipes.size(); ++j) {
			IRecipe recipe = (IRecipe) this.recipes.get(j);
			if (recipe instanceof ITieredRecipe) {
				if (((ITieredRecipe) recipe).matches(grid)) {
					// Pass through a dummy crafting inventory because it doesn't actually need one
					return recipe.getCraftingResult(DUMMY_INVENTORY);
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public List getRecipes() {
		return this.recipes;
	}
	
	public void removeRecipe(ItemStack stack) {
		for (Object obj : getRecipes()) {
			if (obj instanceof IRecipe) {
				IRecipe recipe = (IRecipe)obj;
				if (recipe.getRecipeOutput().isItemEqual(stack)) {
					this.recipes.remove(obj);
					break;
				}
			}
		}
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
