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
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.stream.Collectors;

public class UltimateSingularityRecipe extends ShapelessTableRecipe {
    public static final NonNullList<Ingredient> SINGULARITIES = NonNullList.create();

    public UltimateSingularityRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output) {
        super(recipeId, inputs, output, 4);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ULTIMATE_SINGULARITY;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<UltimateSingularityRecipe> {
        @Override
        public UltimateSingularityRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new UltimateSingularityRecipe(recipeId, SINGULARITIES, new ItemStack(ModItems.ULTIMATE_SINGULARITY.get()));
        }

        @Override
        public UltimateSingularityRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> singularities = SingularityRegistry.getInstance().getSingularities()
                    .stream()
                    .filter(singularity -> singularity.isInUltimateSingularity() && singularity.getIngredient() != Ingredient.EMPTY)
                    .limit(81)
                    .map(SingularityUtils::getItemForSingularity)
                    .map(Ingredient::of)
                    .collect(Collectors.toCollection(NonNullList::create));

            return new UltimateSingularityRecipe(recipeId, singularities, new ItemStack(ModItems.ULTIMATE_SINGULARITY.get()));
        }

        @Override
        public void toNetwork(PacketBuffer buffer, UltimateSingularityRecipe recipe) { }
    }
}
