package com.blakebr0.extendedcrafting.compat.jei.table;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.compat.jei.JeiCompat;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedTableRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessTableRecipe;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;

public class UltimateTableCategory implements IRecipeCategory<ITableRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(ExtendedCrafting.MOD_ID, "ultimate_crafting");
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/jei/ultimate_crafting.png");
	
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable required;
    private final IDrawable shapeless;
	
    public UltimateTableCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 162, 195);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.ULTIMATE_TABLE.get()));
		this.required = helper.createDrawable(JeiCompat.ICONS, 0, 0, 15, 15);
		this.shapeless = helper.createDrawable(JeiCompat.ICONS, 17, 0, 19, 15);
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
		return Localizable.of("jei.category.extendedcrafting.ultimate_crafting").buildString();
	}

	@Override
	public IDrawable getBackground(){
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void draw(ITableRecipe recipe, MatrixStack stack, double mouseX, double mouseY) {
		stack.pushPose();
		stack.scale(0.5F, 0.5F, 0.5F);
		boolean shapeless = recipe instanceof ShapelessTableRecipe;
		if (recipe.hasRequiredTier())
			this.required.draw(stack, shapeless ? 286 : 306, 329);
		if (shapeless)
			this.shapeless.draw(stack, 306, 329);
		stack.popPose();
	}

	@Override
	public List<ITextComponent> getTooltipStrings(ITableRecipe recipe, double mouseX, double mouseY) {
		boolean shapeless = recipe instanceof ShapelessTableRecipe;
		int sX = (shapeless ? 286 : 306) / 2, sY = 329 / 2;

		if (recipe.hasRequiredTier() && mouseX > sX - 1 && mouseX < sX + 8 && mouseY > sY - 1 && mouseY < sY + 8) {
			return Collections.singletonList(ModTooltips.REQUIRES_TABLE.args(recipe.getTier()).color(TextFormatting.WHITE).build());
		}

		if (shapeless && mouseX > sX + 10 && mouseX < sX + 20 && mouseY > sY - 1 && mouseY < sY + 8) {
			return Collections.singletonList(Localizable.of("jei.tooltip.shapeless.recipe").build());
		}

		return Collections.emptyList();
	}

	@Override
	public void setIngredients(ITableRecipe recipe, IIngredients ingredients) {
    	ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    	ingredients.setInputIngredients(recipe.getIngredients());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, ITableRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

		stacks.init(0, false, 84, 173);
		stacks.set(0, outputs);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int index = 1 + j + (i * 9);
				stacks.init(index, true, j * 18, i * 18);
			}
		}

		if (recipe instanceof ShapedTableRecipe) {
			ShapedTableRecipe shaped = (ShapedTableRecipe) recipe;

			int heightOffset = Math.floorDiv(9 - shaped.getHeight(), 2);
			int widthOffset = Math.floorDiv(9 - shaped.getWidth(), 2);
			int stackIndex = 0;

			for (int i = heightOffset; i < shaped.getHeight() + heightOffset; i++) {
				for (int j = widthOffset; j < shaped.getWidth() + widthOffset; j++) {
					int index = 1 + (i * 9) + j;

					stacks.set(index, inputs.get(stackIndex));

					stackIndex++;
				}
			}
		} else if (recipe instanceof ShapelessTableRecipe) {
			for (int i = 0; i < inputs.size(); i++) {
				stacks.set(i + 1, inputs.get(i));
			}
		}

		layout.moveRecipeTransferButton(149, 182);
	}
}
