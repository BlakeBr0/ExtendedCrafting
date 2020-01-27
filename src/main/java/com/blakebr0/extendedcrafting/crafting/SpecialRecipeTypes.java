package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.crafting.ISpecialRecipeType;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;

public class SpecialRecipeTypes {
    public static final ISpecialRecipeType<CombinationRecipe> COMBINATION = new ISpecialRecipeType<CombinationRecipe>() {
        @Override
        public String toString() {
            return "Combination Crafting Recipe Type";
        }
    };
    public static final ISpecialRecipeType<CompressorRecipe> COMPRESSOR = new ISpecialRecipeType<CompressorRecipe>() {
        @Override
        public String toString() {
            return "Compressor Recipe Type";
        }
    };
}
