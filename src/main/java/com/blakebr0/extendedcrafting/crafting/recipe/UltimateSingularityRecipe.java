package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.crafting.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.item.ModItems;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class UltimateSingularityRecipe extends ShapelessTableRecipe {
    public static final NonNullList<Ingredient> SINGULARITIES = NonNullList.create();

    public UltimateSingularityRecipe(ResourceLocation recipeId, ItemStack output) {
        super(recipeId, SINGULARITIES, output, 4);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ULTIMATE_SINGULARITY;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<UltimateSingularityRecipe> {
        @Override
        public UltimateSingularityRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new UltimateSingularityRecipe(recipeId, new ItemStack(ModItems.ULTIMATE_SINGULARITY.get()));
        }

        @Override
        public UltimateSingularityRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new UltimateSingularityRecipe(recipeId, new ItemStack(ModItems.ULTIMATE_SINGULARITY.get()));
        }

        @Override
        public void write(PacketBuffer buffer, UltimateSingularityRecipe recipe) {

        }
    }
}
