package com.blakebr0.extendedcrafting.tile;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTiles {

	public static void init() {
		GameRegistry.registerTileEntity(TileCraftingTable.class, "EC_Crafting_Table");

		GameRegistry.registerTileEntity(TilePedestal.class, "EC_Pedestal");
		GameRegistry.registerTileEntity(TileCraftingCore.class, "EC_Crafting_Core");
		
		GameRegistry.registerTileEntity(TileAutomationInterface.class, "EC_Automation_Interface");

		GameRegistry.registerTileEntity(TileBasicCraftingTable.class, "EC_Basic_Table");
		GameRegistry.registerTileEntity(TileAdvancedCraftingTable.class, "EC_Advanced_Table");
		GameRegistry.registerTileEntity(TileEliteCraftingTable.class, "EC_Elite_Table");
		GameRegistry.registerTileEntity(TileUltimateCraftingTable.class, "EC_Ultimate_Table");

		GameRegistry.registerTileEntity(TileCompressor.class, "EC_Compressor");
	}
}
