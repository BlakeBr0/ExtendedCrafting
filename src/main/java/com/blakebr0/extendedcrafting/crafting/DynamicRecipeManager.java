package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.crafting.ingredient.IngredientWithCount;
import com.blakebr0.cucumber.event.RecipeManagerLoadingEvent;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;

public final class DynamicRecipeManager {
    private static final DynamicRecipeManager INSTANCE = new DynamicRecipeManager();

    @SubscribeEvent
    public void onRegisterRecipes(RecipeManagerLoadingEvent event) {
        SingularityRegistry.getInstance().loadSingularities();

        for (var singularity : SingularityRegistry.getInstance().getSingularities()) {
            var compressorRecipe = makeSingularityRecipe(singularity);

            if (compressorRecipe != null)
                event.addRecipe(compressorRecipe);
        }
    }

    public static DynamicRecipeManager getInstance() {
        return INSTANCE;
    }

    private static RecipeHolder<CompressorRecipe> makeSingularityRecipe(Singularity singularity) {
        if (!ModConfigs.SINGULARITY_DEFAULT_RECIPES.get())
            return null;

        var ingredient = singularity.getIngredient();
        if (ingredient == null)
            return null;

        var id = ExtendedCrafting.resource(singularity.getId().getPath() + "_singularity");
        var result = SingularityUtils.getItemForSingularity(singularity);
        int ingredientCount = singularity.getIngredientCount();
        var catalystItem = BuiltInRegistries.ITEM.get(Identifier.parse(ModConfigs.SINGULARITY_DEFAULT_CATALYST.get()));
        var catalyst = Ingredient.of(catalystItem);
        int powerRequired = ModConfigs.SINGULARITY_POWER_REQUIRED.get();

        return new RecipeHolder<>(id, new CompressorRecipe(NonNullList.of(IngredientWithCount.EMPTY, new IngredientWithCount(ingredient.getValues()[0], ingredientCount)), result, catalyst, powerRequired, ModConfigs.COMPRESSOR_POWER_RATE.get()));
    }
}
