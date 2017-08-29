package com.blakebr0.extendedcrafting.compat.jei.compressor;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CompressorCraftingCategory implements IRecipeCategory<CompressorCraftingWrapper> {

	public static final String UID = "extendedcrafting:compressor";

	private final IDrawable background;

	public CompressorCraftingCategory(IGuiHelper helper) {
		ResourceLocation texture = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/compressor.png");
		this.background = helper.createDrawable(texture, 6, 6, 154, 90);
	}

	@Override
	public String getUid() {
		return this.UID;
	}

	@Override
	public String getTitle() {
		return Utils.localize("jei.ec.compressor");
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public void setRecipe(IRecipeLayout layout, CompressorCraftingWrapper wrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		layout.getItemStacks().init(0, true, 58, 31);
		layout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0).get(0));

		layout.getItemStacks().init(1, true, 31, 31);
		layout.getItemStacks().set(1, ingredients.getInputs(ItemStack.class).get(0).get(1));

		layout.getItemStacks().init(2, false, 128, 31);
		layout.getItemStacks().set(2, ingredients.getOutputs(ItemStack.class).get(0));
	}

	@Override
	public String getModName() {
		return ExtendedCrafting.NAME;
	}
}