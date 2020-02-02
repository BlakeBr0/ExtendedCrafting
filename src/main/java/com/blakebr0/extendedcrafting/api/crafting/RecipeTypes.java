package com.blakebr0.extendedcrafting.api.crafting;

import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;

import java.util.Optional;

public class RecipeTypes {
    public static final IRecipeType<CombinationRecipe> COMBINATION = new IRecipeType<CombinationRecipe>() {
        @Override
        public <C extends IInventory> Optional<CombinationRecipe> matches(IRecipe<C> recipe, World world, C inv) {
            return recipe.matches(inv, world) ? Optional.of((CombinationRecipe) recipe) : Optional.empty();
        }
    };
    public static final IRecipeType<CompressorRecipe> COMPRESSOR = new IRecipeType<CompressorRecipe>() {
        @Override
        public <C extends IInventory> Optional<CompressorRecipe> matches(IRecipe<C> recipe, World world, C inv) {
            return recipe.matches(inv, world) ? Optional.of((CompressorRecipe) recipe) : Optional.empty();
        }
    };
}
