package com.blakebr0.extendedcrafting.compat;

import java.util.List;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.block.BlockAutomationInterface;
import com.blakebr0.extendedcrafting.block.CompressorBlock;
import com.blakebr0.extendedcrafting.block.CraftingCoreBlock;
import com.blakebr0.extendedcrafting.block.EnderCrafterBlock;
import com.blakebr0.extendedcrafting.block.BlockLamp;
import com.blakebr0.extendedcrafting.block.PedestalBlock;
import com.blakebr0.extendedcrafting.block.BlockTrimmed;
import com.blakebr0.extendedcrafting.block.AdvancedTableBlock;
import com.blakebr0.extendedcrafting.block.BasicTableBlock;
import com.blakebr0.extendedcrafting.block.EliteTableBlock;
import com.blakebr0.extendedcrafting.block.UltimateTableBlock;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.tileentity.TileAutomationInterface;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

@WailaPlugin
public class HwylaCompat implements IWailaPlugin {
	@Override
	public List<String> getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor data, IWailaConfigHandler config) {
		Block block = data.getBlock();
		TileEntity tile = data.getTileEntity();
		
		if (block instanceof PedestalBlock && tile instanceof PedestalTileEntity && !tile.isInvalid()) {
			PedestalTileEntity pedestal = (PedestalTileEntity) tile;
			ItemStack result = pedestal.getStack();
			if (!result.isEmpty()) {
				tooltip.add(result.getDisplayName());
			}
		}
		
		if (block instanceof CraftingCoreBlock && tile instanceof CraftingCoreTileEntity && !tile.isInvalid()) {
			CraftingCoreTileEntity core = (CraftingCoreTileEntity) tile;
			
			if (ModConfigs.confEnergyInWaila) {
				tooltip.add(Utils.format(core.getEnergy().getEnergyStored()) + " FE");
			}
			
			CombinationRecipe recipe = core.getRecipe();
			if (recipe != null) {
				ItemStack output = recipe.getOutput();
				tooltip.add(Utils.localize("tooltip.ec.crafting", output.getCount(), output.getDisplayName()));
			}
		}
		
		if (block instanceof BasicTableBlock) tooltip.add(Utils.localize("tooltip.ec.tier", 1));
		if (block instanceof AdvancedTableBlock) tooltip.add(Utils.localize("tooltip.ec.tier", 2));
		if (block instanceof EliteTableBlock) tooltip.add(Utils.localize("tooltip.ec.tier", 3));
		if (block instanceof UltimateTableBlock) tooltip.add(Utils.localize("tooltip.ec.tier", 4));
		
		if (block instanceof BlockAutomationInterface && tile instanceof TileAutomationInterface && !tile.isInvalid()) {
			TileAutomationInterface auto = (TileAutomationInterface) tile;
			
			if (ModConfigs.confEnergyInWaila) {
				tooltip.add(Utils.format(auto.getEnergy().getEnergyStored()) + " FE");
			}
			
			ItemStack result = auto.getResult();
			if (!result.isEmpty()) {
				tooltip.add(Utils.localize("tooltip.ec.crafting", result.getCount(), result.getDisplayName()));
			}
		}
		
		if (block instanceof EnderCrafterBlock && tile instanceof EnderCrafterTileEntity && !tile.isInvalid()) {
			EnderCrafterTileEntity crafter = (EnderCrafterTileEntity) tile;
			ItemStack result = crafter.getResult();
			if (!result.isEmpty()) {
				tooltip.add(Utils.localize("tooltip.ec.output", result.getCount(), result.getDisplayName()));
			}
		}
		
		if (block instanceof CompressorBlock && tile instanceof CompressorTileEntity && !tile.isInvalid()) {
			CompressorTileEntity compressor = (CompressorTileEntity) tile;
			
			if (ModConfigs.confEnergyInWaila) {
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
	public void register(IRegistrar iRegistrar) {

	}
}
