package com.blakebr0.extendedcrafting.lib;

import com.blakebr0.cucumber.guide.Guide;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.item.ModItems;

import net.minecraft.item.ItemStack;

public class ModGuide {

	public static final Guide GUIDE = Guide.create("extendedcrafting", "BlakeBr0", 0x808080);

	public static void setup() {
		if (ModConfig.confGuideEnabled) {
			GUIDE.addEntry("introduction")
				 .setIconStack(new ItemStack(ModItems.itemGuide))
				 .addPageText();

			if (ModConfig.confCraftingCoreEnabled) {
				GUIDE.addEntry("combination_crafting")
				     .setIconStack(new ItemStack(ModBlocks.blockCraftingCore))
				     .addPageText()
				     .addPageText();
			}

			if (ModConfig.confTableEnabled) {
				GUIDE.addEntry("table_crafting")
				     .setIconStack(new ItemStack(ModBlocks.blockUltimateTable))
				     .addPageText();
			}

			if (ModConfig.confCompressorEnabled) {
				GUIDE.addEntry("quantum_compression")
				     .setIconStack(new ItemStack(ModBlocks.blockCompressor))
				     .addPageText()
				     .addPageText()
				     .addPageText();
			}

			if (ModConfig.confEnderEnabled) {
				GUIDE.addEntry("ender_crafting")
				     .setIconStack(new ItemStack(ModBlocks.blockEnderCrafter))
				     .addPageText();
			}

			if (ModConfig.confInterfaceEnabled) {
				GUIDE.addEntry("interface")
				     .setIconStack(new ItemStack(ModBlocks.blockAutomationInterface))
				     .addPageText()
				     .addPageText()
				     .addPageText()
				     .addPageText();
			}

			if (ModConfig.confSingularityEnabled) {
				GUIDE.addEntry("singularity")
				     .setIconStack(new ItemStack(ModItems.itemSingularityUltimate))
				     .addPageText();
			}
		}
	}
}
