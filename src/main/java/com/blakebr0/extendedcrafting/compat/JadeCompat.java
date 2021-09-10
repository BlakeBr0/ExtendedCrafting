package com.blakebr0.extendedcrafting.compat;

import com.blakebr0.extendedcrafting.block.AdvancedAutoTableBlock;
import com.blakebr0.extendedcrafting.block.AdvancedTableBlock;
import com.blakebr0.extendedcrafting.block.BasicAutoTableBlock;
import com.blakebr0.extendedcrafting.block.BasicTableBlock;
import com.blakebr0.extendedcrafting.block.CompressorBlock;
import com.blakebr0.extendedcrafting.block.CraftingCoreBlock;
import com.blakebr0.extendedcrafting.block.EliteAutoTableBlock;
import com.blakebr0.extendedcrafting.block.EliteTableBlock;
import com.blakebr0.extendedcrafting.block.EnderCrafterBlock;
import com.blakebr0.extendedcrafting.block.PedestalBlock;
import com.blakebr0.extendedcrafting.block.UltimateAutoTableBlock;
import com.blakebr0.extendedcrafting.block.UltimateTableBlock;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {
	@Override
	public void register(IRegistrar registrar) {
		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			var pedestal = (PedestalTileEntity) accessor.getBlockEntity();
			var stack = pedestal.getInventory().getStackInSlot(0);

			if (!stack.isEmpty())
				tooltip.add(stack.getHoverName());
		}, TooltipPosition.BODY, PedestalBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			var core = (CraftingCoreTileEntity) accessor.getBlockEntity();
			var recipe = core.getActiveRecipe();

			if (recipe != null) {
				var output = recipe.getResultItem();
				tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
			}
		}, TooltipPosition.BODY, CraftingCoreBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			tooltip.add(ModTooltips.TIER.args(1).build());
		}, TooltipPosition.BODY, BasicTableBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			tooltip.add(ModTooltips.TIER.args(2).build());
		}, TooltipPosition.BODY, AdvancedTableBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			tooltip.add(ModTooltips.TIER.args(3).build());
		}, TooltipPosition.BODY, EliteTableBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			tooltip.add(ModTooltips.TIER.args(4).build());
		}, TooltipPosition.BODY, UltimateTableBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			tooltip.add(ModTooltips.TIER.args(1).build());
		}, TooltipPosition.BODY, BasicAutoTableBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			tooltip.add(ModTooltips.TIER.args(2).build());
		}, TooltipPosition.BODY, AdvancedAutoTableBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			tooltip.add(ModTooltips.TIER.args(3).build());
		}, TooltipPosition.BODY, EliteAutoTableBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			tooltip.add(ModTooltips.TIER.args(4).build());
		}, TooltipPosition.BODY, UltimateAutoTableBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			var crafter = (EnderCrafterTileEntity) accessor.getBlockEntity();
			var recipe = crafter.getActiveRecipe();

			if (recipe != null) {
				var output = recipe.getResultItem();
				tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
			}
		}, TooltipPosition.BODY, EnderCrafterBlock.class);

		registrar.registerComponentProvider((tooltip, accessor, config) -> {
			var compressor = (CompressorTileEntity) accessor.getBlockEntity();
			var recipe = compressor.getActiveRecipe();

			if (recipe != null) {
				var output = recipe.getResultItem();
				tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
			}
		}, TooltipPosition.BODY, CompressorBlock.class);
	}
}
