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
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.stream.Collectors;

public class UltimateSingularityRecipe extends ShapelessTableRecipe {
    public static final NonNullList<Ingredient> SINGULARITIES = NonNullList.create();

    public UltimateSingularityRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output) {
        super(recipeId, inputs, output, 4);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ULTIMATE_SINGULARITY;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<UltimateSingularityRecipe> {
        @Override
        public UltimateSingularityRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new UltimateSingularityRecipe(recipeId, SINGULARITIES, new ItemStack(ModItems.ULTIMATE_SINGULARITY.get()));
        }

        @Override
        public UltimateSingularityRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            var singularities = SingularityRegistry.getInstance().getSingularities()
                    .stream()
                    .filter(singularity -> singularity.isInUltimateSingularity() && singularity.getIngredient() != Ingredient.EMPTY)
                    .limit(81)
                    .map(SingularityUtils::getItemForSingularity)
                    .map(Ingredient::of)
                    .collect(Collectors.toCollection(NonNullList::create));

            return new UltimateSingularityRecipe(recipeId, singularities, new ItemStack(ModItems.ULTIMATE_SINGULARITY.get()));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, UltimateSingularityRecipe recipe) { }
    }
}
