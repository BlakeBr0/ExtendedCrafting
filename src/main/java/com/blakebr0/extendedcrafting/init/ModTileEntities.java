package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.tesr.CompressorRenderer;
import com.blakebr0.extendedcrafting.client.tesr.CraftingCoreRenderer;
import com.blakebr0.extendedcrafting.client.tesr.PedestalRenderer;
import com.blakebr0.extendedcrafting.tileentity.AdvancedTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.BasicTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.EliteTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import com.blakebr0.extendedcrafting.tileentity.UltimateTableTileEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModTileEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExtendedCrafting.MOD_ID);

	public static final RegistryObject<BlockEntityType<PedestalTileEntity>> PEDESTAL = register("pedestal", PedestalTileEntity::new, () -> new Block[] { ModBlocks.PEDESTAL.get() });
	public static final RegistryObject<BlockEntityType<CraftingCoreTileEntity>> CRAFTING_CORE = register("crafting_core", CraftingCoreTileEntity::new, () -> new Block[] { ModBlocks.CRAFTING_CORE.get() });
	public static final RegistryObject<BlockEntityType<BasicTableTileEntity>> BASIC_TABLE = register("basic_table", BasicTableTileEntity::new, () -> new Block[] { ModBlocks.BASIC_TABLE.get() });
	public static final RegistryObject<BlockEntityType<AdvancedTableTileEntity>> ADVANCED_TABLE = register("advanced_table", AdvancedTableTileEntity::new, () -> new Block[] { ModBlocks.ADVANCED_TABLE.get() });
	public static final RegistryObject<BlockEntityType<EliteTableTileEntity>> ELITE_TABLE = register("elite_table", EliteTableTileEntity::new, () -> new Block[] { ModBlocks.ELITE_TABLE.get() });
	public static final RegistryObject<BlockEntityType<UltimateTableTileEntity>> ULTIMATE_TABLE = register("ultimate_table", UltimateTableTileEntity::new, () -> new Block[] { ModBlocks.ULTIMATE_TABLE.get() });
	public static final RegistryObject<BlockEntityType<AutoTableTileEntity.Basic>> BASIC_AUTO_TABLE = register("basic_auto_table", AutoTableTileEntity.Basic::new, () -> new Block[] { ModBlocks.BASIC_AUTO_TABLE.get() });
	public static final RegistryObject<BlockEntityType<AutoTableTileEntity.Advanced>> ADVANCED_AUTO_TABLE = register("advanced_auto_table", AutoTableTileEntity.Advanced::new, () -> new Block[] { ModBlocks.ADVANCED_AUTO_TABLE.get() });
	public static final RegistryObject<BlockEntityType<AutoTableTileEntity.Elite>> ELITE_AUTO_TABLE = register("elite_auto_table", AutoTableTileEntity.Elite::new, () -> new Block[] { ModBlocks.ELITE_AUTO_TABLE.get() });
	public static final RegistryObject<BlockEntityType<AutoTableTileEntity.Ultimate>> ULTIMATE_AUTO_TABLE = register("ultimate_auto_table", AutoTableTileEntity.Ultimate::new, () -> new Block[] { ModBlocks.ULTIMATE_AUTO_TABLE.get() });
	public static final RegistryObject<BlockEntityType<CompressorTileEntity>> COMPRESSOR = register("compressor", CompressorTileEntity::new, () -> new Block[] { ModBlocks.COMPRESSOR.get() });
	public static final RegistryObject<BlockEntityType<EnderCrafterTileEntity>> ENDER_CRAFTER = register("ender_crafter", EnderCrafterTileEntity::new, () -> new Block[] { ModBlocks.ENDER_CRAFTER.get() });

	@OnlyIn(Dist.CLIENT)
	public static void onClientSetup() {
		BlockEntityRenderers.register(PEDESTAL.get(), PedestalRenderer::new);
		BlockEntityRenderers.register(CRAFTING_CORE.get(), CraftingCoreRenderer::new);
		BlockEntityRenderers.register(COMPRESSOR.get(), CompressorRenderer::new);
	}

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> tile, Supplier<Block[]> blocks) {
		return REGISTRY.register(name, () -> BlockEntityType.Builder.of(tile, blocks.get()).build(null));
	}
}
