package com.blakebr0.extendedcrafting.compat.jei.category;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompressorCraftingCategory implements IRecipeCategory<ICompressorRecipe> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/jei/compressor.png");
	public static final ResourceLocation UID = new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor");

	private final IDrawable background;
	private final IDrawable icon;

	public CompressorCraftingCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 149, 78);
		this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.COMPRESSOR.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends ICompressorRecipe> getRecipeClass() {
		return ICompressorRecipe.class;
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
	public List<Component> getTooltipStrings(ICompressorRecipe recipe, double mouseX, double mouseY) {
		if (mouseX > 1 && mouseX < 14 && mouseY > 1 && mouseY < 78) {
			return Arrays.asList(
					Component.literal(NumberFormat.getInstance().format(recipe.getPowerCost()) + " FE"),
					Component.literal(NumberFormat.getInstance().format(recipe.getPowerRate()) + " FE/t")
			);
		}

		if (mouseX > 54 && mouseX < 78 && mouseY > 58 && mouseY < 68) {
			return Collections.singletonList(ModTooltips.NUM_ITEMS.args(recipe.getInputCount()).color(ChatFormatting.WHITE).build());
		}

		return Collections.emptyList();
	}

	@Override
	public void setIngredients(ICompressorRecipe recipe, IIngredients ingredients) {
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());

		NonNullList<Ingredient> inputs = NonNullList.create();

		inputs.addAll(recipe.getIngredients());
		inputs.add(recipe.getCatalyst());

		ingredients.setInputIngredients(inputs);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, ICompressorRecipe recipe, IIngredients ingredients) {
		var stacks = layout.getItemStacks();
		var inputs = ingredients.getInputs(VanillaTypes.ITEM);
		var outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

		stacks.init(0, true, 57, 30);
		stacks.init(1, true, 30, 30);
		stacks.init(2, false, 127, 30);

		stacks.set(0, inputs.get(0));
		stacks.set(1, inputs.get(1));
		stacks.set(2, outputs);
	}
}