package com.blakebr0.extendedcrafting.compat.jei.compressor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipe;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

public class CompressorCraftingWrapper implements IRecipeWrapper {

	private IJeiHelpers helpers;
	protected final CompressorRecipe recipe;

	public CompressorCraftingWrapper(IJeiHelpers helpers, CompressorRecipe recipe) {
		this.helpers = helpers;
		this.recipe = recipe;
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		if (mouseX > 1 && mouseX < 14 && mouseY > 1 && mouseY < 78) {
			return Arrays.<String> asList(Utils.format(this.recipe.getPowerCost()) + " RF");
		}
		if (mouseX > 54 && mouseX < 78 && mouseY > 58 && mouseY < 68) {
			return Arrays.<String> asList(Utils.format(this.recipe.getInputCount()));
		}
		return Collections.emptyList();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		IStackHelper helper = this.helpers.getStackHelper();
		ItemStack output = this.recipe.getOutput();
		ItemStack input = this.recipe.getInput();
		ItemStack catalyst = this.recipe.getCatalyst();

		ingredients.setInput(ItemStack.class, helper.expandRecipeItemStackInputs(Arrays.asList(input, catalyst)));
		ingredients.setOutput(ItemStack.class, output);
	}
}