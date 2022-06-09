package com.blakebr0.extendedcrafting.compat.jei.category;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombinationCraftingCategory implements IRecipeCategory<ICombinationRecipe> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/jei/combination_crafting.png");
	public static final ResourceLocation UID = new ResourceLocation(ExtendedCrafting.MOD_ID, "combination");

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
	public Component getTitle() {
		return Localizable.of("jei.category.extendedcrafting.combination").build();
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
	public List<Component> getTooltipStrings(ICombinationRecipe recipe, double mouseX, double mouseY) {
		if (mouseX > 1 && mouseX < 14 && mouseY > 9 && mouseY < 86) {
			return Arrays.asList(
					Component.literal(NumberFormat.getInstance().format(recipe.getPowerCost()) + " FE"),
					Component.literal(NumberFormat.getInstance().format(recipe.getPowerRate()) + " FE/t")
			);
		}

		if (mouseX > 5 && mouseX < 23 && mouseY > 144 && mouseY < 165) {
			return recipe.getInputsList();
		}

		return Collections.emptyList();
	}

	@Override
	public void setIngredients(ICombinationRecipe recipe, IIngredients ingredients) {
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
		ingredients.setInputIngredients(recipe.getIngredients());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, ICombinationRecipe recipe, IIngredients ingredients) {
		var stacks = layout.getItemStacks();
		var inputs = ingredients.getInputs(VanillaTypes.ITEM);
		var outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

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