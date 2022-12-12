package com.blakebr0.extendedcrafting.compat.jei.category.table;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.compat.jei.JeiCompat;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedTableRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessTableRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AdvancedTableCategory implements IRecipeCategory<ITableRecipe> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/advanced_crafting.png");
	public static final RecipeType<ITableRecipe> RECIPE_TYPE = RecipeType.create(ExtendedCrafting.MOD_ID, "advanced_crafting", ITableRecipe.class);

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable required;

	public AdvancedTableCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 150, 90);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ADVANCED_TABLE.get()));
		this.required = helper.createDrawable(JeiCompat.ICONS, 0, 0, 15, 15);
	}

	@Override
	public RecipeType<ITableRecipe> getRecipeType() {
		return RECIPE_TYPE;
	}

	@Override
	public Component getTitle() {
		return Localizable.of("jei.category.extendedcrafting.advanced_crafting").build();
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
	public void draw(ITableRecipe recipe, IRecipeSlotsView slots, PoseStack stack, double mouseX, double mouseY) {
		stack.pushPose();
		stack.scale(0.5F, 0.5F, 0.5F);

		var shapeless = recipe instanceof ShapelessTableRecipe;

		if (recipe.hasRequiredTier())
			this.required.draw(stack, shapeless ? 265 : 285, 0);

		stack.popPose();
	}

	@Override
	public List<Component> getTooltipStrings(ITableRecipe recipe, IRecipeSlotsView slots, double mouseX, double mouseY) {
		var shapeless = recipe instanceof ShapelessTableRecipe;
		int sX = (shapeless ? 265 : 285) / 2, sY = 0;

		if (recipe.hasRequiredTier() && mouseX > sX - 1 && mouseX < sX + 8 && mouseY > sY - 1 && mouseY < sY + 8) {
			return List.of(ModTooltips.REQUIRES_TABLE.args(recipe.getTier()).color(ChatFormatting.WHITE).build());
		}

		return List.of();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ITableRecipe recipe, IFocusGroup focuses) {
		var inputs = recipe.getIngredients();
		var output = recipe.getResultItem();

		if (recipe instanceof ShapedTableRecipe shaped) {
			int heightOffset = Math.floorDiv(5 - shaped.getHeight(), 2);
			int widthOffset = Math.floorDiv(5 - shaped.getWidth(), 2);
			int stackIndex = 0;

			for (int i = heightOffset; i < shaped.getHeight() + heightOffset; i++) {
				for (int j = widthOffset; j < shaped.getWidth() + widthOffset; j++) {
					builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 1, i * 18 + 1).addIngredients(inputs.get(stackIndex));

					stackIndex++;
				}
			}
		} else if (recipe instanceof ShapelessTableRecipe) {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					int index = j + (i * 5);

					if (index < inputs.size()) {
						builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 1, i * 18 + 1).addIngredients(inputs.get(index));
					}
				}
			}

			builder.setShapeless();
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 129, 36).addItemStack(output);

		builder.moveRecipeTransferButton(138, 78);
	}
}
