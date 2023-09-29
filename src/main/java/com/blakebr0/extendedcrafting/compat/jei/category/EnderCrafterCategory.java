package com.blakebr0.extendedcrafting.compat.jei.category;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EnderCrafterCategory implements IRecipeCategory<IEnderCrafterRecipe> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/ender_crafting.png");
	public static final RecipeType<IEnderCrafterRecipe> RECIPE_TYPE = RecipeType.create(ExtendedCrafting.MOD_ID, "ender_crafting", IEnderCrafterRecipe.class);

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
	public RecipeType<IEnderCrafterRecipe> getRecipeType() {
		return RECIPE_TYPE;
	}

	@Override
	public Component getTitle() {
		return Localizable.of("jei.category.extendedcrafting.ender_crafting").build();
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
	public void draw(IEnderCrafterRecipe recipe, IRecipeSlotsView slots, GuiGraphics gfx, double mouseX, double mouseY) {
		this.arrow.draw(gfx, 61, 19);
	}

	@Override
	public List<Component> getTooltipStrings(IEnderCrafterRecipe recipe, IRecipeSlotsView slots, double mouseX, double mouseY) {
		if (mouseX > 60 && mouseX < 83 && mouseY > 19 && mouseY < 34) {
			return List.of(ModTooltips.SECONDS.args(recipe.getCraftingTime()).color(ChatFormatting.WHITE).build());
		}

		return List.of();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IEnderCrafterRecipe recipe, IFocusGroup focuses) {
		var level = Minecraft.getInstance().level;

		assert level != null;

		var inputs = recipe.getIngredients();
		var output = recipe.getResultItem(level.registryAccess());

		if (recipe instanceof ShapedEnderCrafterRecipe shaped) {
			int stackIndex = 0;

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					var slot = builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 1, i * 18 + 1);

					if (i < shaped.getHeight() && j < shaped.getWidth()) {
						slot.addIngredients(inputs.get(stackIndex++));
					}
				}
			}
		} else if (recipe instanceof ShapelessEnderCrafterRecipe) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int index = j + (i * 3);

					if (index < inputs.size()) {
						builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 1, i * 18 + 1).addIngredients(inputs.get(index));
					}
				}
			}

			builder.setShapeless();
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStack(output);

		builder.moveRecipeTransferButton(122, 41);
	}
}
