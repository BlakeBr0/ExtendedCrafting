package com.blakebr0.extendedcrafting.compat.jei.category;

import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CompressorCraftingCategory implements IRecipeCategory<RecipeHolder<ICompressorRecipe>> {
	private static final Identifier TEXTURE = ExtendedCrafting.resource("textures/jei/compressor.png");
	public static final IRecipeHolderType<ICompressorRecipe> RECIPE_TYPE = IRecipeHolderType.create(ExtendedCrafting.resource("compressor"));

	private final IDrawable background;
	private final IDrawable icon;

	public CompressorCraftingCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 149, 78);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.COMPRESSOR.get()));
	}

	@Override
	public IRecipeType<RecipeHolder<ICompressorRecipe>> getRecipeType() {
		return RECIPE_TYPE;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("jei.category.extendedcrafting.compressor");
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
	public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<ICompressorRecipe> recipeHolder, IRecipeSlotsView slots, double mouseX, double mouseY) {
        var recipe = recipeHolder.value();

		if (mouseX > 1 && mouseX < 14 && mouseY > 1 && mouseY < 78) {
			tooltip.add(Formatting.energy(recipe.getPowerCost()));
			tooltip.add(Formatting.energyPerTick(recipe.getPowerRate()));
		}

		if (mouseX > 54 && mouseX < 78 && mouseY > 58 && mouseY < 68) {
			tooltip.add(ModTooltips.NUM_ITEMS.args(Formatting.number(recipe.getIngredient().count())).color(ChatFormatting.WHITE).toComponent());
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ICompressorRecipe> recipeHolder, IFocusGroup focuses) {
        var recipe = recipeHolder.value();
		var level = Minecraft.getInstance().level;

		assert level != null;
// TODO jei
//		var inputs = recipe.getIngredients();
//		var catalyst = recipe.getCatalyst();
//		var output = recipe.getResultItem(level.registryAccess());
//
//		builder.addSlot(RecipeIngredientRole.INPUT, 58, 31).addIngredients(inputs.get(0));
//		builder.addSlot(RecipeIngredientRole.INPUT, 31, 31).addIngredients(catalyst);
//		builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 31).addItemStack(output);
	}
}