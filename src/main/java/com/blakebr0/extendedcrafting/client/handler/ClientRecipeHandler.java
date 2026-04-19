package com.blakebr0.extendedcrafting.client.handler;

import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public final class ClientRecipeHandler {
    public static final List<RecipeHolder<ICombinationRecipe>> COMBINATION_RECIPES = new ArrayList<>();
    public static final List<RecipeHolder<ICompressorRecipe>> COMPRESSOR_RECIPES = new ArrayList<>();
    public static final List<RecipeHolder<IEnderCrafterRecipe>> ENDER_CRAFTER_RECIPES = new ArrayList<>();
    public static final List<RecipeHolder<IFluxCrafterRecipe>> FLUX_CRAFTER_RECIPES = new ArrayList<>();
    public static final List<RecipeHolder<ITableRecipe>> TABLE_RECIPES = new ArrayList<>();

    @SubscribeEvent
    public void onRecipesReceived(RecipesReceivedEvent event) {
        var recipes = event.getRecipeMap();

        COMBINATION_RECIPES.addAll(recipes.byType(ModRecipeTypes.COMBINATION.get()));
        COMPRESSOR_RECIPES.addAll(recipes.byType(ModRecipeTypes.COMPRESSOR.get()));
        ENDER_CRAFTER_RECIPES.addAll(recipes.byType(ModRecipeTypes.ENDER_CRAFTER.get()));
        FLUX_CRAFTER_RECIPES.addAll(recipes.byType(ModRecipeTypes.FLUX_CRAFTER.get()));
        TABLE_RECIPES.addAll(recipes.byType(ModRecipeTypes.TABLE.get()));
    }

    @SubscribeEvent
    public void onClientPlayerLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
        COMBINATION_RECIPES.clear();
        COMPRESSOR_RECIPES.clear();
        ENDER_CRAFTER_RECIPES.clear();
        FLUX_CRAFTER_RECIPES.clear();
        TABLE_RECIPES.clear();
    }
}
