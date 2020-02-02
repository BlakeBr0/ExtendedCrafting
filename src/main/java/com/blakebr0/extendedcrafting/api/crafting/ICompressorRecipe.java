package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

/**
 * Used to represent a Compressor recipe for the recipe type
 */
@SuppressWarnings("unchecked")
public interface ICompressorRecipe extends IRecipe<IInventory> {
    default <T extends ICompressorRecipe> T cast() {
        return (T) this;
    }
}
