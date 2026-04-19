package com.blakebr0.extendedcrafting.compat.jei.category;

import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.awt.*;

public class CombinationCraftingCategory implements IRecipeCategory<RecipeHolder<ICombinationRecipe>> {
	private static final Identifier TEXTURE = ExtendedCrafting.resource("textures/jei/combination_crafting.png");
	public static final IRecipeHolderType<ICombinationRecipe> RECIPE_TYPE = IRecipeHolderType.create(ExtendedCrafting.resource("combination"));

	private final IDrawable background;
	private final IDrawable icon;

	public CombinationCraftingCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 140, 171);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CRAFTING_CORE.get()));
	}

    @Override
    public IRecipeType<RecipeHolder<ICombinationRecipe>> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
	public Component getTitle() {
		return Component.translatable("jei.category.extendedcrafting.combination");
	}

	@Override
	public int getWidth() {
		return this.background.getWidth();
	}

	@Override
	public int getHeight() {
		return this.background.getHeight();
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void draw(RecipeHolder<ICombinationRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor gfx, double mouseX, double mouseY) {
		this.background.draw(gfx);
	}

    @Override
	public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<ICombinationRecipe> recipeHolder, IRecipeSlotsView slots, double mouseX, double mouseY) {
        var recipe = recipeHolder.value();

		if (mouseX > 1 && mouseX < 14 && mouseY > 9 && mouseY < 86) {
			tooltip.add(Formatting.energy(recipe.getPowerCost()));
			tooltip.add(Formatting.energyPerTick(recipe.getPowerRate()));
		}

		if (mouseX > 5 && mouseX < 23 && mouseY > 144 && mouseY < 165) {
			tooltip.addAll(recipe.getInputsList());
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ICombinationRecipe> recipeHolder, IFocusGroup focuses) {
        var recipe = recipeHolder.value();
		var inputs = recipe.getPedestalIngredients();
		var output = recipe.assemble(CraftingInput.EMPTY);

		builder.addSlot(RecipeIngredientRole.INPUT, 77, 47).add(recipe.getCenterIngredient());

		double angleBetweenEach = 360.0 / inputs.size();
		Point point = new Point(53, 8), center = new Point(74, 47);

        for (var input : inputs) {
            builder.addSlot(RecipeIngredientRole.INPUT, point.x, point.y).add(input);

            point = rotatePoint(point, center, angleBetweenEach);
        }

		builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 150).add(output);
	}

	private static Point rotatePoint(Point in, Point about, double degrees) {
		double rad = degrees * Math.PI / 180.0;
		double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
		double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
		return new Point((int) newX, (int) newY);
	}
}