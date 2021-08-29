package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.UltimateSingularityRecipe;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public final class DynamicRecipeManager implements ResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        SingularityRegistry.getInstance().getSingularities().forEach(singularity -> {
            var compressorRecipe = makeSingularityRecipe(singularity);
            if (compressorRecipe != null)
                RecipeHelper.addRecipe(compressorRecipe);
        });

        initUltimateSingularityRecipe();
    }

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(this);
    }

    private static CompressorRecipe makeSingularityRecipe(Singularity singularity) {
        if (!ModConfigs.SINGULARITY_DEFAULT_RECIPES.get())
            return null;

        var ingredient = singularity.getIngredient();
        if (ingredient == Ingredient.EMPTY)
            return null;

        var id = singularity.getId();
        var recipeId = new ResourceLocation(ExtendedCrafting.MOD_ID, id.getPath() + "_singularity");
        var output = SingularityUtils.getItemForSingularity(singularity);
        int ingredientCount = singularity.getIngredientCount();
        var catalystId = ModConfigs.SINGULARITY_DEFAULT_CATALYST.get();
        var catalystItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(catalystId));
        var catalyst = Ingredient.of(catalystItem);
        int powerRequired = ModConfigs.SINGULARITY_POWER_REQUIRED.get();

        return new CompressorRecipe(recipeId, ingredient, output, ingredientCount, catalyst, powerRequired);
    }

    private static void initUltimateSingularityRecipe() {
        if (!ModConfigs.SINGULARITY_ULTIMATE_RECIPE.get())
            return;

        UltimateSingularityRecipe.SINGULARITIES.clear();

        var singularities = SingularityRegistry.getInstance().getSingularities();
        int added = 0;

        for (int i = 0; i < singularities.size() && added < 81; i++) {
            var singularity = singularities.get(i);

            if (singularity.getIngredient() != Ingredient.EMPTY && singularity.isInUltimateSingularity()) {
                var stack = SingularityUtils.getItemForSingularity(singularity);

                UltimateSingularityRecipe.SINGULARITIES.add(Ingredient.of(stack));

                added++;
            }
        }
    }
}
