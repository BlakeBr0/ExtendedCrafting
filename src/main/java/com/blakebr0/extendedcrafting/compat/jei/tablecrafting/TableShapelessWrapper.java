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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class TableShapelessWrapper implements IRecipeWrapper {

	private final TableRecipeShapeless recipe;
	private final IJeiHelpers helpers;
	private final IDrawable required, shapeless;
	private int reqX = 0, reqY = 0, slX = 0, slY = 0;
	private boolean tiered = false;
	
	public TableShapelessWrapper(IJeiHelpers helpers, TableRecipeShapeless recipe) {
		this.helpers = helpers;
		this.recipe = recipe;
		this.required = helpers.getGuiHelper().createDrawable(CompatJEI.ICONS, 0, 0, 3, 15);
		this.shapeless = helpers.getGuiHelper().createDrawable(CompatJEI.JEI_RESOURCES, 196, 0, 19, 15);
	}

	public TableShapelessWrapper(IJeiHelpers helpers, TableRecipeShapeless recipe, int reqX, int reqY, int slX, int slY) {
		this.helpers = helpers;
		this.recipe = recipe;
		this.required = helpers.getGuiHelper().createDrawable(CompatJEI.ICONS, 0, 0, 3, 15);
		this.shapeless = helpers.getGuiHelper().createDrawable(CompatJEI.JEI_RESOURCES, 196, 0, 19, 15);
		this.reqX = reqX;
		this.reqY = reqY;
		this.slX = slX;
		this.slY = slY;
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
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5, 0.5, 1.0);
		this.shapeless.draw(minecraft, this.slX, this.slY);
		GlStateManager.popMatrix();
	}
	
	@Override
	public List getTooltipStrings(int mouseX, int mouseY) {
		if (this.tiered && this.recipe.requiresTier() && mouseX > this.reqX - 1 && mouseX < this.reqX + 3 && mouseY > this.reqY - 1 && mouseY < this.reqY + 15) {
			return Utils.asList(Utils.localize("tooltip.ec.requires_table", this.recipe.getTier()));
		}
		
		int sX = this.slX / 2, sY = this.slY / 2;
		if (mouseX > sX - 1 && mouseX < sX + 10 && mouseY > sY - 1 && mouseY < sY + 8) {
			return Utils.asList(Utils.localize("jei.tooltip.shapeless.recipe"));
		}
		
		return Collections.emptyList();
	}
}
