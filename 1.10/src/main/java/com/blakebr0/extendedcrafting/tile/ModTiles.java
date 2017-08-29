package com.blakebr0.extendedcrafting.tile;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTiles {

	public static void init(){
		GameRegistry.registerTileEntity(TilePedestal.class, "EC_Pedestal");
		GameRegistry.registerTileEntity(TileCraftingCore.class, "EC_Crafting_Core");
	}
}
