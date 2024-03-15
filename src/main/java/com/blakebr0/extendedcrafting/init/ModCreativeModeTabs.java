package com.blakebr0.extendedcrafting.init;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.util.FeatureFlagDisplayItemGenerator;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModFeatureFlags;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExtendedCrafting.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = REGISTRY.register("creative_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.extendedcrafting"))
            .icon(() -> new ItemStack(ModItems.LUMINESSENCE.get()))
            .displayItems(FeatureFlagDisplayItemGenerator.create((parameters, output) -> {
                var stack = ItemStack.EMPTY;

                output.accept(ModBlocks.LUMINESSENCE_BLOCK);
                output.accept(ModBlocks.BLACK_IRON_BLOCK);
                output.accept(ModBlocks.REDSTONE_INGOT_BLOCK);
                output.accept(ModBlocks.ENHANCED_REDSTONE_INGOT_BLOCK);
                output.accept(ModBlocks.ENDER_INGOT_BLOCK);
                output.accept(ModBlocks.ENHANCED_ENDER_INGOT_BLOCK);
                output.accept(ModBlocks.CRYSTALTINE_BLOCK);
                output.accept(ModBlocks.THE_ULTIMATE_BLOCK);
                output.accept(ModBlocks.NETHER_STAR_BLOCK);
                output.accept(ModBlocks.FLUX_STAR_BLOCK);
                output.accept(ModBlocks.ENDER_STAR_BLOCK);

                output.accept(ModBlocks.FRAME);
                output.accept(ModBlocks.PEDESTAL, ModFeatureFlags.CRAFTING_CORE);
                output.accept(ModBlocks.CRAFTING_CORE, ModFeatureFlags.CRAFTING_CORE);
                output.accept(ModBlocks.BASIC_TABLE, ModFeatureFlags.TABLES);
                output.accept(ModBlocks.ADVANCED_TABLE, ModFeatureFlags.TABLES);
                output.accept(ModBlocks.ELITE_TABLE, ModFeatureFlags.TABLES);
                output.accept(ModBlocks.ULTIMATE_TABLE, ModFeatureFlags.TABLES);
                output.accept(ModBlocks.BASIC_AUTO_TABLE, ModFeatureFlags.TABLES, ModFeatureFlags.AUTO_TABLES);
                output.accept(ModBlocks.ADVANCED_AUTO_TABLE, ModFeatureFlags.TABLES, ModFeatureFlags.AUTO_TABLES);
                output.accept(ModBlocks.ELITE_AUTO_TABLE, ModFeatureFlags.TABLES, ModFeatureFlags.AUTO_TABLES);
                output.accept(ModBlocks.ULTIMATE_AUTO_TABLE, ModFeatureFlags.TABLES, ModFeatureFlags.AUTO_TABLES);
                output.accept(ModBlocks.COMPRESSOR, ModFeatureFlags.COMPRESSOR);
                output.accept(ModBlocks.ENDER_ALTERNATOR, ModFeatureFlags.ENDER_CRAFTER);
                output.accept(ModBlocks.ENDER_CRAFTER, ModFeatureFlags.ENDER_CRAFTER);
                output.accept(ModBlocks.AUTO_ENDER_CRAFTER, ModFeatureFlags.ENDER_CRAFTER, ModFeatureFlags.AUTO_ENDER_CRAFTER);
                output.accept(ModBlocks.FLUX_ALTERNATOR, ModFeatureFlags.FLUX_CRAFTER);
                output.accept(ModBlocks.FLUX_CRAFTER, ModFeatureFlags.FLUX_CRAFTER);
                output.accept(ModBlocks.AUTO_FLUX_CRAFTER, ModFeatureFlags.FLUX_CRAFTER, ModFeatureFlags.AUTO_FLUX_CRAFTER);

                output.accept(ModItems.LUMINESSENCE);
                output.accept(ModItems.BLACK_IRON_INGOT);
                output.accept(ModItems.REDSTONE_INGOT);
                output.accept(ModItems.ENHANCED_REDSTONE_INGOT);
                output.accept(ModItems.ENDER_INGOT);
                output.accept(ModItems.ENHANCED_ENDER_INGOT);
                output.accept(ModItems.CRYSTALTINE_INGOT);
                output.accept(ModItems.THE_ULTIMATE_INGOT);
                output.accept(ModItems.BLACK_IRON_NUGGET);
                output.accept(ModItems.REDSTONE_NUGGET);
                output.accept(ModItems.ENHANCED_REDSTONE_NUGGET);
                output.accept(ModItems.ENDER_NUGGET);
                output.accept(ModItems.ENHANCED_ENDER_NUGGET);
                output.accept(ModItems.CRYSTALTINE_NUGGET);
                output.accept(ModItems.THE_ULTIMATE_NUGGET);
                output.accept(ModItems.BLACK_IRON_SLATE);
                output.accept(ModItems.BASIC_CATALYST);
                output.accept(ModItems.ADVANCED_CATALYST);
                output.accept(ModItems.ELITE_CATALYST);
                output.accept(ModItems.ULTIMATE_CATALYST);
                output.accept(ModItems.REDSTONE_CATALYST);
                output.accept(ModItems.ENHANCED_REDSTONE_CATALYST);
                output.accept(ModItems.ENDER_CATALYST);
                output.accept(ModItems.ENHANCED_ENDER_CATALYST);
                output.accept(ModItems.CRYSTALTINE_CATALYST);
                output.accept(ModItems.THE_ULTIMATE_CATALYST);
                output.accept(ModItems.BASIC_COMPONENT);
                output.accept(ModItems.ADVANCED_COMPONENT);
                output.accept(ModItems.ELITE_COMPONENT);
                output.accept(ModItems.ULTIMATE_COMPONENT);
                output.accept(ModItems.REDSTONE_COMPONENT);
                output.accept(ModItems.ENHANCED_REDSTONE_COMPONENT);
                output.accept(ModItems.ENDER_COMPONENT);
                output.accept(ModItems.ENHANCED_ENDER_COMPONENT);
                output.accept(ModItems.CRYSTALTINE_COMPONENT);
                output.accept(ModItems.THE_ULTIMATE_COMPONENT);
                output.accept(ModItems.FLUX_STAR);
                output.accept(ModItems.ENDER_STAR);
                output.accept(ModItems.HANDHELD_TABLE, ModFeatureFlags.HANDHELD_WORKBENCH);

                stack = new ItemStack(ModItems.RECIPE_MAKER.get());
                NBTHelper.setBoolean(stack, "Shapeless", false);
                NBTHelper.setString(stack, "Type", "Datapack");
                output.accept(stack, ModFeatureFlags.RECIPE_MAKER);

                stack = new ItemStack(ModItems.RECIPE_MAKER.get());
                NBTHelper.setBoolean(stack, "Shapeless", false);
                NBTHelper.setString(stack, "Type", "CraftTweaker");
                output.accept(stack, ModFeatureFlags.RECIPE_MAKER);

                for (var singularity : SingularityRegistry.getInstance().getSingularities()) {
                    if (singularity.isEnabled()) {
                        output.accept(SingularityUtils.getItemForSingularity(singularity), ModFeatureFlags.SINGULARITIES);
                    }
                }

                output.accept(ModItems.ULTIMATE_SINGULARITY, ModFeatureFlags.SINGULARITIES);
            }))
            .build());
}
