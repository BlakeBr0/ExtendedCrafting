package com.blakebr0.extendedcrafting.compat.jei;

import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.List;

public class CombinationCraftingCategory implements IRecipeCategory<ICombinationRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(ExtendedCrafting.MOD_ID, "combination");
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/combination_crafting.png");

	private final IDrawable background;
	private final IDrawable icon;

	public CombinationCraftingCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 140, 171);
		this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.CRAFTING_CORE.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends ICombinationRecipe> getRecipeClass() {
		return ICombinationRecipe.class;
	}

	@Override
	public String getTitle() {
		return Localizable.of("jei.category.extendedcrafting.combination").buildString();
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setIngredients(ICombinationRecipe recipe, IIngredients ingredients) {
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());

		NonNullList<Ingredient> inputs = NonNullList.create();
		inputs.add(recipe.getInput());
		inputs.addAll(recipe.getIngredients());

		ingredients.setInputIngredients(inputs);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, ICombinationRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

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