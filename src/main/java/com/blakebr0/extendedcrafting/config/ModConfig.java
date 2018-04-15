package com.blakebr0.extendedcrafting.config;

import java.io.File;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.item.ModItems;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModConfig {

	public static Configuration config;
	public static ModConfig instance;

	public static int confCraftingCoreRFCapacity;
	public static int confCraftingCoreRFRate;
	public static int confCompressorRFCapacity;
	public static int confCompressorRFRate;
	public static int confCompressorItemRate;
	public static int confInterfaceRFCapacity;
	public static int confInterfaceRFRate;
	
	public static boolean confRMOredict;
	public static boolean confRMNBT;
	
	public static int confSingularityAmount;
	public static int confSingularityRF;
	public static boolean confSingularityRecipes;
	public static boolean confUltimateSingularityRecipe;
	public static String confSingularityCatalyst;

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.getModID().equals(ExtendedCrafting.MOD_ID)) {
			ModConfig.pre();
		}
	}

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
		pre();
	}

	public static void pre() {

		String category;

		category = "settings";
		config.setCategoryComment(category, "Settings for Extended Crafting content.");
		confCraftingCoreRFCapacity = config.getInt("crafting_core_rf_capacity", category, 5000000, 0, Integer.MAX_VALUE, "How much RF/FE the Crafting Core should hold.");
		confCraftingCoreRFRate = config.getInt("crafting_core_rf_rate", category, 500, 0, Integer.MAX_VALUE, "How much RF/t the Crafting Core should use when crafting by default.");
		confCompressorRFCapacity = config.getInt("compressor_rf_capacity", category, 10000000, 0, Integer.MAX_VALUE, "How much RF/FE the Quantum Compressor should hold.");
		confCompressorRFRate = config.getInt("compressor_rf_rate", category, 5000, 0, Integer.MAX_VALUE, "How much RF/t the Quantum Compressor should use when crafting by default.");
		confCompressorItemRate = config.getInt("compressor_item_rate", category, 4, 1, 64, "How many items/t the Quantum Compressor should consume/eject.");
		confInterfaceRFCapacity = config.getInt("interface_rf_capacity", category, 1000000, 0, Integer.MAX_VALUE, "How much RF/FE the Automation Interface should hold.");
		confInterfaceRFRate = config.getInt("interface_rf_rate", category, 80, 0, 100000, "How much RF the Automation Interface should use when moving items.");
		
		category = "recipe_maker";
		confRMOredict = config.getBoolean("recipe_maker_oredict", category, true, "Should the Recipe Maker use OreDictionary entries when applicable?");
		confRMNBT = config.getBoolean("recipe_maker_nbt", category, false, "Should the Recipe maker also copy the NBT of the ingredients?");
		
		category = "singularity";
		config.setCategoryComment(category, "High end crafting components.");
		confSingularityAmount = config.getInt("_singularity_amount", category, 10000, 1, Integer.MAX_VALUE, "The amount of materials required to create a Singularity, for the default recipes.");
		confSingularityRF = config.getInt("_singularity_rf", category, 5000000, 0, Integer.MAX_VALUE, "The amount of RF required to craft a Singularity, for the default recipes.");
		confSingularityCatalyst = config.getString("_singularity_catalyst", category, "extendedcrafting:material:11", "The catalyst required for the default Singularity recipes. modid:itemid:metadata");
		confSingularityRecipes = config.getBoolean("_singularity_recipes", category, true, "Should the default Singularity recipes be enabled?");
		confUltimateSingularityRecipe = config.getBoolean("_ultimate_singularity_recipe", category, true, "Should the default Ultimate Singularity recipe be enabled?");
		ModItems.itemSingularityCustom.configure(config);
		ModItems.itemSingularityUltimate.configure(config);

		if (config.hasChanged()) {
			config.save();
		}
	}
}
