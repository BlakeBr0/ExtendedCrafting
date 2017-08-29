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
	public static int confCompressorRFCapacity;
	public static int confCompressorRFRate;
	public static int confCompressorSpeed;
	
	public static int confSingularityAmount;
	public static int confSingularityRF;

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
		confCompressorRFCapacity = config.getInt("compressor_rf_capacity", category, 10000000, 0, Integer.MAX_VALUE, "How much RF/FE the Quantum Compressor should hold.");
		confCompressorRFRate = config.getInt("compressor_rf_rate", category, 5000, 0, Integer.MAX_VALUE, "How much RF/t the Quantum Compressor should use when crafting by default.");
		//confCompressorSpeed = config.getInt("compressor_speed", category, 200, 0, Integer.MAX_VALUE, "How many ticks the crafting stage of the Quantum Compressor should take.");
		
		category = "singularity";
		config.setCategoryComment(category, "Wow Blake, back at it again stealing other peoples ideas.");
		confSingularityAmount = config.getInt("_singularity_amount", category, 10000, 1, Integer.MAX_VALUE, "The amount of materials required to create a Singularity, for the default recipes.");
		confSingularityRF = config.getInt("_singularity_rf", category, 5000000, 0, Integer.MAX_VALUE, "The amount of RF required to craft a Singularity, for the default recipes.");
		ModItems.itemSingularityCustom.configure(config);
		ModItems.itemSingularityUltimate.configure(config);

		if (config.hasChanged()) {
			config.save();
		}
	}
}
