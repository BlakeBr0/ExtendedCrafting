package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.init.ModItems;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class UltimateSingularityRecipe extends ShapelessTableRecipe {
    private boolean ingredientsLoaded = false;

    public UltimateSingularityRecipe(ResourceLocation recipeId, ItemStack output) {
        super(recipeId, NonNullList.create(), output, 4);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        if (!this.ingredientsLoaded) {
            SingularityRegistry.getInstance().getSingularities()
                    .stream()
                    .filter(singularity -> singularity.isInUltimateSingularity() && singularity.getIngredient() != Ingredient.EMPTY)
                    .limit(81)
                    .map(SingularityUtils::getItemForSingularity)
                    .map(Ingredient::of)
                    .forEach(super.getIngredients()::add);

            this.ingredientsLoaded = true;
        }

        return super.getIngredients();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ULTIMATE_SINGULARITY;
    }

    public static class Serializer implements RecipeSerializer<UltimateSingularityRecipe> {
        @Override
        public UltimateSingularityRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new UltimateSingularityRecipe(recipeId, new ItemStack(ModItems.ULTIMATE_SINGULARITY.get()));
        }

        @Override
        public UltimateSingularityRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return new UltimateSingularityRecipe(recipeId, new ItemStack(ModItems.ULTIMATE_SINGULARITY.get()));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, UltimateSingularityRecipe recipe) { }
    }
}
