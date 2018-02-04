package com.blakebr0.extendedcrafting.compat.jei.combinationcrafting;

import java.awt.Point;
import java.util.List;

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

	private final IDrawable background;

	public CombinationCraftingCategory(IGuiHelper helper) {
		ResourceLocation texture = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/combination_crafting.png");
		this.background = helper.createDrawable(texture, 6, 10, 158, 170);
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
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public void setRecipe(IRecipeLayout layout, CombinationCraftingWrapper wrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		layout.getItemStacks().init(0, true, 78, 43);
		layout.getItemStacks().set(0, wrapper.getInput());

		int index = 1;
		double angleBetweenEach = 360.0 / ingredients.getInputs(ItemStack.class).size();
		Point point = new Point(57, 4), center = new Point(78, 43);

		for (List<ItemStack> o : ingredients.getInputs(ItemStack.class)) {
			layout.getItemStacks().init(index, true, point.x, point.y);
			layout.getItemStacks().set(index, o);
			index += 1;
			point = rotatePoint(point, center, angleBetweenEach);
		}

		layout.getItemStacks().init(index, false, 77, 149);
		layout.getItemStacks().set(index, ingredients.getOutputs(ItemStack.class).get(0));
	}

	private Point rotatePoint(Point in, Point about, double degrees) {
		double rad = degrees * Math.PI / 180.0;
		double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
		double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
		return new Point((int) newX, (int) newY);
	}

	@Override
	public String getModName() {
		return ExtendedCrafting.NAME;
	}
}