package com.blakebr0.extendedcrafting.compat.jei.endercrafter;

import java.util.List;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.compat.jei.CompatJEI;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.TableShapelessWrapper;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
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

	private final IDrawable background;
	private final ICraftingGridHelper gridHelper;
	private final IDrawableAnimated arrow;

	public EnderCrafterCategory(IGuiHelper helper) {
		ResourceLocation texture = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/ender_crafter.png");
		this.background = helper.createDrawable(texture, 24, 11, 126, 64);
		this.gridHelper = CompatJEI.jeiHelpers.getGuiHelper().createCraftingGridHelper(1, 0);
		
		IDrawableStatic arrowDrawable = CompatJEI.jeiHelpers.getGuiHelper().createDrawable(texture, 195, 0, 24, 17);
		this.arrow = CompatJEI.jeiHelpers.getGuiHelper().createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
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
		return background;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		arrow.draw(minecraft, 66, 24);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class).get(0);

		stacks.init(0, false, 99, 24);
		stacks.set(0, outputs);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int index = 1 + j + (i * 3);
				stacks.init(index, true, j * 18 + 5, i * 18 + 6);
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

		layout.setRecipeTransferButton(105, 47);
	}
}
