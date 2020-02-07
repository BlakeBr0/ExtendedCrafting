package com.blakebr0.extendedcrafting.compat.jei;

import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class EnderCrafterCategory implements IRecipeCategory<IEnderCrafterRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(ExtendedCrafting.MOD_ID, "ender_crafting");
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/ender_crafting.png");

	private final IDrawable background;
	private final IDrawableAnimated arrow;
	private final IDrawable icon;

	public EnderCrafterCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);

		IDrawableStatic arrowDrawable = helper.createDrawable(TEXTURE, 195, 0, 24, 17);
		this.arrow = helper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
		this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.ENDER_CRAFTER.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends IEnderCrafterRecipe> getRecipeClass() {
		return IEnderCrafterRecipe.class;
	}

	@Override
	public String getTitle() {
		return Localizable.of("jei.category.extendedcrafting.ender_crafting").buildString();
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
	public void setIngredients(IEnderCrafterRecipe recipe, IIngredients ingredients) {
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
		ingredients.setInputIngredients(recipe.getIngredients());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IEnderCrafterRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

		stacks.init(0, false, 94, 18);
		stacks.set(0, outputs);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int index = 1 + j + (i * 3);
				stacks.init(index, true, j * 18, i * 18);
			}
		}

		int xd = 1;
		for (List<ItemStack> stack : inputs) {
			stacks.set(xd, stack);
			xd++;
		}

//		if (recipe instanceof TableShapelessWrapper) {
//			layout.setShapeless();
//		}

		layout.moveRecipeTransferButton(122, 41);
	}

	@Override
	public void draw(IEnderCrafterRecipe recipe, double mouseX, double mouseY) {
		this.arrow.draw(61, 19);
	}
}
