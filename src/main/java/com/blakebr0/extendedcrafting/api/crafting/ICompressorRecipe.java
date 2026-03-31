package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

/**
 * Used to represent a Compressor recipe for the recipe type
 */
public interface ICompressorRecipe extends Recipe<CraftingInput> {
    SizedIngredient getIngredient();
    Ingredient getCatalyst();
    int getPowerCost();
    int getPowerRate();

    @Override
    default boolean isSpecial() {
        return true;
    }

    @Override
    default boolean showNotification() {
        return false;
    }

    @Override
    default String group() {
        return "extendedcrafting:compressor";
    }

    @Override
    default RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    default PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }
}
