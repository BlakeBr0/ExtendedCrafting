package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;

import java.util.List;
import java.util.Optional;

/**
 * Used to represent a Flux Crafting recipe for the recipe type
 */
public interface IFluxCrafterRecipe extends Recipe<CraftingInput> {
    int getPowerRequired();
    int getPowerRate();

    /**
     * Returns the ingredients for shapeless recipes
     * @return the list of ingredients
     */
    default List<Ingredient> getIngredients() {
        return List.of();
    }

    /**
     * Returns the ingredients for shaped recipes; empty Optionals represent empty slots
     * @return the grid-positioned list of ingredients
     */
    default List<Optional<Ingredient>> getPositionedIngredients() {
        return List.of();
    }

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
        return "extendedcrafting:flux_crafter";
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
