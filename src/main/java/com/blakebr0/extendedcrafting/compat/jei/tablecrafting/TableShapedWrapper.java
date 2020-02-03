package com.blakebr0.extendedcrafting.compat.jei.tablecrafting;

import java.util.Collections;
import java.util.List;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.compat.jei.JeiCompat;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShaped;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class TableShapedWrapper implements IShapedCraftingRecipeWrapper {

	private final TableRecipeShaped recipe;
	private final IJeiHelpers helpers;
	private final IDrawable required;
	private int iconsX = 0, iconsY = 0;
	private boolean tiered = false;
	
	public TableShapedWrapper(IJeiHelpers helpers, TableRecipeShaped recipe) {
		this.helpers = helpers;
		this.recipe = recipe;
		this.required = helpers.getGuiHelper().createDrawable(JeiCompat.ICONS, 0, 0, 15, 15);
	}

	public TableShapedWrapper(IJeiHelpers helpers, TableRecipeShaped recipe, int iconsX, int iconsY) {
		this.helpers = helpers;
		this.recipe = recipe;
		this.required = helpers.getGuiHelper().createDrawable(JeiCompat.ICONS, 0, 0, 15, 15);
		this.iconsX = iconsX;
		this.iconsY = iconsY;
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
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5, 0.5, 1.0);
			
			this.required.draw(minecraft, this.iconsX, this.iconsY);
			
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public List getTooltipStrings(int mouseX, int mouseY) {
		int sX = this.iconsX / 2, sY = this.iconsY / 2;
		
		if (this.tiered && this.recipe.requiresTier() && mouseX > sX - 1 && mouseX < sX + 10 && mouseY > sY - 1 && mouseY < sY + 8) {
			return Utils.asList(Utils.localize("tooltip.ec.requires_table", this.recipe.getTier()));
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public int getWidth() {
		return this.recipe.getWidth();
	}

	@Override
	public int getHeight() {
		return this.recipe.getHeight();
	}
}
