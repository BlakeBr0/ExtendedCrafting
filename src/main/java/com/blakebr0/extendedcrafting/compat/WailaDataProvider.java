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
import com.blakebr0.extendedcrafting.block.craftingtable.BlockAdvancedTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockBasicTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockEliteTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockUltimateTable;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipe;
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
		registrar.registerBodyProvider(new WailaDataProvider(), BlockBasicTable.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockAdvancedTable.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockEliteTable.class);
		registrar.registerBodyProvider(new WailaDataProvider(), BlockUltimateTable.class);
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
			ItemStack result = pedestal.getStack();
			if (!result.isEmpty()) {
				tooltip.add(result.getDisplayName());
			}
		}
		
		if (block instanceof BlockCraftingCore && tile instanceof TileCraftingCore && !tile.isInvalid()) {
			TileCraftingCore core = (TileCraftingCore) tile;
			
			if (ModConfig.confEnergyInWaila) {
				tooltip.add(Utils.format(core.getEnergy().getEnergyStored()) + " FE");
			}
			
			CombinationRecipe recipe = core.getRecipe();
			if (recipe != null) {
				ItemStack output = recipe.getOutput();
				tooltip.add(Utils.localize("tooltip.ec.crafting", output.getCount(), output.getDisplayName()));
			}
		}
		
		if (block instanceof BlockBasicTable) tooltip.add(Utils.localize("tooltip.ec.tier", 1));
		if (block instanceof BlockAdvancedTable) tooltip.add(Utils.localize("tooltip.ec.tier", 2));
		if (block instanceof BlockEliteTable) tooltip.add(Utils.localize("tooltip.ec.tier", 3));
		if (block instanceof BlockUltimateTable) tooltip.add(Utils.localize("tooltip.ec.tier", 4));
		
		if (block instanceof BlockAutomationInterface && tile instanceof TileAutomationInterface && !tile.isInvalid()) {
			TileAutomationInterface auto = (TileAutomationInterface) tile;
			
			if (ModConfig.confEnergyInWaila) {
				tooltip.add(Utils.format(auto.getEnergy().getEnergyStored()) + " FE");
			}
			
			ItemStack result = auto.getResult();
			if (!result.isEmpty()) {
				tooltip.add(Utils.localize("tooltip.ec.crafting", result.getCount(), result.getDisplayName()));
			}
		}
		
		if (block instanceof BlockEnderCrafter && tile instanceof TileEnderCrafter && !tile.isInvalid()) {
			TileEnderCrafter crafter = (TileEnderCrafter) tile;
			ItemStack result = crafter.getResult();
			if (!result.isEmpty()) {
				tooltip.add(Utils.localize("tooltip.ec.output", result.getCount(), result.getDisplayName()));
			}
		}
		
		if (block instanceof BlockCompressor && tile instanceof TileCompressor && !tile.isInvalid()) {
			TileCompressor compressor = (TileCompressor) tile;
			
			if (ModConfig.confEnergyInWaila) {
				tooltip.add(Utils.format(compressor.getEnergy().getEnergyStored()) + " FE");
			}
			
			CompressorRecipe recipe = compressor.getRecipe();
			if (recipe != null) {
				ItemStack output = recipe.getOutput();
				tooltip.add(Utils.localize("tooltip.ec.crafting", output.getCount(), output.getDisplayName()));
			}
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
