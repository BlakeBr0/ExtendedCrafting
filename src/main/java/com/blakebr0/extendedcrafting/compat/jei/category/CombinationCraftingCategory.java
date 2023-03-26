package com.blakebr0.extendedcrafting.compat.jei.category;

import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;

public class CombinationCraftingCategory implements IRecipeCategory<ICombinationRecipe> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/combination_crafting.png");
	public static final RecipeType<ICombinationRecipe> RECIPE_TYPE = RecipeType.create(ExtendedCrafting.MOD_ID, "combination", ICombinationRecipe.class);

	private final IDrawable background;
	private final IDrawable icon;

	public CombinationCraftingCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 140, 171);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CRAFTING_CORE.get()));
	}

	@Override
	public RecipeType<ICombinationRecipe> getRecipeType() {
		return RECIPE_TYPE;
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
	public List<Component> getTooltipStrings(ICombinationRecipe recipe, IRecipeSlotsView slots, double mouseX, double mouseY) {
		if (mouseX > 1 && mouseX < 14 && mouseY > 9 && mouseY < 86) {
			return List.of(
					Formatting.energy(recipe.getPowerCost()),
					Formatting.energyPerTick(recipe.getPowerRate())
			);
		}

		if (mouseX > 5 && mouseX < 23 && mouseY > 144 && mouseY < 165) {
			return recipe.getInputsList();
		}

		return List.of();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ICombinationRecipe recipe, IFocusGroup focuses) {
		var level = Minecraft.getInstance().level;

		assert level != null;

		var inputs = recipe.getIngredients();
		var output = recipe.getResultItem(level.registryAccess());

		builder.addSlot(RecipeIngredientRole.INPUT, 77, 47).addIngredients(inputs.get(0));

		double angleBetweenEach = 360.0 / (inputs.size() - 1);
		Point point = new Point(53, 8), center = new Point(74, 47);

		for (int i = 1; i < inputs.size(); i++) {
			builder.addSlot(RecipeIngredientRole.INPUT, point.x, point.y).addIngredients(inputs.get(i));

			point = rotatePoint(point, center, angleBetweenEach);
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 150).addItemStack(output);
	}

	private static Point rotatePoint(Point in, Point about, double degrees) {
		double rad = degrees * Math.PI / 180.0;
		double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
		double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
		return new Point((int) newX, (int) newY);
	}
}