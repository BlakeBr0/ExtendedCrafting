package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;

/**
 * Used to represent a Compressor recipe for the recipe type
 */
public interface ICompressorRecipe extends Recipe<CraftingInput> {
    /**
     * Get the count for the ingredient at the requested index
     * @param index the ingredient index
     * @return either the count or -1 if invalid
     */
    int getCount(int index);

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
