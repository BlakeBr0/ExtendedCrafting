package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public final class DynamicRecipeManager implements IResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        SingularityRegistry.getInstance().getSingularities().forEach(singularity -> {
            CompressorRecipe compressorRecipe = this.makeSingularityRecipe(singularity);
            if (compressorRecipe != null)
                RecipeHelper.addRecipe(compressorRecipe);
        });
    }

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(this);
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
