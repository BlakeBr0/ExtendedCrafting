package com.blakebr0.extendedcrafting.compat.jei.category;

import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
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

public class CompressorCraftingCategory implements IRecipeCategory<ICompressorRecipe> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/compressor.png");
	public static final RecipeType<ICompressorRecipe> RECIPE_TYPE = RecipeType.create(ExtendedCrafting.MOD_ID, "compressor", ICompressorRecipe.class);

	private final IDrawable background;
	private final IDrawable icon;

	public CompressorCraftingCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 149, 78);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.COMPRESSOR.get()));
	}

	@Override
	public RecipeType<ICompressorRecipe> getRecipeType() {
		return RECIPE_TYPE;
	}

	@Override
	public Component getTitle() {
		return Localizable.of("jei.category.extendedcrafting.compressor").build();
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
	public List<Component> getTooltipStrings(ICompressorRecipe recipe, IRecipeSlotsView slots, double mouseX, double mouseY) {
		if (mouseX > 1 && mouseX < 14 && mouseY > 1 && mouseY < 78) {
			return List.of(
					Formatting.energy(recipe.getPowerCost()),
					Formatting.energyPerTick(recipe.getPowerRate())
			);
		}

		if (mouseX > 54 && mouseX < 78 && mouseY > 58 && mouseY < 68) {
			return List.of(ModTooltips.NUM_ITEMS.args(Formatting.number(recipe.getInputCount())).color(ChatFormatting.WHITE).build());
		}

		return List.of();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ICompressorRecipe recipe, IFocusGroup focuses) {
		var level = Minecraft.getInstance().level;

		assert level != null;

		var inputs = recipe.getIngredients();
		var catalyst = recipe.getCatalyst();
		var output = recipe.getResultItem(level.registryAccess());

		builder.addSlot(RecipeIngredientRole.INPUT, 58, 31).addIngredients(inputs.get(0));
		builder.addSlot(RecipeIngredientRole.INPUT, 31, 31).addIngredients(catalyst);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 31).addItemStack(output);
	}
}