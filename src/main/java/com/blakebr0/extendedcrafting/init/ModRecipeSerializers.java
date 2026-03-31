package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedTableRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessTableRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.UltimateSingularityRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ExtendedCrafting.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> COMBINATION = REGISTRY.register("combination", () -> CombinationRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SHAPED_TABLE = REGISTRY.register("shaped_table", () -> ShapedTableRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SHAPELESS_TABLE = REGISTRY.register("shapeless_table", () -> ShapelessTableRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> COMPRESSOR = REGISTRY.register("compressor", () -> CompressorRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SHAPED_ENDER_CRAFTER = REGISTRY.register("shaped_ender_crafter", () -> ShapedEnderCrafterRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SHAPELESS_ENDER_CRAFTER = REGISTRY.register("shapeless_ender_crafter", () -> ShapelessEnderCrafterRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SHAPED_FLUX_CRAFTER = REGISTRY.register("shaped_flux_crafter", () -> ShapedFluxCrafterRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SHAPELESS_FLUX_CRAFTER = REGISTRY.register("shapeless_flux_crafter", () -> ShapelessFluxCrafterRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> ULTIMATE_SINGULARITY = REGISTRY.register("ultimate_singularity", () -> UltimateSingularityRecipe.SERIALIZER);
}
