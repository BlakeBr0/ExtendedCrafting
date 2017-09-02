package com.blakebr0.extendedcrafting.compat;

import java.util.List;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.block.BlockCompressor;
import com.blakebr0.extendedcrafting.block.BlockCraftingCore;
import com.blakebr0.extendedcrafting.block.BlockLamp;
import com.blakebr0.extendedcrafting.block.BlockPedestal;
import com.blakebr0.extendedcrafting.block.BlockTrimmed;
import com.blakebr0.extendedcrafting.tile.TileCompressor;
import com.blakebr0.extendedcrafting.tile.TileCraftingCore;
import com.blakebr0.extendedcrafting.tile.TilePedestal;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WailaDataProvider implements IWailaDataProvider {

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new WailaDataProvider(), BlockLamp.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockTrimmed.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockPedestal.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockCraftingCore.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockCompressor.class);
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP arg0, TileEntity arg1, NBTTagCompound arg2, World arg3, BlockPos arg4) {
		return null;
	}

	@Override
	public List<String> getWailaBody(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		if (arg2.getBlock() instanceof BlockLamp) {
			arg1.add(Utils.localize("tooltip.ec.lamp_" + BlockLamp.Type.byMetadata(arg2.getMetadata()).getName()));
		}
		
		if (arg2.getBlock() instanceof BlockTrimmed) {
			arg1.add(Utils.localize("tooltip.ec.trimmed_" + BlockTrimmed.Type.byMetadata(arg2.getMetadata()).getName()));
		}
		
		if (arg2.getBlock() instanceof BlockPedestal && arg2.getTileEntity() instanceof TilePedestal && !arg2.getTileEntity().isInvalid()) {
			TilePedestal pedestal = (TilePedestal) arg2.getTileEntity();
			if (!StackHelper.isNull(pedestal.getInventory().getStackInSlot(0))) {
				arg1.add(pedestal.getInventory().getStackInSlot(0).getDisplayName());
			}
		}
		
		if (arg2.getBlock() instanceof BlockCraftingCore && arg2.getTileEntity() instanceof TileCraftingCore && !arg2.getTileEntity().isInvalid()) {
			TileCraftingCore core = (TileCraftingCore) arg2.getTileEntity();
			arg1.add(Utils.format(core.getEnergy().getEnergyStored()) + " RF");
		}
		
		if (arg2.getBlock() instanceof BlockCompressor && arg2.getTileEntity() instanceof TileCompressor && !arg2.getTileEntity().isInvalid()) {
			TileCompressor compressor = (TileCompressor) arg2.getTileEntity();
			arg1.add(Utils.format(compressor.getEnergy().getEnergyStored()) + " RF");
		}
		return arg1;
	}

	@Override
	public List<String> getWailaHead(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		return null;
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor arg0, IWailaConfigHandler arg1) {
		return StackHelper.getNull();
	}

	@Override
	public List<String> getWailaTail(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		return null;
	}
}
