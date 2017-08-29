package com.blakebr0.extendedcrafting.compat.jei.combinationcrafting;

import java.util.List;

import com.blakebr0.extendedcrafting.ExtendedCrafting;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class CombinationCraftingCategory extends BlankRecipeCategory<CombinationCraftingWrapper> {

    public static final String UID = "extendedcrafting:combination_crafting";

    private final IDrawable background;

    public CombinationCraftingCategory(IGuiHelper helper){
        ResourceLocation texture = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/combination_crafting.png");
        this.background = helper.createDrawable(texture, 10, 14, 156, 92);
    }

    @Override
    public String getUid() {
        return this.UID;
    }

    @Override
    public String getTitle() {
        return new TextComponentTranslation("jei.ec.combination_crafting").getFormattedText();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout layout, CombinationCraftingWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();
        
        stacks.init(0, false, 127, 37);
        stacks.set(0, ingredients.getOutputs(ItemStack.class));

        stacks.init(9, true, 36, 37);
        stacks.set(9, wrapper.getInput());
        
        stacks.init(1, true, 7, 8);
        stacks.init(2, true, 36, 8);
        stacks.init(3, true, 65, 8);
        stacks.init(4, true, 65, 37);
        stacks.init(5, true, 65, 66);
        stacks.init(6, true, 36, 66);
        stacks.init(7, true, 7, 66);
        stacks.init(8, true, 7, 37);
        for(int i = 0; i < 8; i++){
        	stacks.set(i + 1, ingredients.getInputs(ItemStack.class).get(i));
        }
    }
}