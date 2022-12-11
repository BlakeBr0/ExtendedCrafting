package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Used to represent an Ender Crafting recipe for the recipe type
 */
public interface IEnderCrafterRecipe extends Recipe<Container> {
    int getCraftingTime();
}
