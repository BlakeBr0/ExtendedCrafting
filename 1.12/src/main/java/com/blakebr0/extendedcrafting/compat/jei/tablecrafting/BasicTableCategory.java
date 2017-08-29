package com.blakebr0.extendedcrafting.compat.jei.tablecrafting;

import java.util.List;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.compat.jei.CompatJEI;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BasicTableCategory implements IRecipeCategory {

	public static final String UID = "extendedcrafting:table_crafting_3x3";

	private final IDrawable background;
	private final ICraftingGridHelper gridHelper;

	public BasicTableCategory(IGuiHelper helper) {
		ResourceLocation texture = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/basic_table.png");
		this.background = helper.createDrawable(texture, 10, 7, 156, 68);
		this.gridHelper = CompatJEI.jeiHelpers.getGuiHelper().createCraftingGridHelper(1, 0);
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return Utils.localize("jei.ec.table_crafting_3x3");
	}

	@Override
	public String getModName() {
		return ExtendedCrafting.NAME;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class).get(0);

		stacks.init(0, false, 113, 25);
		stacks.set(0, outputs);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int index = 1 + j + (i * 3);
				stacks.init(index, true, j * 18 + 21, i * 18 + 7);
			}
		}

		int xd = 1;
		for (List<ItemStack> stack : inputs) {
			stacks.set(xd, stack);
			xd++;
		}

		layout.setRecipeTransferButton(137, 34);
	}
}
