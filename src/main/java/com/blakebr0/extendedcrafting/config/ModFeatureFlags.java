package com.blakebr0.extendedcrafting.config;

import com.blakebr0.cucumber.util.FeatureFlag;
import com.blakebr0.cucumber.util.FeatureFlags;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import net.minecraft.resources.ResourceLocation;

@FeatureFlags
public final class ModFeatureFlags {
    public static final FeatureFlag AUTO_ENDER_CRAFTER = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "auto_ender_crafter"), ModConfigs.ENABLE_AUTO_ENDER_CRAFTER);
    public static final FeatureFlag AUTO_FLUX_CRAFTER = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "auto_flux_crafter"), ModConfigs.ENABLE_AUTO_FLUX_CRAFTER);
    public static final FeatureFlag AUTO_TABLES = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "auto_tables"), ModConfigs.ENABLE_AUTO_TABLES);
    public static final FeatureFlag COMPRESSOR = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor"), ModConfigs.ENABLE_COMPRESSOR);
    public static final FeatureFlag CRAFTING_CORE = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "crafting_core"), ModConfigs.ENABLE_CRAFTING_CORE);
    public static final FeatureFlag ENDER_CRAFTER = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "ender_crafter"), ModConfigs.ENABLE_ENDER_CRAFTER);
    public static final FeatureFlag FLUX_CRAFTER = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "flux_crafter"), ModConfigs.ENABLE_FLUX_CRAFTER);
    public static final FeatureFlag HANDHELD_WORKBENCH = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "handheld_workbench"), ModConfigs.ENABLE_HANDHELD_WORKBENCH);
    public static final FeatureFlag RECIPE_MAKER = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "recipe_maker"), ModConfigs.ENABLE_RECIPE_MAKER);
    public static final FeatureFlag RECIPE_MAKER_USE_NBT = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "recipe_maker_use_nbt"), ModConfigs.RECIPE_MAKER_USE_NBT);
    public static final FeatureFlag RECIPE_MAKER_USE_TAGS = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "recipe_maker_use_tags"), ModConfigs.RECIPE_MAKER_USE_TAGS);
    public static final FeatureFlag SINGULARITIES = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "singularities"), ModConfigs.ENABLE_SINGULARITIES);
    public static final FeatureFlag SINGULARITY_ULTIMATE_RECIPE = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "ultimate_singularity_recipe"), ModConfigs.SINGULARITY_ULTIMATE_RECIPE);
    public static final FeatureFlag TABLES = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "tables"), ModConfigs.ENABLE_TABLES);
    public static final FeatureFlag TABLE_USE_VANILLA_RECIPES = FeatureFlag.create(new ResourceLocation(ExtendedCrafting.MOD_ID, "table_use_vanilla_recipes"), ModConfigs.TABLE_USE_VANILLA_RECIPES);
}
