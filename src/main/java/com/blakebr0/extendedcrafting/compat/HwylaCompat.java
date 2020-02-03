package com.blakebr0.extendedcrafting.compat;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.block.AdvancedTableBlock;
import com.blakebr0.extendedcrafting.block.BasicTableBlock;
import com.blakebr0.extendedcrafting.block.CompressorBlock;
import com.blakebr0.extendedcrafting.block.CraftingCoreBlock;
import com.blakebr0.extendedcrafting.block.EliteTableBlock;
import com.blakebr0.extendedcrafting.block.EnderCrafterBlock;
import com.blakebr0.extendedcrafting.block.PedestalBlock;
import com.blakebr0.extendedcrafting.block.UltimateTableBlock;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

@WailaPlugin
public class HwylaCompat implements IWailaPlugin {
	@Override
	public void register(IRegistrar registrar) {
		registrar.registerComponentProvider(new IComponentProvider() {
			@Override
			public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
				PedestalTileEntity pedestal = (PedestalTileEntity) accessor.getTileEntity();
				ItemStack stack = pedestal.getInventory().getStackInSlot(0);
				if (!stack.isEmpty())
					tooltip.add(stack.getDisplayName());
			}
		}, TooltipPosition.BODY, PedestalBlock.class);

		registrar.registerComponentProvider(new IComponentProvider() {
			@Override
			public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
				CraftingCoreTileEntity core = (CraftingCoreTileEntity) accessor.getTileEntity();
				CombinationRecipe recipe = core.getRecipe();
				if (recipe != null) {
					ItemStack output = recipe.getRecipeOutput();
					tooltip.add(Utils.localize("tooltip.ec.crafting", output.getCount(), output.getDisplayName()));
				}
			}
		}, TooltipPosition.BODY, CraftingCoreBlock.class);

		registrar.registerComponentProvider(new IComponentProvider() {
			@Override
			public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(1).build());
			}
		}, TooltipPosition.BODY, BasicTableBlock.class);

		registrar.registerComponentProvider(new IComponentProvider() {
			@Override
			public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(2).build());
			}
		}, TooltipPosition.BODY, AdvancedTableBlock.class);

		registrar.registerComponentProvider(new IComponentProvider() {
			@Override
			public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(3).build());
			}
		}, TooltipPosition.BODY, EliteTableBlock.class);

		registrar.registerComponentProvider(new IComponentProvider() {
			@Override
			public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(4).build());
			}
		}, TooltipPosition.BODY, UltimateTableBlock.class);

		registrar.registerComponentProvider(new IComponentProvider() {
			@Override
			public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
				EnderCrafterTileEntity crafter = (EnderCrafterTileEntity) accessor.getTileEntity();
				ItemStack result = crafter.getResult();
				if (!result.isEmpty()) {
					tooltip.add(Utils.localize("tooltip.ec.output", result.getCount(), result.getDisplayName()));
				}
			}
		}, TooltipPosition.BODY, EnderCrafterBlock.class);

		registrar.registerComponentProvider(new IComponentProvider() {
			@Override
			public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
				CompressorTileEntity compressor = (CompressorTileEntity) tile;
				CompressorRecipe recipe = compressor.getRecipe();
				if (recipe != null) {
					ItemStack output = recipe.getOutput();
					tooltip.add(Utils.localize("tooltip.ec.crafting", output.getCount(), output.getDisplayName()));
				}
			}
		}, TooltipPosition.BODY, CompressorBlock.class);
	}
}
