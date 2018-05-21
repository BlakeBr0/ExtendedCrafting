package com.blakebr0.extendedcrafting.compat.jei.tablecrafting;

import java.util.Collections;
import java.util.List;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.compat.jei.CompatJEI;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShapeless;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class TableShapelessWrapper implements IRecipeWrapper {

	private final TableRecipeShapeless recipe;
	private final IJeiHelpers helpers;
	private final IDrawable required;
	private int reqX = 0, reqY = 0;
	private boolean tiered = false;
	
	public TableShapelessWrapper(IJeiHelpers helpers, TableRecipeShapeless recipe) {
		this.helpers = helpers;
		this.recipe = recipe;
		this.required = helpers.getGuiHelper().createDrawable(CompatJEI.ICONS, 0, 0, 3, 15);
	}

	public TableShapelessWrapper(IJeiHelpers helpers, TableRecipeShapeless recipe, int reqX, int reqY) {
		this.helpers = helpers;
		this.recipe = recipe;
		this.required = helpers.getGuiHelper().createDrawable(CompatJEI.ICONS, 0, 0, 3, 15);
		this.reqX = reqX;
		this.reqY = reqY;
		this.tiered = true;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		IStackHelper helper = this.helpers.getStackHelper();
		ItemStack output = this.recipe.getRecipeOutput();

		List<List<ItemStack>> inputs = helper.expandRecipeItemStackInputs(this.recipe.getIngredients());

		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, output);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		if (this.tiered && this.recipe.requiresTier()) {
			this.required.draw(minecraft, this.reqX, this.reqY);
		}
	}
	
	@Override
	public List getTooltipStrings(int mouseX, int mouseY) {
		if (this.tiered && this.recipe.requiresTier() && mouseX > this.reqX - 1 && mouseX < this.reqX + 3 && mouseY > this.reqY - 1 && mouseY < this.reqY + 15) {
			return Utils.asList(Utils.localize("tooltip.ec.requires_table", this.recipe.getTier()));
		}
		return Collections.emptyList();
	}
}
