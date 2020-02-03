package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

/**
 * Used to represent a Compressor recipe for the recipe type
 */
public interface ICompressorRecipe extends IRecipe<IInventory> {
    int getInputCount();
    Ingredient getCatalyst();
    int getPowerCost();
    int getPowerRate();
}
