package com.blakebr0.extendedcrafting.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ModConfigs {
	public static final ForgeConfigSpec CLIENT;
	public static final ForgeConfigSpec COMMON;

	public static final ForgeConfigSpec.BooleanValue ENABLE_COMPRESSOR_RENDERER;

	// Client
	static {
		final var client = new ForgeConfigSpec.Builder();

		client.comment("Client settings.").push("General");
		ENABLE_COMPRESSOR_RENDERER = client
				.comment("Should the Quantum Compressor render the result item above it?")
				.translation("configGui.extendedcrafting.enable_compressor_renderer")
				.define("enableCompressorRenderer", true);
		client.pop();

		CLIENT = client.build();
	}

	public static final ForgeConfigSpec.BooleanValue ENABLE_HANDHELD_WORKBENCH;

	public static final ForgeConfigSpec.BooleanValue ENABLE_CRAFTING_CORE;
	public static final ForgeConfigSpec.IntValue CRAFTING_CORE_POWER_CAPACITY;
	public static final ForgeConfigSpec.IntValue CRAFTING_CORE_POWER_RATE;
	
	public static final ForgeConfigSpec.BooleanValue ENABLE_TABLES;
	public static final ForgeConfigSpec.BooleanValue ENABLE_AUTO_TABLES;
	public static final ForgeConfigSpec.BooleanValue TABLE_USE_VANILLA_RECIPES;
	public static final ForgeConfigSpec.IntValue AUTO_TABLE_POWER_CAPACITY;
	public static final ForgeConfigSpec.IntValue AUTO_TABLE_POWER_RATE;
	public static final ForgeConfigSpec.IntValue AUTO_TABLE_INSERT_POWER_RATE;
	
	public static final ForgeConfigSpec.BooleanValue ENABLE_COMPRESSOR;
	public static final ForgeConfigSpec.IntValue COMPRESSOR_POWER_CAPACITY;
	public static final ForgeConfigSpec.IntValue COMPRESSOR_POWER_RATE;

	public static final ForgeConfigSpec.BooleanValue ENABLE_ENDER_CRAFTER;
	public static final ForgeConfigSpec.IntValue ENDER_CRAFTER_TIME_REQUIRED;
	public static final ForgeConfigSpec.DoubleValue ENDER_CRAFTER_ALTERNATOR_EFFECTIVENESS;

	public static final ForgeConfigSpec.BooleanValue ENABLE_SINGULARITIES;
	public static final ForgeConfigSpec.IntValue SINGULARITY_MATERIALS_REQUIRED;
	public static final ForgeConfigSpec.IntValue SINGULARITY_POWER_REQUIRED;
	public static final ForgeConfigSpec.BooleanValue SINGULARITY_DEFAULT_RECIPES;
	public static final ForgeConfigSpec.ConfigValue<String> SINGULARITY_DEFAULT_CATALYST;
	public static final ForgeConfigSpec.BooleanValue SINGULARITY_ULTIMATE_RECIPE;

	public static final ForgeConfigSpec.BooleanValue ENABLE_RECIPE_MAKER;
	public static final ForgeConfigSpec.BooleanValue RECIPE_MAKER_USE_TAGS;
	public static final ForgeConfigSpec.BooleanValue RECIPE_MAKER_USE_NBT;

	// Common
	static {
		final var common = new ForgeConfigSpec.Builder();

		common.comment("Settings for general things.").push("General");
		ENABLE_HANDHELD_WORKBENCH = common
				.comment("Should the Handheld Crafting Table be enabled?")
				.translation("configGui.extendedcrafting.enable_handheld_workbench")
				.define("handheldWorkbench", true);
		common.pop();

		common.comment("Settings for the Crafting Core.").push("Combination Crafting");
		ENABLE_CRAFTING_CORE = common
				.comment("Should the Crafting Core be enabled?")
				.translation("configGui.extendedcrafting.enable_crafting_core")
				.define("enabled", true);
		CRAFTING_CORE_POWER_CAPACITY = common
				.comment("How much FE the Crafting Core should hold.")
				.translation("configGui.extendedcrafting.crafting_core_power_capacity")
				.defineInRange("powerCapacity", 5000000, 0, Integer.MAX_VALUE);
		CRAFTING_CORE_POWER_RATE = common
				.comment("How much FE/t the Crafting Core should use when crafting by default.")
				.translation("configGui.extendedcrafting.crafting_core_power_rate")
				.defineInRange("powerRate", 500, 0, Integer.MAX_VALUE);
		common.pop();

		common.comment("Settings for the Extended Crafting Tables.").push("Table Crafting");
		ENABLE_TABLES = common
				.comment("Should the Extended Crafting Tables be enabled?")
				.translation("configGui.extendedcrafting.enable_tables")
				.define("enabled", true);
		ENABLE_AUTO_TABLES = common
				.comment("Should the Auto Crafting Tables be enabled?")
				.translation("configGui.extendedcrafting.enable_auto_tables")
				.define("autoTablesEnabled", true);
		TABLE_USE_VANILLA_RECIPES = common
				.comment("Should the Basic Crafting Table inherit vanilla crafting recipes?")
				.translation("configGui.extendedcrafting.table_use_vanilla_recipes")
				.define("useVanillaRecipes", true);
		AUTO_TABLE_POWER_CAPACITY = common
				.comment("How much FE the Auto Crafting Tables should hold. Higher tiers use double the previous tier.")
				.translation("configGui.extendedcrafting.auto_table_power_capacity")
				.defineInRange("autoTablePowerCapacity", 500000, 0, Integer.MAX_VALUE);
		AUTO_TABLE_POWER_RATE = common
				.comment("How much FE the Auto Crafting Tables should use when crafting.")
				.translation("configGui.extendedcrafting.auto_table_power_rate")
				.defineInRange("autoTablePowerRate", 500, 0, Integer.MAX_VALUE);
		AUTO_TABLE_INSERT_POWER_RATE = common
				.comment("How much FE the Auto Crafting Tables should use when auto inserting items.")
				.translation("configGui.extendedcrafting.auto_table_insert_power_rate")
				.defineInRange("autoTableInsertPowerRate", 100, 0, Integer.MAX_VALUE);
		common.pop();

		common.comment("Settings for the Quantum Compressor.").push("Quantum Compression");
		ENABLE_COMPRESSOR = common
				.comment("Should the Quantum Compressor be enabled?")
				.translation("configGui.extendedcrafting.enable_compressor")
				.define("enabled", true);
		COMPRESSOR_POWER_CAPACITY = common
				.comment("How much FE the Quantum Compressor should hold.")
				.translation("configGui.extendedcrafting.compressor_power_capacity")
				.defineInRange("powerCapacity", 10000000, 0, Integer.MAX_VALUE);
		COMPRESSOR_POWER_RATE = common
				.comment("How much FE/t the Quantum Compressor should use when crafting by default.")
				.translation("configGui.extendedcrafting.compressor_power_rate")
				.defineInRange("powerRate", 5000, 0, Integer.MAX_VALUE);
		common.pop();

		common.comment("Settings for the Ender Crafter.").push("Ender Crafting");
		ENABLE_ENDER_CRAFTER = common
				.comment("Should the Ender Crafter be enabled?")
				.translation("configGui.extendedcrafting.enable_ender_crafter")
				.define("enabled", true);
		ENDER_CRAFTER_TIME_REQUIRED = common
				.comment("How long a single Ender Crafter crafting operation should take (in seconds) by default.")
				.translation("configGui.extendedcrafting.ender_crafter_time_required")
				.defineInRange("defaultTimeRequired", 60, 1, Integer.MAX_VALUE);
		ENDER_CRAFTER_ALTERNATOR_EFFECTIVENESS = common
				.comment("How much a single Ender Alternator should speed up a craft. This is a percentage of the time required.")
				.translation("configGui.extendedcrafting.ender_crafter_alternator_effectiveness")
				.defineInRange("alternatorEffectiveness", 0.01, 0, 1);
		common.pop();

		common.comment("Settings for Singularities.").push("Singularities");
		ENABLE_SINGULARITIES = common
				.comment("Should the Singularities be enabled?")
				.translation("configGui.extendedcrafting.enable_singularities")
				.define("enabled", true);
		SINGULARITY_MATERIALS_REQUIRED = common
				.comment("The default amount of items required to create a Singularity.")
				.translation("configGui.extendedcrafting.singularity_materials_required")
				.defineInRange("defaultMaterialsRequired", 10000, 1, Integer.MAX_VALUE);
		SINGULARITY_POWER_REQUIRED = common
				.comment("The default amount of FE required to create a Singularity.")
				.translation("configGui.extendedcrafting.singularity_power_required")
				.defineInRange("defaultPowerRequired", 5000000, 0, Integer.MAX_VALUE);
		SINGULARITY_DEFAULT_RECIPES = common
				.comment("Should default recipes be generated for Singularities?")
				.translation("configGui.extendedcrafting.singularity_default_recipes")
				.define("defaultRecipes", true);
		SINGULARITY_DEFAULT_CATALYST = common
				.comment("The item to use as the Catalyst in default Singularity recipes.")
				.translation("configGui.extendedcrafting.singularity_default_catalyst")
				.define("defaultCatalyst", "extendedcrafting:ultimate_catalyst");
		SINGULARITY_ULTIMATE_RECIPE = common
				.comment("Should the default recipe for the Ultimate Singularity be generated?")
				.translation("configGui.extendedcrafting.singularity_ultimate_recipe")
				.define("ultimateSingularityRecipe", true);
		common.pop();

		common.comment("Settings for the Recipe Maker.").push("Recipe Maker");
		ENABLE_RECIPE_MAKER = common
				.comment("Should the Recipe Maker be enabled?")
				.translation("configGui.extendedcrafting.enable_recipe_maker")
				.define("enabled", true);
		RECIPE_MAKER_USE_TAGS = common
				.comment("Should the recipe maker try to use tags when possible?")
				.translation("configGui.extendedcrafting.recipe_maker_use_tags")
				.define("useTags", false);
		RECIPE_MAKER_USE_NBT = common
				.comment("Should the recipe maker add NBT tags when possible?")
				.translation("configGui.extendedcrafting.recipe_maker_use_nbt")
				.define("useNBT", true);
		common.pop();

		COMMON = common.build();
	}
}
