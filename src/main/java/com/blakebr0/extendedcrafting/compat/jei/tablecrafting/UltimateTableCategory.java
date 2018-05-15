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

public class UltimateTableCategory implements IRecipeCategory {

	public static final String UID = "extendedcrafting:table_crafting_9x9";
	
    private final IDrawable background;
	private final ICraftingGridHelper gridHelper;

    public UltimateTableCategory(IGuiHelper helper) {
        ResourceLocation texture = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/ultimate_crafting.png");
        this.background = helper.createDrawable(texture, 33, 5, 168, 200);
        this.gridHelper = CompatJEI.jeiHelpers.getGuiHelper().createCraftingGridHelper(1, 0);
    }
    
	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return Utils.localize("jei.ec.table_crafting_9x9");
	}

	@Override
	public String getModName() {
		return ExtendedCrafting.NAME;
	}

	@Override
	public IDrawable getBackground(){
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();

        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class).get(0);
        
        stacks.init(0, false, 87, 175);
        stacks.set(0, outputs);

        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                int index = 1 + j + (i * 9);
                stacks.init(index, true, j * 18 + 3, i * 18 + 2);
            }
        }
        
		if (wrapper instanceof TableShapedWrapper) {
			TableShapedWrapper shaped = (TableShapedWrapper) wrapper;
			
			int stackIndex = 0;
			for (int i = 0; i < shaped.getHeight(); i++) {
				for (int j = 0; j < shaped.getWidth(); j++) {
					int index = 1 + (i * 9) + j;
					
					stacks.set(index, inputs.get(stackIndex));
					
					stackIndex++;
				}
			}
		} else if (wrapper instanceof TableShapelessWrapper) {
			int i = 1;
			for (List<ItemStack> stack : inputs) {
				stacks.set(i, stack);
				i++;
			}
			
			layout.setShapeless();
		}
        
        layout.setRecipeTransferButton(111, 184);
	}
}
