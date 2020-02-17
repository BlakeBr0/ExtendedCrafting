package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedTableRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessTableRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRecipeSerializers {
    public static final IRecipeSerializer<CombinationRecipe> COMBINATION = new CombinationRecipe.Serializer();
    public static final IRecipeSerializer<ShapedTableRecipe> SHAPED_TABLE = new ShapedTableRecipe.Serializer();
    public static final IRecipeSerializer<ShapelessTableRecipe> SHAPELESS_TABLE = new ShapelessTableRecipe.Serializer();
    public static final IRecipeSerializer<CompressorRecipe> COMPRESSOR = new CompressorRecipe.Serializer();
    public static final IRecipeSerializer<ShapedEnderCrafterRecipe> SHAPED_ENDER_CRAFTER = new ShapedEnderCrafterRecipe.Serializer();
    public static final IRecipeSerializer<ShapelessEnderCrafterRecipe> SHAPELESS_ENDER_CRAFTER = new ShapelessEnderCrafterRecipe.Serializer();

    @SubscribeEvent
    public void onRegisterSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

        registry.register(COMBINATION.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "combination")));
        registry.register(SHAPED_TABLE.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "shaped_table")));
        registry.register(SHAPELESS_TABLE.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "shapeless_table")));
        registry.register(COMPRESSOR.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor")));
        registry.register(SHAPED_ENDER_CRAFTER.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "shaped_ender_crafter")));
        registry.register(SHAPELESS_ENDER_CRAFTER.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "shapeless_ender_crafter")));
    }
}
