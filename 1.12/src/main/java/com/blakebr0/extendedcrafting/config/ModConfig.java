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

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.getModID().equals(ExtendedCrafting.MOD_ID)) {
			ModConfig.syncConfig();
		}
	}

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
		syncConfig();
	}

	public static void syncConfig() {

		String category;

		category = "settings";
		config.setCategoryComment(category, "Settings for Extended Crafting content.");
		confCraftingCoreRFCapacity = config.getInt("crafting_core_rf_capacity", category, 5000000, 0, Integer.MAX_VALUE, "How much RF/FE the Crafting Core should hold.");
		confCompressorRFCapacity = config.getInt("compressor_rf_capacity", category, 5000000, 0, Integer.MAX_VALUE, "How much RF/FE the Quantum Compressor should hold.");
		confCompressorRFRate = config.getInt("compressor_rf_rate", category, 5000, 0, Integer.MAX_VALUE, "How much RF/t the Quantum Compressor should use when crafting by default.");
		//confCompressorSpeed = config.getInt("compressor_speed", category, 200, 0, Integer.MAX_VALUE, "How many ticks the crafting stage of the Quantum Compressor should take.");
		
		category = "singularity";
		config.setCategoryComment(category, "Wow Blake, back at it again stealing other peoples ideas.");
		ModItems.itemSingularityCustom.configure(config);
		ModItems.itemSingularityUltimate.configure(config);

		if (config.hasChanged()) {
			config.save();
		}
	}
}
