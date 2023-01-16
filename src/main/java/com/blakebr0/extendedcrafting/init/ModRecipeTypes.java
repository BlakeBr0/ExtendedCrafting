package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ExtendedCrafting.MOD_ID);

    public static final RegistryObject<RecipeType<ICombinationRecipe>> COMBINATION = register("combination", () -> RecipeType.simple(new ResourceLocation(ExtendedCrafting.MOD_ID, "combination")));
    public static final RegistryObject<RecipeType<ITableRecipe>> TABLE = register("table", () -> RecipeType.simple(new ResourceLocation(ExtendedCrafting.MOD_ID, "table")));
    public static final RegistryObject<RecipeType<ICompressorRecipe>> COMPRESSOR = register("compressor", () -> RecipeType.simple(new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor")));
    public static final RegistryObject<RecipeType<IEnderCrafterRecipe>> ENDER_CRAFTER = register("ender_crafter", () -> RecipeType.simple(new ResourceLocation(ExtendedCrafting.MOD_ID, "ender_crafter")));
    public static final RegistryObject<RecipeType<IFluxCrafterRecipe>> FLUX_CRAFTER = register("flux_crafter", () -> RecipeType.simple(new ResourceLocation(ExtendedCrafting.MOD_ID, "flux_crafter")));

    private static <T extends Recipe<Container>> RegistryObject<RecipeType<T>> register(String name, Supplier<RecipeType<T>> type) {
        return REGISTRY.register(name, type);
    }
}
