package com.blakebr0.extendedcrafting.compat.jei.combinationcrafting;

import java.awt.Point;
import java.util.List;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CombinationCraftingCategory implements IRecipeCategory<CombinationCraftingWrapper> {

	public static final String UID = "extendedcrafting:combination_crafting";
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource(ExtendedCrafting.MOD_ID, "textures/jei/combination_crafting.png");

	private final IDrawable background;

	public CombinationCraftingCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 140, 171);
	}

	@Override
	public String getUid() {
		return this.UID;
	}

	@Override
	public String getTitle() {
		return Utils.localize("jei.ec.combination_crafting");
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
	public void setRecipe(IRecipeLayout layout, CombinationCraftingWrapper wrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();
		
		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class).get(0);

		stacks.init(0, false, 76, 149);
		stacks.init(1, true, 76, 46);
		stacks.set(0, outputs);
		stacks.set(1, inputs.get(0));

		double angleBetweenEach = 360.0 / (inputs.size() - 1);
		Point point = new Point(53, 8), center = new Point(74, 47);

		for (int i = 2; i < inputs.size() + 1; i++) {
			stacks.init(i, true, point.x, point.y);
			stacks.set(i, inputs.get(i - 1));
			point = rotatePoint(point, center, angleBetweenEach);
		}
	}

	private Point rotatePoint(Point in, Point about, double degrees) {
		double rad = degrees * Math.PI / 180.0;
		double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
		double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
		return new Point((int) newX, (int) newY);
	}
}