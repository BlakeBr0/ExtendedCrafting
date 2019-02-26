package com.blakebr0.extendedcrafting.tile;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfig;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTiles {

	// TODO: 1.13, correct the namespaces
	public static void init() {
		if (ModConfig.confCraftingCoreEnabled) {
			GameRegistry.registerTileEntity(TilePedestal.class, "EC_Pedestal");
			GameRegistry.registerTileEntity(TileCraftingCore.class, "EC_Crafting_Core");
		}

		if (ModConfig.confInterfaceEnabled) {
			GameRegistry.registerTileEntity(TileAutomationInterface.class, "EC_Automation_Interface");
			GameRegistry.registerTileEntity(TileUltimaterInterface.class, "EC_Ultimater_Interface");
		}

		if (ModConfig.confTableEnabled) {
			GameRegistry.registerTileEntity(TileBasicCraftingTable.class, "EC_Basic_Table");
			GameRegistry.registerTileEntity(TileAdvancedCraftingTable.class, "EC_Advanced_Table");
			GameRegistry.registerTileEntity(TileEliteCraftingTable.class, "EC_Elite_Table");
			GameRegistry.registerTileEntity(TileUltimateCraftingTable.class, "EC_Ultimate_Table");
		}

		if (ModConfig.confCompressorEnabled) {
			GameRegistry.registerTileEntity(TileCompressor.class, "EC_Compressor");
		}
		
		if (ModConfig.confEnderEnabled) {
			GameRegistry.registerTileEntity(TileEnderCrafter.class, ExtendedCrafting.MOD_ID + "ender_crafter");
		}
	}
}
