package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_TYPE, ExtendedCrafting.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ICombinationRecipe>> COMBINATION = REGISTRY.register("combination", () -> RecipeType.simple(ExtendedCrafting.resource("combination")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<ITableRecipe>> TABLE = REGISTRY.register("table", () -> RecipeType.simple(ExtendedCrafting.resource("table")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<ICompressorRecipe>> COMPRESSOR = REGISTRY.register("compressor", () -> RecipeType.simple(ExtendedCrafting.resource("compressor")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<IEnderCrafterRecipe>> ENDER_CRAFTER = REGISTRY.register("ender_crafter", () -> RecipeType.simple(ExtendedCrafting.resource("ender_crafter")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<IFluxCrafterRecipe>> FLUX_CRAFTER = REGISTRY.register("flux_crafter", () -> RecipeType.simple(ExtendedCrafting.resource("flux_crafter")));

    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(
                COMBINATION.get(),
                COMPRESSOR.get(),
                ENDER_CRAFTER.get(),
                FLUX_CRAFTER.get(),
                TABLE.get()
        );
    }
}
