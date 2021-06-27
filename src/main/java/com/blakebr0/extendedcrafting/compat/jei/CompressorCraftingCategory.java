package com.blakebr0.extendedcrafting.compat.jei;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

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
	public String getTitle() {
		return Localizable.of("jei.category.extendedcrafting.compressor").buildString();
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
	public List<ITextComponent> getTooltipStrings(ICompressorRecipe recipe, double mouseX, double mouseY) {
		if (mouseX > 1 && mouseX < 14 && mouseY > 1 && mouseY < 78) {
			return Arrays.asList(
					new StringTextComponent(NumberFormat.getInstance().format(recipe.getPowerCost()) + " FE"),
					new StringTextComponent(NumberFormat.getInstance().format(recipe.getPowerRate()) + " FE/t")
			);
		}

		if (mouseX > 54 && mouseX < 78 && mouseY > 58 && mouseY < 68) {
			return Collections.singletonList(ModTooltips.NUM_ITEMS.args(recipe.getInputCount()).color(TextFormatting.WHITE).build());
		}

		return Collections.emptyList();
	}

	@Override
	public void setIngredients(ICompressorRecipe recipe, IIngredients ingredients) {
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());

		NonNullList<Ingredient> inputs = NonNullList.create();
		inputs.addAll(recipe.getIngredients());
		inputs.add(recipe.getCatalyst());

		ingredients.setInputIngredients(inputs);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, ICompressorRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

		stacks.init(0, true, 57, 30);
		stacks.init(1, true, 30, 30);
		stacks.init(2, false, 127, 30);

		stacks.set(0, inputs.get(0));
		stacks.set(1, inputs.get(1));
		stacks.set(2, outputs);
	}
}