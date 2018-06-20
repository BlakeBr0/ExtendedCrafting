package com.blakebr0.extendedcrafting.compat;

import java.util.List;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.block.BlockAutomationInterface;
import com.blakebr0.extendedcrafting.block.BlockCompressor;
import com.blakebr0.extendedcrafting.block.BlockCraftingCore;
import com.blakebr0.extendedcrafting.block.BlockEnderCrafter;
import com.blakebr0.extendedcrafting.block.BlockLamp;
import com.blakebr0.extendedcrafting.block.BlockPedestal;
import com.blakebr0.extendedcrafting.block.BlockTrimmed;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import com.blakebr0.extendedcrafting.tile.TileCompressor;
import com.blakebr0.extendedcrafting.tile.TileCraftingCore;
import com.blakebr0.extendedcrafting.tile.TileEnderCrafter;
import com.blakebr0.extendedcrafting.tile.TilePedestal;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
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
		registrar.registerBodyProvider(new WailaDataProvider(), BlockAutomationInterface.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockEnderCrafter.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockCompressor.class);
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, BlockPos pos) {
		return null;
	}

	@Override
	public List<String> getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor data, IWailaConfigHandler config) {
		Block block = data.getBlock();
		TileEntity tile = data.getTileEntity();
		
		if (block instanceof BlockLamp) {
			tooltip.add(Utils.localize("tooltip.ec.lamp_" + BlockLamp.Type.byMetadata(data.getMetadata()).getName()));
		}
		
		if (block instanceof BlockTrimmed) {
			tooltip.add(Utils.localize("tooltip.ec.trimmed_" + BlockTrimmed.Type.byMetadata(data.getMetadata()).getName()));
		}
		
		if (block instanceof BlockPedestal && tile instanceof TilePedestal && !tile.isInvalid()) {
			TilePedestal pedestal = (TilePedestal) tile;
			ItemStack invStack = pedestal.getInventory().getStackInSlot(0);
			if (!invStack.isEmpty()) {
				tooltip.add(invStack.getDisplayName());
			}
		}
		
		if (block instanceof BlockCraftingCore && tile instanceof TileCraftingCore && !tile.isInvalid()) {
			TileCraftingCore core = (TileCraftingCore) tile;
			tooltip.add(Utils.format(core.getEnergy().getEnergyStored()) + " FE");
		}
		
		if (block instanceof BlockAutomationInterface && tile instanceof TileAutomationInterface && !tile.isInvalid()) {
			TileAutomationInterface auto = (TileAutomationInterface) tile;
			tooltip.add(Utils.format(auto.getEnergy().getEnergyStored()) + " FE");
			
			ItemStack result = auto.getResult();
			if (!result.isEmpty()) {
				tooltip.add(result.getDisplayName());
			}
		}
		
		if (block instanceof BlockEnderCrafter && tile instanceof TileEnderCrafter && !tile.isInvalid()) {
			TileEnderCrafter crafter = (TileEnderCrafter) tile;
			ItemStack result = crafter.getResult();
			if (!result.isEmpty()) {
				tooltip.add(Utils.localize("tooltip.ec.output") + result.getCount() + "x " + result.getDisplayName());
			}
		}
		
		if (block instanceof BlockCompressor && tile instanceof TileCompressor && !tile.isInvalid()) {
			TileCompressor compressor = (TileCompressor) tile;
			tooltip.add(Utils.format(compressor.getEnergy().getEnergyStored()) + " FE");
		}
		return tooltip;
	}

	@Override
	public List<String> getWailaHead(ItemStack stack, List<String> tooltip, IWailaDataAccessor data, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor data, IWailaConfigHandler config) {
		return ItemStack.EMPTY;
	}

	@Override
	public List<String> getWailaTail(ItemStack stack, List<String> tooltip, IWailaDataAccessor data, IWailaConfigHandler config) {
		return null;
	}
}
