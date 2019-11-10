package com.blakebr0.extendedcrafting.client.tesr;

import com.blakebr0.extendedcrafting.tileentity.TileAutomationInterface;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;

import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRenders {

	public static void init() {
		ClientRegistry.bindTileEntitySpecialRenderer(PedestalTileEntity.class, new RenderPedestal());
		ClientRegistry.bindTileEntitySpecialRenderer(CraftingCoreTileEntity.class, new RenderCraftingCore());
		ClientRegistry.bindTileEntitySpecialRenderer(CompressorTileEntity.class, new RenderCompressor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAutomationInterface.class, new RenderAutomationInterface());
	}
}
