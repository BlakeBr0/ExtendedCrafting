package com.blakebr0.extendedcrafting.api.crafting;

import net.minecraft.world.item.crafting.RecipeType;

/**
 * The custom recipe types added by Extended Crafting
 */
public class RecipeTypes {
    public static final RecipeType<ICombinationRecipe> COMBINATION = new RecipeType<>() {
        @Override
        public String toString() {
            return "extendedcrafting:combination";
        }
    };
    public static final RecipeType<ITableRecipe> TABLE = new RecipeType<>() {
        @Override
        public String toString() {
            return "extendedcrafting:table";
        }
    };
    public static final RecipeType<ICompressorRecipe> COMPRESSOR = new RecipeType<>() {
        @Override
        public String toString() {
            return "extendedcrafting:compressor";
        }
    };
    public static final RecipeType<IEnderCrafterRecipe> ENDER_CRAFTER = new RecipeType<>() {
        @Override
        public String toString() {
            return "extendedcrafting:ender_crafter";
        }
    };
}
