package com.blakebr0.extendedcrafting.compat.jei.tablecrafting;

import java.util.List;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShaped;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

public class TableShapedWrapper implements IShapedCraftingRecipeWrapper {

	private final TableRecipeShaped recipe;
	private IJeiHelpers helpers;

	public TableShapedWrapper(IJeiHelpers helpers, TableRecipeShaped recipe) {
		this.helpers = helpers;
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		IStackHelper helper = helpers.getStackHelper();
		ItemStack output = recipe.getRecipeOutput();

		List<List<ItemStack>> inputs = helper.expandRecipeItemStackInputs(recipe.getIngredients());

		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, output);
	}

	@Override
	public int getWidth() {
		return recipe.getWidth();
	}

	@Override
	public int getHeight() {
		return recipe.getHeight();
	}
}
