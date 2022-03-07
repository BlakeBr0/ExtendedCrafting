package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.crafting.condition.UltimateSingularityRecipeCondition;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedTableRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessTableRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.UltimateSingularityRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public final class ModRecipeSerializers {
    public static final RecipeSerializer<CombinationRecipe> COMBINATION = new CombinationRecipe.Serializer();
    public static final RecipeSerializer<ShapedTableRecipe> SHAPED_TABLE = new ShapedTableRecipe.Serializer();
    public static final RecipeSerializer<ShapelessTableRecipe> SHAPELESS_TABLE = new ShapelessTableRecipe.Serializer();
    public static final RecipeSerializer<CompressorRecipe> COMPRESSOR = new CompressorRecipe.Serializer();
    public static final RecipeSerializer<ShapedEnderCrafterRecipe> SHAPED_ENDER_CRAFTER = new ShapedEnderCrafterRecipe.Serializer();
    public static final RecipeSerializer<ShapelessEnderCrafterRecipe> SHAPELESS_ENDER_CRAFTER = new ShapelessEnderCrafterRecipe.Serializer();
    public static final RecipeSerializer<UltimateSingularityRecipe> ULTIMATE_SINGULARITY = new UltimateSingularityRecipe.Serializer();

    @SubscribeEvent
    public void onRegisterSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        var registry = event.getRegistry();

        registry.register(COMBINATION.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "combination")));
        registry.register(SHAPED_TABLE.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "shaped_table")));
        registry.register(SHAPELESS_TABLE.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "shapeless_table")));
        registry.register(COMPRESSOR.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor")));
        registry.register(SHAPED_ENDER_CRAFTER.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "shaped_ender_crafter")));
        registry.register(SHAPELESS_ENDER_CRAFTER.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "shapeless_ender_crafter")));
        registry.register(ULTIMATE_SINGULARITY.setRegistryName(new ResourceLocation(ExtendedCrafting.MOD_ID, "ultimate_singularity")));

        CraftingHelper.register(UltimateSingularityRecipeCondition.Serializer.INSTANCE);

        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(ExtendedCrafting.MOD_ID, "combination"), RecipeTypes.COMBINATION);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(ExtendedCrafting.MOD_ID, "table"), RecipeTypes.TABLE);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor"), RecipeTypes.COMPRESSOR);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(ExtendedCrafting.MOD_ID, "ender_crafter"), RecipeTypes.ENDER_CRAFTER);
    }
}
