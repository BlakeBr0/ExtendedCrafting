package com.blakebr0.extendedcrafting.compat.jei;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class CompressorCraftingCategory implements IRecipeCategory<ICompressorRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor");
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource(ExtendedCrafting.MOD_ID, "textures/jei/compressor.png");

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
	public void setIngredients(ICompressorRecipe recipe, IIngredients ingredients) {
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());


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