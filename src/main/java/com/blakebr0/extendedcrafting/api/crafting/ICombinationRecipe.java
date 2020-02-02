package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

/**
 * Used to represent a Combination recipe for the recipe type
 */
@SuppressWarnings("unchecked")
public interface ICombinationRecipe extends IRecipe<IInventory> {
    default <T extends ICombinationRecipe> T cast() {
        return (T) this;
    }
}
