package com.blakebr0.extendedcrafting.api.crafting;

import com.blakebr0.extendedcrafting.api.TableCraftingInput;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;

/**
 * Used to represent an Extended Crafting Table recipe for the recipe type
 */
public interface ITableRecipe extends Recipe<TableCraftingInput> {
    int getTier();
    boolean hasRequiredTier();
    NonNullList<ItemStack> getRemainingItems(TableCraftingInput input);

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
        return "extendedcrafting:table";
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
