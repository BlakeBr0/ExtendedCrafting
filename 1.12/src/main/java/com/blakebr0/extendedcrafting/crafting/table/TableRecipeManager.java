package com.blakebr0.extendedcrafting.crafting.table;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.cucumber.helper.StackHelper;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class TableRecipeManager {

	private static final TableRecipeManager INSTANCE = new TableRecipeManager();
	private List recipes = new ArrayList();

	public static final TableRecipeManager getInstance() {
		return INSTANCE;
	}

	public TableRecipeShaped addShaped(ItemStack result, Object... recipe) {
		return addShaped(0, result, recipe);
	}

	public TableRecipeShaped addShaped(int tier, ItemStack result, Object... recipe) {
		TableRecipeShaped craft = new TableRecipeShaped(tier, result, recipe);
		this.recipes.add(craft);
		return craft;
	}

	public TableRecipeShapeless addShapeless(ItemStack result, Object... ingredients) {
		return addShapeless(0, result, ingredients);
	}

	public TableRecipeShapeless addShapeless(int tier, ItemStack result, Object... ingredients) {
		TableRecipeShapeless recipe = new TableRecipeShapeless(tier, result, ingredients);
		this.recipes.add(recipe);
		return recipe;
	}

	public ItemStack findMatchingRecipe(InventoryCrafting grid, World world) {
		int i = 0;
		ItemStack stack = ItemStack.EMPTY;
		ItemStack stack1 = ItemStack.EMPTY;
		int j;

		for (j = 0; j < grid.getSizeInventory(); ++j) {
			ItemStack stack2 = grid.getStackInSlot(j);
			if (!StackHelper.isNull(stack2)) {
				if (i == 0) {
					stack = stack2;
				}
				if (i == 1) {
					stack1 = stack2;
				}
				++i;
			}
		}

		if (i == 2 && stack.getItem() == stack1.getItem() && stack.getCount() == 1 && stack1.getCount() == 1
				&& stack.getItem().isRepairable()) {
			Item item = stack.getItem();
			int j1 = item.getMaxDamage() - stack.getItemDamage();
			int k = item.getMaxDamage() - stack1.getItemDamage();
			int l = j1 + k + item.getMaxDamage() * 5 / 100;
			int i1 = item.getMaxDamage() - l;

			if (i1 < 0) {
				i1 = 0;
			}
			return new ItemStack(stack.getItem(), 1, i1);
		} else {
			for (j = 0; j < this.recipes.size(); ++j) {
				IRecipe recipe = (IRecipe) this.recipes.get(j);
				if (recipe.matches(grid, world)) {
					return recipe.getCraftingResult(grid);
				}
			}
			return ItemStack.EMPTY;
		}
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
