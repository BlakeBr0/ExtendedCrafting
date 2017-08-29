package com.blakebr0.extendedcrafting.compat.jei.combinationcrafting;

import java.util.List;

import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class CombinationCraftingWrapper implements IRecipeWrapper {

	private IJeiHelpers helpers;
	private final CombinationRecipe recipe;

	public CombinationCraftingWrapper(IJeiHelpers helpers, CombinationRecipe recipe) {
		this.helpers = helpers;
		this.recipe = recipe;
	}

	public List<ItemStack> getInput() {
		return this.helpers.getStackHelper().toItemStackList(this.recipe.getInput());
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		minecraft.fontRenderer.drawString(recipe.getCost() + " RF (" + recipe.getPerTick() + " RF/t)", 7, 90, 0, false);
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		IStackHelper helper = this.helpers.getStackHelper();
		ItemStack output = this.recipe.getOutput();

		List<List<ItemStack>> inputs = helper.expandRecipeItemStackInputs(this.recipe.getPedestalItems());
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, output);
	}
}