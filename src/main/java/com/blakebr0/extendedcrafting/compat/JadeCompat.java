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
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {
	private static final Identifier CRAFTING_CORE_PROVIDER = ExtendedCrafting.resource("crafting_core");
	private static final Identifier BASIC_TABLE_PROVIDER = ExtendedCrafting.resource("basic_table");
	private static final Identifier ADVANCED_TABLE_PROVIDER = ExtendedCrafting.resource("advanced_table");
	private static final Identifier ELITE_TABLE_PROVIDER = ExtendedCrafting.resource("elite_table");
	private static final Identifier ULTIMATE_TABLE_PROVIDER = ExtendedCrafting.resource("ultimate_table");
	private static final Identifier BASIC_AUTO_TABLE_PROVIDER = ExtendedCrafting.resource("basic_auto_table");
	private static final Identifier ADVANCED_AUTO_TABLE_PROVIDER = ExtendedCrafting.resource("advanced_auto_table");
	private static final Identifier ELITE_AUTO_TABLE_PROVIDER = ExtendedCrafting.resource("elite_auto_table");
	private static final Identifier ULTIMATE_AUTO_TABLE_PROVIDER = ExtendedCrafting.resource("ultimate_auto_table");
	private static final Identifier ENDER_CRAFTER_PROVIDER = ExtendedCrafting.resource("ender_crafter");
	private static final Identifier AUTO_ENDER_CRAFTER_PROVIDER = ExtendedCrafting.resource("auto_ender_crafter");
	private static final Identifier FLUX_CRAFTER_PROVIDER = ExtendedCrafting.resource("flux_crafter");
	private static final Identifier COMPRESSOR_PROVIDER = ExtendedCrafting.resource("compressor");


	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				var core = (CraftingCoreTileEntity) accessor.getBlockEntity();
				var recipe = core.getActiveRecipe();

				if (recipe != null) {
					var level = accessor.getLevel();
					var output = recipe.getResultItem(level.registryAccess());

					tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
				}
			}

			@Override
			public Identifier getUid() {
				return CRAFTING_CORE_PROVIDER;
			}
		}, CraftingCoreBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(1).build());
			}

			@Override
			public Identifier getUid() {
				return BASIC_TABLE_PROVIDER;
			}
		}, BasicTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(2).build());
			}

			@Override
			public Identifier getUid() {
				return ADVANCED_TABLE_PROVIDER;
			}
		}, AdvancedTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(3).build());
			}

			@Override
			public Identifier getUid() {
				return ELITE_TABLE_PROVIDER;
			}
		}, EliteTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(4).build());
			}

			@Override
			public Identifier getUid() {
				return ULTIMATE_TABLE_PROVIDER;
			}
		}, UltimateTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(1).build());
			}

			@Override
			public Identifier getUid() {
				return BASIC_AUTO_TABLE_PROVIDER;
			}
		}, BasicAutoTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(2).build());
			}

			@Override
			public Identifier getUid() {
				return ADVANCED_AUTO_TABLE_PROVIDER;
			}
		}, AdvancedAutoTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(3).build());
			}

			@Override
			public Identifier getUid() {
				return ELITE_AUTO_TABLE_PROVIDER;
			}
		}, EliteAutoTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				tooltip.add(ModTooltips.TIER.args(4).build());
			}

			@Override
			public Identifier getUid() {
				return ULTIMATE_AUTO_TABLE_PROVIDER;
			}
		}, UltimateAutoTableBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				var crafter = (EnderCrafterTileEntity) accessor.getBlockEntity();
				var recipe = crafter.getActiveRecipe();

				if (recipe != null) {
					var level = accessor.getLevel();
					var output = recipe.getResultItem(level.registryAccess());

					tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
				}
			}

			@Override
			public Identifier getUid() {
				return ENDER_CRAFTER_PROVIDER;
			}
		}, EnderCrafterBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				var crafter = (EnderCrafterTileEntity) accessor.getBlockEntity();
				var recipe = crafter.getActiveRecipe();

				if (recipe != null) {
					var level = accessor.getLevel();
					var output = recipe.getResultItem(level.registryAccess());

					tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
				}
			}

			@Override
			public Identifier getUid() {
				return AUTO_ENDER_CRAFTER_PROVIDER;
			}
		}, AutoEnderCrafterBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				var crafter = (FluxCrafterTileEntity) accessor.getBlockEntity();
				var recipe = crafter.getActiveRecipe();

				if (recipe != null) {
					var level = accessor.getLevel();
					var output = recipe.getResultItem(level.registryAccess());

					tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
				}
			}

			@Override
			public Identifier getUid() {
				return FLUX_CRAFTER_PROVIDER;
			}
		}, FluxCrafterBlock.class);

		registration.registerBlockComponent(new IBlockComponentProvider() {
			@Override
			public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
				var compressor = (CompressorTileEntity) accessor.getBlockEntity();
				var recipe = compressor.getActiveRecipe();

				if (recipe != null) {
					var level = accessor.getLevel();
					var output = recipe.getResultItem(level.registryAccess());

					tooltip.add(ModTooltips.CRAFTING.args(output.getCount(), output.getHoverName()).build());
				}
			}

			@Override
			public Identifier getUid() {
				return COMPRESSOR_PROVIDER;
			}
		}, CompressorBlock.class);
	}
}
