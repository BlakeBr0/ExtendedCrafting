package com.blakebr0.extendedcrafting.client.handler;

import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ClientRecipeHandler {
    public static final List<RecipeHolder<ICombinationRecipe>> COMBINATION_RECIPES = new ArrayList<>();
    public static final List<RecipeHolder<ICompressorRecipe>> COMPRESSOR_RECIPES = new ArrayList<>();
    public static final List<RecipeHolder<IEnderCrafterRecipe>> ENDER_CRAFTER_RECIPES = new ArrayList<>();
    public static final List<RecipeHolder<IFluxCrafterRecipe>> FLUX_CRAFTER_RECIPES = new ArrayList<>();
    public static final List<RecipeHolder<ITableRecipe>> TABLE_RECIPES = new ArrayList<>();

    public static final Map<Identifier, ICombinationRecipe> COMBINATION_RECIPE_MAP = new LinkedHashMap<>();
    public static final Map<Identifier, ICompressorRecipe> COMPRESSOR_RECIPE_MAP = new LinkedHashMap<>();
    public static final Map<Identifier, IEnderCrafterRecipe> ENDER_CRAFTER_RECIPE_MAP = new LinkedHashMap<>();
    public static final Map<Identifier, IFluxCrafterRecipe> FLUX_CRAFTER_RECIPE_MAP = new LinkedHashMap<>();

    @SubscribeEvent
    public void onRecipesReceived(RecipesReceivedEvent event) {
        var recipes = event.getRecipeMap();

        COMBINATION_RECIPES.addAll(recipes.byType(ModRecipeTypes.COMBINATION.get()));
        COMPRESSOR_RECIPES.addAll(recipes.byType(ModRecipeTypes.COMPRESSOR.get()));
        ENDER_CRAFTER_RECIPES.addAll(recipes.byType(ModRecipeTypes.ENDER_CRAFTER.get()));
        FLUX_CRAFTER_RECIPES.addAll(recipes.byType(ModRecipeTypes.FLUX_CRAFTER.get()));
        TABLE_RECIPES.addAll(recipes.byType(ModRecipeTypes.TABLE.get()));

        map(COMBINATION_RECIPES, COMBINATION_RECIPE_MAP);
        map(COMPRESSOR_RECIPES, COMPRESSOR_RECIPE_MAP);
        map(ENDER_CRAFTER_RECIPES, ENDER_CRAFTER_RECIPE_MAP);
        map(FLUX_CRAFTER_RECIPES, FLUX_CRAFTER_RECIPE_MAP);
    }

    @SubscribeEvent
    public void onClientPlayerLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
        COMBINATION_RECIPES.clear();
        COMPRESSOR_RECIPES.clear();
        ENDER_CRAFTER_RECIPES.clear();
        FLUX_CRAFTER_RECIPES.clear();
        TABLE_RECIPES.clear();

        COMBINATION_RECIPE_MAP.clear();
        COMPRESSOR_RECIPE_MAP.clear();
        ENDER_CRAFTER_RECIPE_MAP.clear();
        FLUX_CRAFTER_RECIPE_MAP.clear();
    }

    private static <T extends Recipe<?>> void map(List<RecipeHolder<T>> list, Map<Identifier, T> map) {
        for (var recipe : list) {
            map.put(recipe.id().identifier(), recipe.value());
        }
    }
}
