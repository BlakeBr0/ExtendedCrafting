package com.blakebr0.extendedcrafting.config;

import com.blakebr0.extendedcrafting.item.ModItems;
import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfigs {
	public static final ForgeConfigSpec COMMON;

	public static final ForgeConfigSpec.BooleanValue ENABLE_HANDHELD_WORKBENCH;
	
	public static boolean confGuideEnabled;
	public static boolean confHandheldTableEnabled;
	public static boolean confEnergyInWaila;

	public static final ForgeConfigSpec.BooleanValue ENABLE_CRAFTING_CORE;
	public static final ForgeConfigSpec.IntValue CRAFTING_CORE_POWER_CAPACITY;
	public static final ForgeConfigSpec.IntValue CRAFTING_CORE_POWER_RATE;
	
	public static boolean confInterfaceEnabled;
	public static int confInterfaceRFCapacity;
	public static int confInterfaceRFRate;
	public static boolean confInterfaceRenderer;
	
	public static final ForgeConfigSpec.BooleanValue ENABLE_TABLES;
	public static boolean confTableUseRecipes;
	
	public static final ForgeConfigSpec.BooleanValue ENABLE_COMPRESSOR;
	public static int confCompressorRFCapacity;
	public static int confCompressorRFRate;
	public static boolean confCompressorRenderer;
	
	public static final ForgeConfigSpec.BooleanValue ENABLE_ENDER_CRAFTER;
	public static int confEnderTimeRequired;
	public static float confEnderAlternatorEff;
	
	public static boolean confRMEnabled;
	public static boolean confRMOredict;
	public static boolean confRMNBT;
	
	public static boolean confSingularityEnabled;
	public static int confSingularityAmount;
	public static int confSingularityRF;
	public static boolean confSingularityRecipes;
	public static boolean confUltimateSingularityRecipe;
	public static String confSingularityCatalyst;

	// Common
	static {
		final ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();
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
		common.pop();

		common.comment("Settings for the Quantum Compressor.").push("Quantum Compression");
		ENABLE_COMPRESSOR = common
				.comment("Should the Quantum Compressor be enabled?")
				.translation("configGui.extendedcrafting.enable_compressor")
				.define("enabled", true);
		common.pop();

		common.comment("Settings for the Ender Crafter.").push("Ender Crafting");
		ENABLE_ENDER_CRAFTER = common
				.comment("Should the Ender Crafter be enabled?")
				.translation("configGui.extendedcrafting.enable_ender_crafter")
				.define("enabled", true);
		common.pop();

		COMMON = common.build();
	}

	public static void init() {
		
		String category;
		
		category = "general";
		config.setCategoryComment(category, "Settings for general things.");
		confGuideEnabled = config.getBoolean("guide_enabled", category, true, "Should the In-Game Guide Book be enabled?");
		confHandheldTableEnabled = config.getBoolean("handheld_table_enabled", category, true, "Should the Handheld Crafting Table be enabled?");
		confEnergyInWaila = config.getBoolean("energy_in_waila", category, true, "Should WAILA show the current energy of Extended Crafting machines?");
		
		category = "combination_crafting";
		config.setCategoryComment(category, "Settings for the Crafting Core.");
		confCraftingCoreEnabled = config.getBoolean("enabled", category, true, "Should the Crafting Core and Pedestal be enabled?");
		confCraftingCoreRFCapacity = config.getInt("energy_capacity", category, 5000000, 0, Integer.MAX_VALUE, "How much FE the Crafting Core should hold.");
		confCraftingCoreRFRate = config.getInt("energy_rate", category, 500, 0, Integer.MAX_VALUE, "How much FE/t the Crafting Core should use when crafting by default.");

		category = "automation_interface";
		config.setCategoryComment(category, "Settings for the Automation Interface.");
		confInterfaceEnabled = config.getBoolean("enabled", category, true, "Should the Automation Interface be enabled?");
		confInterfaceRFCapacity = config.getInt("energy_capacity", category, 1000000, 0, Integer.MAX_VALUE, "How much FE the Automation Interface should hold.");
		confInterfaceRFRate = config.getInt("energy_rate", category, 80, 0, 100000, "How much FE the Automation Interface should use when moving items.");
		confInterfaceRenderer = config.getBoolean("render_item", category, true, "Should the Automation Interface render the result item inside it?");
		
		category = "table_crafting";
		config.setCategoryComment(category, "Settings for the Extended Crafting Tables.");
		confTableEnabled = config.getBoolean("enabled", category, true, "Should the Extended Crafting Tables be enabled?");
		confTableUseRecipes = config.getBoolean("inherit_vanilla_recipes", category, true, "Should the Basic Crafting Table inherit normal crafting recipes?");
		
		category = "quantum_compression";
		config.setCategoryComment(category, "Settings for the Quantum Compressor.");
		confCompressorEnabled = config.getBoolean("enabled", category, true, "Should the Quantum Compressor be enabled?");
		confCompressorRFCapacity = config.getInt("energy_capacity", category, 10000000, 0, Integer.MAX_VALUE, "How much FE the Quantum Compressor should hold.");
		confCompressorRFRate = config.getInt("energy_rate", category, 5000, 0, Integer.MAX_VALUE, "How much FE/t the Quantum Compressor should use when crafting by default.");
		confCompressorRenderer = config.getBoolean("render_item", category, true, "Should the Quantum Compressor render the result item above it?");
		
		category = "ender_crafting";
		config.setCategoryComment(category, "Settings for the Ender Crafter.");
		confEnderEnabled = config.getBoolean("enabled", category, true, "Should the Ender Crafter and Ender Alternator be enabled?");
		confEnderTimeRequired = config.getInt("time_required", category, 60, 1, Integer.MAX_VALUE, "How many seconds each craft should take.");
		confEnderAlternatorEff = config.getFloat("alternator_effectiveness", category, 0.01F, 0, 1, "How much an Ender Alternator should speed up a craft. This is the percentage of time_required.");
		
		category = "recipe_maker";
		config.setCategoryComment(category, "Settings for the Recipe Maker.");
		confRMEnabled = config.getBoolean("enabled", category, true, "Should the Recipe Maker be enabled?");
		confRMOredict = config.getBoolean("use_oredictionary", category, true, "Should the Recipe Maker use OreDictionary entries when applicable?");
		confRMNBT = config.getBoolean("use_nbt", category, false, "Should the Recipe Maker also copy the NBT of the ingredients?");
		
		category = "singularity";
		config.setCategoryComment(category, "Settings for the Singularities.");
		confSingularityEnabled = config.getBoolean("enabled", category, true, "Should the Singularities be enabled?");
		confSingularityAmount = config.getInt("material_amount", category, 10000, 1, Integer.MAX_VALUE, "The amount of materials required to create a Singularity, for the default recipes.");
		confSingularityRF = config.getInt("energy_cost", category, 5000000, 0, Integer.MAX_VALUE, "The amount of RF required to craft a Singularity, for the default recipes.");
		confSingularityCatalyst = config.getString("default_catalyst", category, "extendedcrafting:material:11", "The catalyst required for the default Singularity recipes. modid:itemid:metadata");
		confSingularityRecipes = config.getBoolean("default_recipes", category, true, "Should the default Singularity recipes be enabled?");
		confUltimateSingularityRecipe = config.getBoolean("ultimate_singularity_recipe", category, true, "Should the default Ultimate Singularity recipe be enabled?");
		ModItems.itemSingularityCustom.configure(config);
		ModItems.itemSingularityUltimate.configure(config);

		if (config.hasChanged()) {
			config.save();
		}
	}
}
