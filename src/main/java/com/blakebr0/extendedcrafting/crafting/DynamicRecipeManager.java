package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.UltimateSingularityRecipe;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public final class DynamicRecipeManager {
    private static final DynamicRecipeManager INSTANCE = new DynamicRecipeManager();

    public void onResourceManagerReload(IResourceManager manager) {
        SingularityRegistry.getInstance().getSingularities().forEach(singularity -> {
            CompressorRecipe compressorRecipe = makeSingularityRecipe(singularity);
            if (compressorRecipe != null)
                RecipeHelper.addRecipe(compressorRecipe);
        });

        initUltimateSingularityRecipe();
    }

    public static DynamicRecipeManager getInstance() {
        return INSTANCE;
    }

    private static CompressorRecipe makeSingularityRecipe(Singularity singularity) {
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
        Ingredient catalyst = Ingredient.of(catalystItem);
        int powerRequired = ModConfigs.SINGULARITY_POWER_REQUIRED.get();

        return new CompressorRecipe(recipeId, ingredient, output, ingredientCount, catalyst, powerRequired);
    }

    private static void initUltimateSingularityRecipe() {
        if (!ModConfigs.SINGULARITY_ULTIMATE_RECIPE.get())
            return;

        UltimateSingularityRecipe.SINGULARITIES.clear();

        List<Singularity> singularities = SingularityRegistry.getInstance().getSingularities();
        int added = 0;
        for (int i = 0; i < singularities.size() && added < 81; i++) {
            Singularity singularity = singularities.get(i);
            if (singularity.getIngredient() != Ingredient.EMPTY && singularity.isInUltimateSingularity()) {
                ItemStack stack = SingularityUtils.getItemForSingularity(singularity);
                UltimateSingularityRecipe.SINGULARITIES.add(Ingredient.of(stack));

                added++;
            }
        }
    }
}
