package com.blakebr0.extendedcrafting.compat.jei.category;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;

public class EnderCrafterCategory implements IRecipeCategory<RecipeHolder<IEnderCrafterRecipe>> {
	private static final Identifier TEXTURE = ExtendedCrafting.resource("textures/jei/ender_crafting.png");
	public static final IRecipeHolderType<IEnderCrafterRecipe> RECIPE_TYPE = IRecipeHolderType.create(ExtendedCrafting.resource("ender_crafting"));

	private final IDrawable background;
	private final IDrawableAnimated arrow;
	private final IDrawable icon;

	public EnderCrafterCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);

		var arrow = helper.createDrawable(TEXTURE, 195, 0, 24, 17);

		this.arrow = helper.createAnimatedDrawable(arrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ENDER_CRAFTER.get()));
	}

	@Override
	public IRecipeType<RecipeHolder<IEnderCrafterRecipe>> getRecipeType() {
		return RECIPE_TYPE;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("jei.category.extendedcrafting.ender_crafting");
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
	public void draw(RecipeHolder<IEnderCrafterRecipe> recipe, IRecipeSlotsView slots, GuiGraphicsExtractor gfx, double mouseX, double mouseY) {
		this.background.draw(gfx);
		this.arrow.draw(gfx, 61, 19);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<IEnderCrafterRecipe> recipeHolder, IRecipeSlotsView slots, double mouseX, double mouseY) {
        var recipe = recipeHolder.value();

		if (mouseX > 60 && mouseX < 83 && mouseY > 19 && mouseY < 34) {
			tooltip.add(ModTooltips.SECONDS.args(recipe.getCraftingTime()).color(ChatFormatting.WHITE).toComponent());
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<IEnderCrafterRecipe> recipeHolder, IFocusGroup focuses) {
        var recipe = recipeHolder.value();
		var output = recipe.assemble(CraftingInput.EMPTY);

		if (recipe instanceof ShapedEnderCrafterRecipe shaped) {
			var inputs = recipe.getPositionedIngredients();
			int stackIndex = 0;

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					var slot = builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 1, i * 18 + 1);

					if (i < shaped.getHeight() && j < shaped.getWidth()) {
						inputs.get(stackIndex++).ifPresent(slot::add);
					}
				}
			}
		} else if (recipe instanceof ShapelessEnderCrafterRecipe) {
			var inputs = recipe.getIngredients();
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int index = j + (i * 3);

					if (index < inputs.size()) {
						builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 1, i * 18 + 1).add(inputs.get(index));
					}
				}
			}

			builder.setShapeless();
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).add(output);

		builder.moveRecipeTransferButton(122, 41);
	}
}
