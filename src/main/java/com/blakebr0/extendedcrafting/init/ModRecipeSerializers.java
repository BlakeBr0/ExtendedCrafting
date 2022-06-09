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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public final class ModRecipeSerializers {
    public static final RecipeSerializer<CombinationRecipe> COMBINATION = new CombinationRecipe.Serializer();
    public static final RecipeSerializer<ShapedTableRecipe> SHAPED_TABLE = new ShapedTableRecipe.Serializer();
    public static final RecipeSerializer<ShapelessTableRecipe> SHAPELESS_TABLE = new ShapelessTableRecipe.Serializer();
    public static final RecipeSerializer<CompressorRecipe> COMPRESSOR = new CompressorRecipe.Serializer();
    public static final RecipeSerializer<ShapedEnderCrafterRecipe> SHAPED_ENDER_CRAFTER = new ShapedEnderCrafterRecipe.Serializer();
    public static final RecipeSerializer<ShapelessEnderCrafterRecipe> SHAPELESS_ENDER_CRAFTER = new ShapelessEnderCrafterRecipe.Serializer();
    public static final RecipeSerializer<UltimateSingularityRecipe> ULTIMATE_SINGULARITY = new UltimateSingularityRecipe.Serializer();

    @SubscribeEvent
    public void onRegisterSerializers(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, registry -> {
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "combination"), COMBINATION);
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "shaped_table"), SHAPED_TABLE);
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "shapeless_table"), SHAPELESS_TABLE);
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor"), COMPRESSOR);
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "shaped_ender_crafter"), SHAPED_ENDER_CRAFTER);
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "shapeless_ender_crafter"), SHAPELESS_ENDER_CRAFTER);
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "ultimate_singularity"), ULTIMATE_SINGULARITY);

            CraftingHelper.register(UltimateSingularityRecipeCondition.Serializer.INSTANCE);
        });

        event.register(ForgeRegistries.Keys.RECIPE_TYPES, registry -> {
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "combination"), RecipeTypes.COMBINATION);
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "table"), RecipeTypes.TABLE);
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor"), RecipeTypes.COMPRESSOR);
            registry.register(new ResourceLocation(ExtendedCrafting.MOD_ID, "ender_crafter"), RecipeTypes.ENDER_CRAFTER);
        });
    }
}
