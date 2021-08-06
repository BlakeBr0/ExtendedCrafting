package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Used to represent a Compressor recipe for the recipe type
 */
public interface ICompressorRecipe extends Recipe<Container> {
    int getInputCount();
    Ingredient getCatalyst();
    int getPowerCost();
    int getPowerRate();
}
