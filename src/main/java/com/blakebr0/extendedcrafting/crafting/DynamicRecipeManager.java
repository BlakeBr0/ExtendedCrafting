package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public final class DynamicRecipeManager implements IResourceManagerReloadListener {
    private static RecipeManager recipeManager;

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        Map<ResourceLocation, IRecipe<?>> recipes = getRecipeManager().recipes.computeIfAbsent(RecipeTypes.COMPRESSOR, t -> new HashMap<>());
        SingularityRegistry.getInstance().getSingularities().forEach(singularity -> {
            CompressorRecipe compressorRecipe = this.makeSingularityRecipe(singularity);
            if (compressorRecipe != null)
                recipes.put(compressorRecipe.getId(), compressorRecipe);
        });
    }

    public static RecipeManager getRecipeManager() {
        if (recipeManager == null) {
            RecipeManager recipeManager = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
            recipeManager.recipes = new HashMap<>(recipeManager.recipes);
            recipeManager.recipes.replaceAll((t, v) -> new HashMap<>(recipeManager.recipes.get(t)));
            DynamicRecipeManager.recipeManager = recipeManager;
        }

        return recipeManager;
    }

    private CompressorRecipe makeSingularityRecipe(Singularity singularity) {
        if (!ModConfigs.SINGULARITY_DEFAULT_RECIPES.get())
            return null;

        Ingredient ingredient = singularity.getIngredient();
        if (ingredient == Ingredient.EMPTY)
            return null;

        ResourceLocation id = singularity.getId();
        ResourceLocation recipeId = new ResourceLocation(ExtendedCrafting.MOD_ID, id.getPath() + "_singularity");
        ItemStack output = SingularityUtils.getItemForSingularity(singularity);
        int ingredientCount = singularity.getIngredientCount();
        String catalystId = ModConfigs.SINGULARITY_DEFAULT_CATALYST.get();
        Item catalystItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(catalystId));
        Ingredient catalyst = Ingredient.fromItems(catalystItem);
        Integer powerRequired = ModConfigs.SINGULARITY_POWER_REQUIRED.get();

        return new CompressorRecipe(recipeId, ingredient, output, ingredientCount, catalyst, powerRequired);
    }
}
