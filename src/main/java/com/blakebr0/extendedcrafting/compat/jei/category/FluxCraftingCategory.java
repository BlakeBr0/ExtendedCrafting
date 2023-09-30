package com.blakebr0.extendedcrafting.compat.jei.category;

import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FluxCraftingCategory implements IRecipeCategory<IFluxCrafterRecipe> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/flux_crafting.png");
	public static final RecipeType<IFluxCrafterRecipe> RECIPE_TYPE = RecipeType.create(ExtendedCrafting.MOD_ID, "flux_crafting", IFluxCrafterRecipe.class);

	private final IDrawable background;
	private final IDrawable icon;

	public FluxCraftingCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 149, 78);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FLUX_CRAFTER.get()));
	}

	@Override
	public RecipeType<IFluxCrafterRecipe> getRecipeType() {
		return RECIPE_TYPE;
	}

	@Override
	public Component getTitle() {
		return Localizable.of("jei.category.extendedcrafting.flux_crafting").build();
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
	public List<Component> getTooltipStrings(IFluxCrafterRecipe recipe, IRecipeSlotsView slots, double mouseX, double mouseY) {
		if (mouseX > 1 && mouseX < 14 && mouseY > 1 && mouseY < 78) {
			return List.of(
					Formatting.energy(recipe.getPowerRequired()),
					ModTooltips.PER_ALTERNATOR.args(Formatting.energyPerTick(recipe.getPowerRate())).color(ChatFormatting.WHITE).build()
			);
		}

		return List.of();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IFluxCrafterRecipe recipe, IFocusGroup focuses) {
		var level = Minecraft.getInstance().level;

		assert level != null;

		var inputs = recipe.getIngredients();
		var output = recipe.getResultItem(level.registryAccess());

		if (recipe instanceof ShapedFluxCrafterRecipe shaped) {
			int stackIndex = 0;

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					var slot = builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 26, i * 18 + 13);

					if (i < shaped.getHeight() && j < shaped.getWidth()) {
						slot.addIngredients(inputs.get(stackIndex++));
					}
				}
			}
		} else if (recipe instanceof ShapelessFluxCrafterRecipe) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int index = j + (i * 3);

					if (index < inputs.size()) {
						builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 26, i * 18 + 13).addIngredients(inputs.get(index));
					}
				}
			}

			builder.setShapeless();
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 31).addItemStack(output);

		builder.moveRecipeTransferButton(134, 63);
	}
}