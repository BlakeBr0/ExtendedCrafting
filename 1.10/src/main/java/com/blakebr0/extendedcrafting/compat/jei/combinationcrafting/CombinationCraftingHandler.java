package com.blakebr0.extendedcrafting.compat.jei.combinationcrafting;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;

public class CombinationCraftingHandler implements IRecipeHandler<CombinationRecipe>{

    private IJeiHelpers helpers;

    public CombinationCraftingHandler(IJeiHelpers helpers){
        this.helpers = helpers;
    }

    @Override
    public Class<CombinationRecipe> getRecipeClass(){
        return CombinationRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return CombinationCraftingCategory.UID;
    }

    @Override
    public String getRecipeCategoryUid(CombinationRecipe recipe) {
        return CombinationCraftingCategory.UID;
    }


    @Override
    public IRecipeWrapper getRecipeWrapper(CombinationRecipe recipe) {
        return new CombinationCraftingWrapper(this.helpers, recipe);
    }

    @Override
    public boolean isRecipeValid(CombinationRecipe recipe) {
        if(recipe.getOutput() == null){
            return false;
        }

        int inputCount = 0;
        for (Object input : recipe.getPedestalItems()){
            if(input != null) {
                if(input instanceof List && ((List) input).isEmpty())
                {
                    return false;
                }
                inputCount++;
            }
        }

        if(inputCount > 25)
            return false;

        return inputCount > 0;
    }
}