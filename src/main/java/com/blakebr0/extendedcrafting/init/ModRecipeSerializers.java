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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ExtendedCrafting.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> COMBINATION = register("combination", CombinationRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> SHAPED_TABLE = register("shaped_table", ShapedTableRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> SHAPELESS_TABLE = register("shapeless_table", ShapelessTableRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> COMPRESSOR = register("compressor", CompressorRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> SHAPED_ENDER_CRAFTER = register("shaped_ender_crafter", ShapedEnderCrafterRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> SHAPELESS_ENDER_CRAFTER = register("shapeless_ender_crafter", ShapelessEnderCrafterRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> SHAPED_FLUX_CRAFTER = register("shaped_flux_crafter", ShapedFluxCrafterRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> SHAPELESS_FLUX_CRAFTER = register("shapeless_flux_crafter", ShapelessFluxCrafterRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> ULTIMATE_SINGULARITY = register("ultimate_singularity", UltimateSingularityRecipe.Serializer::new);

    private static RegistryObject<RecipeSerializer<?>> register(String name, Supplier<RecipeSerializer<?>> serializer) {
        return REGISTRY.register(name, serializer);
    }
}
