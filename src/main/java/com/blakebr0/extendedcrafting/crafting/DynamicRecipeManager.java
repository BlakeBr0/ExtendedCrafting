package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.event.RecipeManagerLoadingEvent;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

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
}
