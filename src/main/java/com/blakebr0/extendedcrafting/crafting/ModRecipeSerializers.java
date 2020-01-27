package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.crafting.ISpecialRecipeSerializer;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRecipeSerializers {
    public static final ISpecialRecipeSerializer<CombinationRecipe> SPECIAL_COMBINATION = new CombinationRecipe.Serializer();
    public static final ISpecialRecipeSerializer<CompressorRecipe> SPECIAL_COMPRESSOR = new CompressorRecipe.Serializer();

    @SubscribeEvent
    public void onRegisterSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
        ExtendedRecipeManager manager = ExtendedRecipeManager.getInstance();

        manager.addSerializer(new ResourceLocation(ExtendedCrafting.MOD_ID, "combination"), SPECIAL_COMBINATION);
        manager.addSerializer(new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor"), SPECIAL_COMPRESSOR);
    }
}
