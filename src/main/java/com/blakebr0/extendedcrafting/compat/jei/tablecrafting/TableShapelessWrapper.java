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
	private int iconsX = 0, iconsY = 0;
	private boolean tiered = false;
	
	public TableShapelessWrapper(IJeiHelpers helpers, TableRecipeShapeless recipe) {
		this.helpers = helpers;
		this.recipe = recipe;
		this.required = helpers.getGuiHelper().createDrawable(CompatJEI.ICONS, 0, 0, 15, 15);
		this.shapeless = helpers.getGuiHelper().createDrawable(CompatJEI.ICONS, 17, 0, 19, 15);
	}

	public TableShapelessWrapper(IJeiHelpers helpers, TableRecipeShapeless recipe, int iconsX, int iconsY) {
		this.helpers = helpers;
		this.recipe = recipe;
		this.required = helpers.getGuiHelper().createDrawable(CompatJEI.ICONS, 0, 0, 15, 15);
		this.shapeless = helpers.getGuiHelper().createDrawable(CompatJEI.ICONS, 17, 0, 19, 15);
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
		if (this.tiered) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5, 0.5, 1.0);
			
			if (this.recipe.requiresTier()) {
				this.required.draw(minecraft, this.iconsX - 20, this.iconsY);
			}
			
			this.shapeless.draw(minecraft, this.iconsX, this.iconsY);
			
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public List getTooltipStrings(int mouseX, int mouseY) {
		int sX = this.iconsX / 2, sY = this.iconsY / 2;

		if (this.tiered && this.recipe.requiresTier() && mouseX > sX - 10 && mouseX < sX - 1 && mouseY > sY - 1 && mouseY < sY + 8) {
			return Utils.asList(Utils.localize("tooltip.ec.requires_table", this.recipe.getTier()));
		}
		
		if (mouseX > sX - 1 && mouseX < sX + 10 && mouseY > sY - 1 && mouseY < sY + 8) {
			return Utils.asList(Utils.localize("jei.tooltip.shapeless.recipe"));
		}
		
		return Collections.emptyList();
	}
}
