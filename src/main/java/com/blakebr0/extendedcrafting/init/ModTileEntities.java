package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.tileentity.AdvancedTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoEnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoFluxCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.BasicTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.EliteTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.FluxAlternatorTileEntity;
import com.blakebr0.extendedcrafting.tileentity.FluxCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import com.blakebr0.extendedcrafting.tileentity.TheUltimateBlockTileEntity;
import com.blakebr0.extendedcrafting.tileentity.UltimateTableTileEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModTileEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExtendedCrafting.MOD_ID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TheUltimateBlockTileEntity>> THE_ULTIMATE_BLOCK = register("the_ultimate_block", TheUltimateBlockTileEntity::new, () -> new Block[] { ModBlocks.THE_ULTIMATE_BLOCK.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PedestalTileEntity>> PEDESTAL = register("pedestal", PedestalTileEntity::new, () -> new Block[] { ModBlocks.PEDESTAL.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CraftingCoreTileEntity>> CRAFTING_CORE = register("crafting_core", CraftingCoreTileEntity::new, () -> new Block[] { ModBlocks.CRAFTING_CORE.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BasicTableTileEntity>> BASIC_TABLE = register("basic_table", BasicTableTileEntity::new, () -> new Block[] { ModBlocks.BASIC_TABLE.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedTableTileEntity>> ADVANCED_TABLE = register("advanced_table", AdvancedTableTileEntity::new, () -> new Block[] { ModBlocks.ADVANCED_TABLE.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EliteTableTileEntity>> ELITE_TABLE = register("elite_table", EliteTableTileEntity::new, () -> new Block[] { ModBlocks.ELITE_TABLE.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<UltimateTableTileEntity>> ULTIMATE_TABLE = register("ultimate_table", UltimateTableTileEntity::new, () -> new Block[] { ModBlocks.ULTIMATE_TABLE.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoTableTileEntity.Basic>> BASIC_AUTO_TABLE = register("basic_auto_table", AutoTableTileEntity.Basic::new, () -> new Block[] { ModBlocks.BASIC_AUTO_TABLE.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoTableTileEntity.Advanced>> ADVANCED_AUTO_TABLE = register("advanced_auto_table", AutoTableTileEntity.Advanced::new, () -> new Block[] { ModBlocks.ADVANCED_AUTO_TABLE.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoTableTileEntity.Elite>> ELITE_AUTO_TABLE = register("elite_auto_table", AutoTableTileEntity.Elite::new, () -> new Block[] { ModBlocks.ELITE_AUTO_TABLE.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoTableTileEntity.Ultimate>> ULTIMATE_AUTO_TABLE = register("ultimate_auto_table", AutoTableTileEntity.Ultimate::new, () -> new Block[] { ModBlocks.ULTIMATE_AUTO_TABLE.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CompressorTileEntity>> COMPRESSOR = register("compressor", CompressorTileEntity::new, () -> new Block[] { ModBlocks.COMPRESSOR.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnderCrafterTileEntity>> ENDER_CRAFTER = register("ender_crafter", EnderCrafterTileEntity::new, () -> new Block[] { ModBlocks.ENDER_CRAFTER.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoEnderCrafterTileEntity>> AUTO_ENDER_CRAFTER = register("auto_ender_crafter", AutoEnderCrafterTileEntity::new, () -> new Block[] { ModBlocks.AUTO_ENDER_CRAFTER.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluxAlternatorTileEntity>> FLUX_ALTERNATOR = register("flux_alternator", FluxAlternatorTileEntity::new, () -> new Block[] { ModBlocks.FLUX_ALTERNATOR.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluxCrafterTileEntity>> FLUX_CRAFTER = register("flux_crafter", FluxCrafterTileEntity::new, () -> new Block[] { ModBlocks.FLUX_CRAFTER.get() });
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoFluxCrafterTileEntity>> AUTO_FLUX_CRAFTER = register("auto_flux_crafter", AutoFluxCrafterTileEntity::new, () -> new Block[] { ModBlocks.AUTO_FLUX_CRAFTER.get() });

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> tile, Supplier<Block[]> blocks) {
		return REGISTRY.register(name, () -> new BlockEntityType<>(tile, blocks.get()));
	}
}
