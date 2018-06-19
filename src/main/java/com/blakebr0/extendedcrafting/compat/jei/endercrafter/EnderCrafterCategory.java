package com.blakebr0.extendedcrafting.compat.jei.endercrafter;

import java.util.List;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.TableShapelessWrapper;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnderCrafterCategory implements IRecipeCategory {

	public static final String UID = "extendedcrafting:ender_crafting";
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource(ExtendedCrafting.MOD_ID, "textures/jei/ender_crafting.png");

	private final IDrawable background;
	private final IDrawableAnimated arrow;

	public EnderCrafterCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);
		
		IDrawableStatic arrowDrawable = helper.createDrawable(TEXTURE, 195, 0, 24, 17);
		this.arrow = helper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return Utils.localize("jei.ec.ender_crafting");
	}

	@Override
	public String getModName() {
		return ExtendedCrafting.NAME;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		this.arrow.draw(minecraft, 61, 19);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class).get(0);

		stacks.init(0, false, 94, 18);
		stacks.set(0, outputs);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int index = 1 + j + (i * 3);
				stacks.init(index, true, j * 18, i * 18);
			}
		}

		int xd = 1;
		for (List<ItemStack> stack : inputs) {
			stacks.set(xd, stack);
			xd++;
		}
		
		if (wrapper instanceof TableShapelessWrapper) {
			layout.setShapeless();
		}

		layout.setRecipeTransferButton(122, 41);
	}
}
