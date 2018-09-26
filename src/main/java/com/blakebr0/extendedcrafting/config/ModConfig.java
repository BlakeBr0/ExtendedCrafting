package com.blakebr0.extendedcrafting.config;

import java.io.File;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.item.ModItems;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModConfig {

	public static Configuration config;
	public static ModConfig instance;
	
	public static boolean confGuideEnabled;
	public static boolean confHandheldTableEnabled;
	public static boolean confEnergyInWaila;

	public static boolean confCraftingCoreEnabled;
	public static int confCraftingCoreRFCapacity;
	public static int confCraftingCoreRFRate;
	
	public static boolean confInterfaceEnabled;
	public static int confInterfaceRFCapacity;
	public static int confInterfaceRFRate;
	public static boolean confInterfaceRenderer;
	
	public static boolean confTableEnabled;
	public static boolean confTableUseRecipes;
	
	public static boolean confCompressorEnabled;
	public static int confCompressorRFCapacity;
	public static int confCompressorRFRate;
	public static boolean confCompressorRenderer;
	
	public static boolean confEnderEnabled;
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

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(ExtendedCrafting.MOD_ID)) {
			ModConfig.init();
		}
	}

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
		init();
	}

	public static void init() {
		updateConfig();
		
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
	
	private static void updateConfig() {
		if (config.hasCategory("settings")) {
			updateProperty("crafting_core_rf_capacity", "energy_capacity", "settings", "combination_crafting");
			updateProperty("crafting_core_rf_rate", "energy_rate", "settings", "combination_crafting");
			updateProperty("compressor_rf_capacity", "energy_capacity", "settings", "quantum_compression");
			updateProperty("compressor_rf_rate", "energy_rate", "settings", "quantum_compression");
			updateProperty("interface_rf_capacity", "energy_capacity", "settings", "automation_interface");
			updateProperty("interface_rf_rate", "energy_rate", "settings", "automation_interface");
			
			ConfigCategory settings = config.getCategory("settings");
			settings.remove("compressor_item_rate");
			config.removeCategory(settings);
			
			config.renameProperty("recipe_maker", "recipe_maker_oredict", "use_oredictionary");
			config.renameProperty("recipe_maker", "recipe_maker_nbt", "use_nbt");
			
			config.renameProperty("singularity", "_singularity_amount", "material_amount");
			config.renameProperty("singularity", "_singularity_rf", "energy_cost");
			config.renameProperty("singularity", "_singularity_catalyst", "default_catalyst");
			config.renameProperty("singularity", "_singularity_recipes", "default_recipes");
			config.renameProperty("singularity", "_ultimate_singularity_recipe", "ultimate_singularity_recipe");
			config.renameProperty("singularity", "_custom_singularities", "custom_singularities");
			config.renameProperty("singularity", "_ultimate_blacklist", "ultimate_singularity_recipe_blacklist");
		}
	}
	
	private static void updateProperty(String oldName, String newName, String oldCategory, String newCategory) {
		config.moveProperty(oldCategory, oldName, newCategory);
		config.renameProperty(newCategory, oldName, newName);
	}
	
	public static boolean removeSingularity(String name) {
		boolean value = config.get("singularity", name, true).getBoolean();
		config.getCategory("singularity").remove(name);
		
		return value;
	}
}
