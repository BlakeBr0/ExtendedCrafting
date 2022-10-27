package com.blakebr0.extendedcrafting.compat;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.AdvancedAutoTableBlock;
import com.blakebr0.extendedcrafting.block.AdvancedTableBlock;
import com.blakebr0.extendedcrafting.block.AutoEnderCrafterBlock;
import com.blakebr0.extendedcrafting.block.BasicAutoTableBlock;
import com.blakebr0.extendedcrafting.block.BasicTableBlock;
import com.blakebr0.extendedcrafting.block.CompressorBlock;
import com.blakebr0.extendedcrafting.block.CraftingCoreBlock;
import com.blakebr0.extendedcrafting.block.EliteAutoTableBlock;
import com.blakebr0.extendedcrafting.block.EliteTableBlock;
import com.blakebr0.extendedcrafting.block.EnderCrafterBlock;
import com.blakebr0.extendedcrafting.block.FluxCrafterBlock;
import com.blakebr0.extendedcrafting.block.UltimateAutoTableBlock;
import com.blakebr0.extendedcrafting.block.UltimateTableBlock;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.FluxCrafterTileEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {
	private static final ResourceLocation CRAFTING_CORE_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "crafting_core");
	private static final ResourceLocation BASIC_TABLE_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "basic_table");
	private static final ResourceLocation ADVANCED_TABLE_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "advanced_table");
	private static final ResourceLocation ELITE_TABLE_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "elite_table");
	private static final ResourceLocation ULTIMATE_TABLE_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "ultimate_table");
	private static final ResourceLocation BASIC_AUTO_TABLE_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "basic_auto_table");
	private static final ResourceLocation ADVANCED_AUTO_TABLE_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "advanced_auto_table");
	private static final ResourceLocation ELITE_AUTO_TABLE_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "elite_auto_table");
	private static final ResourceLocation ULTIMATE_AUTO_TABLE_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "ultimate_auto_table");
	private static final ResourceLocation ENDER_CRAFTER_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "ender_crafter");
	private static final ResourceLocation AUTO_ENDER_CRAFTER_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "auto_ender_crafter");
	private static final ResourceLocation FLUX_CRAFTER_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "flux_crafter");
	private static final ResourceLocation COMPRESSOR_PROVIDER = new ResourceLocation(ExtendedCrafting.MOD_ID, "compressor");


	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				var core = (CraftingCoreTileEntity) accessor.getBlockEntity();
				var recipe = core.getActiveRecipe();

				if (recipe != null) {
					var output = recipe.getResultItem();
					tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
				}
			}

			@Override
			public ResourceLocation getUid() {
				return CRAFTING_CORE_PROVIDER;
			}
		}, CraftingCoreBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(1).build());
			}

			@Override
			public ResourceLocation getUid() {
				return BASIC_TABLE_PROVIDER;
			}
		}, BasicTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(2).build());
			}

			@Override
			public ResourceLocation getUid() {
				return ADVANCED_TABLE_PROVIDER;
			}
		}, AdvancedTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(3).build());
			}

			@Override
			public ResourceLocation getUid() {
				return ELITE_TABLE_PROVIDER;
			}
		}, EliteTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(4).build());
			}

			@Override
			public ResourceLocation getUid() {
				return ULTIMATE_TABLE_PROVIDER;
			}
		}, UltimateTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(1).build());
			}

			@Override
			public ResourceLocation getUid() {
				return BASIC_AUTO_TABLE_PROVIDER;
			}
		}, BasicAutoTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(2).build());
			}

			@Override
			public ResourceLocation getUid() {
				return ADVANCED_AUTO_TABLE_PROVIDER;
			}
		}, AdvancedAutoTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(3).build());
			}

			@Override
			public ResourceLocation getUid() {
				return ELITE_AUTO_TABLE_PROVIDER;
			}
		}, EliteAutoTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(4).build());
			}

			@Override
			public ResourceLocation getUid() {
				return ULTIMATE_AUTO_TABLE_PROVIDER;
			}
		}, UltimateAutoTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				var crafter = (EnderCrafterTileEntity) accessor.getBlockEntity();
				var recipe = crafter.getActiveRecipe();

				if (recipe != null) {
					var output = recipe.getResultItem();
					tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
				}
			}

			@Override
			public ResourceLocation getUid() {
				return ENDER_CRAFTER_PROVIDER;
			}
		}, EnderCrafterBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				var crafter = (EnderCrafterTileEntity) accessor.getBlockEntity();
				var recipe = crafter.getActiveRecipe();

				if (recipe != null) {
					var output = recipe.getResultItem();
					tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
				}
			}

			@Override
			public ResourceLocation getUid() {
				return AUTO_ENDER_CRAFTER_PROVIDER;
			}
		}, AutoEnderCrafterBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				var crafter = (FluxCrafterTileEntity) accessor.getBlockEntity();
				var recipe = crafter.getActiveRecipe();

				if (recipe != null) {
					var output = recipe.getResultItem();
					tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
				}
			}

			@Override
			public ResourceLocation getUid() {
				return FLUX_CRAFTER_PROVIDER;
			}
		}, FluxCrafterBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				var compressor = (CompressorTileEntity) accessor.getBlockEntity();
				var recipe = compressor.getActiveRecipe();

				if (recipe != null) {
					var output = recipe.getResultItem();
					tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
				}
			}

			@Override
			public ResourceLocation getUid() {
				return COMPRESSOR_PROVIDER;
			}
		}, CompressorBlock.class);
	}
}
