package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

/**
 * Used to represent a Combination recipe for the recipe type
 */
public interface ICombinationRecipe extends IRecipe<IInventory> {
    Ingredient getInput();
    int getPowerCost();
    int getPowerRate();
}
