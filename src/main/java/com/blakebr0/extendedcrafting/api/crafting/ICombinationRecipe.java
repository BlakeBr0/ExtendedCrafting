package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

/**
 * Used to represent a Combination recipe for the recipe type
 */
public interface ICombinationRecipe extends IRecipe<IInventory> {
    int getPowerCost();
    int getPowerRate();
    List<String> getInputsList();
}
