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
	public static final ForgeConfigSpec.BooleanValue ENABLE_AUTO_ENDER_CRAFTER;
	public static final ForgeConfigSpec.IntValue ENDER_CRAFTER_TIME_REQUIRED;
	public static final ForgeConfigSpec.DoubleValue ENDER_CRAFTER_ALTERNATOR_EFFECTIVENESS;
	public static final ForgeConfigSpec.IntValue AUTO_ENDER_CRAFTER_POWER_CAPACITY;
	public static final ForgeConfigSpec.IntValue AUTO_ENDER_CRAFTER_INSERT_POWER_RATE;

	public static final ForgeConfigSpec.BooleanValue ENABLE_FLUX_CRAFTER;
	public static final ForgeConfigSpec.BooleanValue ENABLE_AUTO_FLUX_CRAFTER;
	public static final ForgeConfigSpec.IntValue FLUX_CRAFTER_POWER_RATE;
	public static final ForgeConfigSpec.IntValue FLUX_ALTERNATOR_POWER_CAPACITY;
	public static final ForgeConfigSpec.IntValue AUTO_FLUX_CRAFTER_POWER_CAPACITY;
	public static final ForgeConfigSpec.IntValue AUTO_FLUX_CRAFTER_INSERT_POWER_RATE;

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
				.define("handheldWorkbench", true);
		common.pop();

		common.comment("Settings for the Crafting Core.").push("Combination Crafting");
		ENABLE_CRAFTING_CORE = common
				.comment("Should the Crafting Core be enabled?")
				.define("enabled", true);
		CRAFTING_CORE_POWER_CAPACITY = common
				.comment("How much FE the Crafting Core should hold.")
				.defineInRange("powerCapacity", 5000000, 0, Integer.MAX_VALUE);
		CRAFTING_CORE_POWER_RATE = common
				.comment("How much FE/t the Crafting Core should use when crafting by default.")
				.defineInRange("powerRate", 500, 0, Integer.MAX_VALUE);
		common.pop();

		common.comment("Settings for the Extended Crafting Tables.").push("Table Crafting");
		ENABLE_TABLES = common
				.comment("Should the Extended Crafting Tables be enabled?")
				.define("enabled", true);
		ENABLE_AUTO_TABLES = common
				.comment("Should the Auto Crafting Tables be enabled?")
				.define("autoTablesEnabled", true);
		TABLE_USE_VANILLA_RECIPES = common
				.comment("Should the Basic Crafting Table inherit vanilla crafting recipes?")
				.define("useVanillaRecipes", true);
		AUTO_TABLE_POWER_CAPACITY = common
				.comment("How much FE the Auto Crafting Tables should hold. Higher tiers use double the previous tier.")
				.defineInRange("autoTablePowerCapacity", 500000, 0, Integer.MAX_VALUE);
		AUTO_TABLE_POWER_RATE = common
				.comment("How much FE the Auto Crafting Tables should use when crafting.")
				.defineInRange("autoTablePowerRate", 500, 0, Integer.MAX_VALUE);
		AUTO_TABLE_INSERT_POWER_RATE = common
				.comment("How much FE the Auto Crafting Tables should use when auto inserting items.")
				.defineInRange("autoTableInsertPowerRate", 100, 0, Integer.MAX_VALUE);
		common.pop();

		common.comment("Settings for the Quantum Compressor.").push("Quantum Compression");
		ENABLE_COMPRESSOR = common
				.comment("Should the Quantum Compressor be enabled?")
				.define("enabled", true);
		COMPRESSOR_POWER_CAPACITY = common
				.comment("How much FE the Quantum Compressor should hold.")
				.defineInRange("powerCapacity", 10000000, 0, Integer.MAX_VALUE);
		COMPRESSOR_POWER_RATE = common
				.comment("How much FE/t the Quantum Compressor should use when crafting by default.")
				.defineInRange("powerRate", 5000, 0, Integer.MAX_VALUE);
		common.pop();

		common.comment("Settings for the Ender Crafter.").push("Ender Crafting");
		ENABLE_ENDER_CRAFTER = common
				.comment("Should the Ender Crafter be enabled?")
				.define("enabled", true);
		ENABLE_AUTO_ENDER_CRAFTER = common
				.comment("Should the Auto Ender Crafter be enabled?")
				.define("autoCrafterEnabled", true);
		ENDER_CRAFTER_TIME_REQUIRED = common
				.comment("How long a single Ender Crafter crafting operation should take (in seconds) by default.")
				.defineInRange("defaultTimeRequired", 60, 1, Integer.MAX_VALUE);
		ENDER_CRAFTER_ALTERNATOR_EFFECTIVENESS = common
				.comment("How much a single Ender Alternator should speed up a craft. This is a percentage of the time required.")
				.defineInRange("alternatorEffectiveness", 0.01, 0, 1);
		AUTO_ENDER_CRAFTER_POWER_CAPACITY = common
				.comment("How much FE the Auto Ender Crafter should hold.")
				.defineInRange("autoCrafterPowerCapacity", 500000, 0, Integer.MAX_VALUE);
		AUTO_ENDER_CRAFTER_INSERT_POWER_RATE = common
				.comment("How much FE the Auto Ender Crafter should use when auto inserting items.")
				.defineInRange("autoCrafterInsertPowerRate", 100, 0, Integer.MAX_VALUE);
		common.pop();

		common.comment("Settings for the Flux Crafter.").push("Flux Crafting");
		ENABLE_FLUX_CRAFTER = common
				.comment("Should the Flux Crafter be enabled?")
                .define("enabled", true);
		ENABLE_AUTO_FLUX_CRAFTER = common
				.comment("Should the Auto Flux Crafter be enabled?")
				.define("autoCrafterEnabled", true);
		FLUX_CRAFTER_POWER_RATE = common
				.comment("How much FE/t the Flux Crafter should pull from each Flux Alternator when crafting by default.")
				.defineInRange("defaultPowerRate", 400, 0, Integer.MAX_VALUE);
		FLUX_ALTERNATOR_POWER_CAPACITY = common
				.comment("How much FE the Flux Alternator should hold.")
                .defineInRange("powerCapacity", 80000, 0, Integer.MAX_VALUE);
		AUTO_FLUX_CRAFTER_POWER_CAPACITY = common
				.comment("How much FE the Auto Flux Crafter should hold.")
				.defineInRange("autoCrafterPowerCapacity", 500000, 0, Integer.MAX_VALUE);
		AUTO_FLUX_CRAFTER_INSERT_POWER_RATE = common
				.comment("How much FE the Auto Flux Crafter should use when auto inserting items.")
				.defineInRange("autoCrafterInsertPowerRate", 100, 0, Integer.MAX_VALUE);
		common.pop();

		common.comment("Settings for Singularities.").push("Singularities");
		ENABLE_SINGULARITIES = common
				.comment("Should the Singularities be enabled?")
				.define("enabled", true);
		SINGULARITY_MATERIALS_REQUIRED = common
				.comment("The default amount of items required to create a Singularity.")
				.defineInRange("defaultMaterialsRequired", 10000, 1, Integer.MAX_VALUE);
		SINGULARITY_POWER_REQUIRED = common
				.comment("The default amount of FE required to create a Singularity.")
				.defineInRange("defaultPowerRequired", 5000000, 0, Integer.MAX_VALUE);
		SINGULARITY_DEFAULT_RECIPES = common
				.comment("Should default recipes be generated for Singularities?")
				.define("defaultRecipes", true);
		SINGULARITY_DEFAULT_CATALYST = common
				.comment("The item to use as the Catalyst in default Singularity recipes.")
				.define("defaultCatalyst", "extendedcrafting:ultimate_catalyst");
		SINGULARITY_ULTIMATE_RECIPE = common
				.comment("Should the default recipe for the Ultimate Singularity be generated?")
				.define("ultimateSingularityRecipe", true);
		common.pop();

		common.comment("Settings for the Recipe Maker.").push("Recipe Maker");
		ENABLE_RECIPE_MAKER = common
				.comment("Should the Recipe Maker be enabled?")
				.define("enabled", true);
		RECIPE_MAKER_USE_TAGS = common
				.comment("Should the recipe maker try to use tags when possible?")
				.define("useTags", false);
		RECIPE_MAKER_USE_NBT = common
				.comment("Should the recipe maker add NBT tags when possible?")
				.define("useNBT", true);
		common.pop();

		COMMON = common.build();
	}
}
