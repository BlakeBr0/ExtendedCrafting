package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

public interface IEnderCrafterRecipe extends IRecipe<IInventory> {
    int getCraftingTime();
}
