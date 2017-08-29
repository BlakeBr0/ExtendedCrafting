package com.blakebr0.extendedcrafting.compat;

import net.minecraft.block.BlockCrops;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.text.NumberFormat;
import java.util.List;

import com.blakebr0.extendedcrafting.block.BlockCraftingCore;
import com.blakebr0.extendedcrafting.block.BlockPedestal;
import com.blakebr0.extendedcrafting.tile.TileCraftingCore;
import com.blakebr0.extendedcrafting.tile.TilePedestal;
import com.blakebr0.extendedcrafting.util.StackHelper;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaDataProvider implements IWailaDataProvider {

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new WailaDataProvider(), BlockPedestal.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockCraftingCore.class);
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP arg0, TileEntity arg1, NBTTagCompound arg2, World arg3, BlockPos arg4) {
		return null;
	}

	@Override
	public List<String> getWailaBody(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2, IWailaConfigHandler arg3) {
		if(arg2.getBlock() instanceof BlockPedestal && arg2.getTileEntity() instanceof TilePedestal && !arg2.getTileEntity().isInvalid()){
			TilePedestal pedestal = (TilePedestal)arg2.getTileEntity();
			if(!StackHelper.isNull(pedestal.getInventory().getStackInSlot(0))){
				arg1.add(pedestal.getInventory().getStackInSlot(0).getDisplayName());
			}
		}
		if(arg2.getBlock() instanceof BlockCraftingCore && arg2.getTileEntity() instanceof TileCraftingCore && !arg2.getTileEntity().isInvalid()){
			TileCraftingCore core = (TileCraftingCore)arg2.getTileEntity();
			arg1.add(NumberFormat.getInstance().format(core.getEnergy().getEnergyStored()) + " RF");
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
