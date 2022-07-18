package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.init.ModItems;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

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
    public boolean matches(IItemHandler inventory) {
        // ensure ingredients list is initialized
        NonNullList<Ingredient> ingredients = this.getIngredients();

        // in the case there are no ingredients, the recipe should never match
        return !ingredients.isEmpty() && super.matches(inventory);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ULTIMATE_SINGULARITY;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<UltimateSingularityRecipe> {
        @Override
        public UltimateSingularityRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new UltimateSingularityRecipe(recipeId, new ItemStack(ModItems.ULTIMATE_SINGULARITY.get()));
        }

        @Override
        public UltimateSingularityRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            return new UltimateSingularityRecipe(recipeId, new ItemStack(ModItems.ULTIMATE_SINGULARITY.get()));
        }

        @Override
        public void toNetwork(PacketBuffer buffer, UltimateSingularityRecipe recipe) { }
    }
}
