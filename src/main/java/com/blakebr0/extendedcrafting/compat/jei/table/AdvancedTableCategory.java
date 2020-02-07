package com.blakebr0.extendedcrafting.compat.jei.table;

import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedTableRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessTableRecipe;
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

public class AdvancedTableCategory implements IRecipeCategory<ITableRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(ExtendedCrafting.MOD_ID, "advanced_crafting");
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/advanced_crafting.png");

	private final IDrawable background;
	private final IDrawable icon;

	public AdvancedTableCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 150, 90);
		this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.ADVANCED_TABLE.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends ITableRecipe> getRecipeClass() {
		return ITableRecipe.class;
	}

	@Override
	public String getTitle() {
		return Localizable.of("jei.category.extendedcrafting.advanced_crafting").buildString();
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
	public void setIngredients(ITableRecipe recipe, IIngredients ingredients) {
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
		ingredients.setInputIngredients(recipe.getIngredients());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, ITableRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

		stacks.init(0, false, 128, 35);
		stacks.set(0, outputs);

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				int index = 1 + j + (i * 5);
				stacks.init(index, true, j * 18, i * 18);
			}
		}

		if (recipe instanceof ShapedTableRecipe) {
			ShapedTableRecipe shaped = (ShapedTableRecipe) recipe;

			int stackIndex = 0;
			for (int i = 0; i < shaped.getHeight(); i++) {
				for (int j = 0; j < shaped.getWidth(); j++) {
					int index = 1 + (i * 5) + j;

					stacks.set(index, inputs.get(stackIndex));

					stackIndex++;
				}
			}
		} else if (recipe instanceof ShapelessTableRecipe) {
			for (int i = 0; i < inputs.size(); i++) {
				stacks.set(i + 1, inputs.get(i));
			}
		}

		layout.moveRecipeTransferButton(137, 77);
	}
}
