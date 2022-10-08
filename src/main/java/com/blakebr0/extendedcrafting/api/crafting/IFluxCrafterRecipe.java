package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public interface IFluxCrafterRecipe extends Recipe<Container> {
    int getPowerCost();
    int getPowerRate();
}
