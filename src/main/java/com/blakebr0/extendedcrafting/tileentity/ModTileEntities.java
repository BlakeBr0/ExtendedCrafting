package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.client.tesr.PedestalRenderer;
import com.blakebr0.extendedcrafting.client.tesr.CompressorRenderer;
import com.blakebr0.extendedcrafting.client.tesr.CraftingCoreRenderer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModTileEntities {
	public static final List<Supplier<? extends TileEntityType<?>>> ENTRIES = new ArrayList<>();

	public static final RegistryObject<TileEntityType<PedestalTileEntity>> PEDESTAL = register("pedestal", PedestalTileEntity::new, () -> new Block[] { ModBlocks.PEDESTAL.get() });
	public static final RegistryObject<TileEntityType<CraftingCoreTileEntity>> CRAFTING_CORE = register("crafting_core", CraftingCoreTileEntity::new, () -> new Block[] { ModBlocks.CRAFTING_CORE.get() });
	public static final RegistryObject<TileEntityType<BasicTableTileEntity>> BASIC_TABLE = register("basic_table", BasicTableTileEntity::new, () -> new Block[] { ModBlocks.BASIC_TABLE.get() });
	public static final RegistryObject<TileEntityType<AdvancedTableTileEntity>> ADVANCED_TABLE = register("advanced_table", AdvancedTableTileEntity::new, () -> new Block[] { ModBlocks.ADVANCED_TABLE.get() });
	public static final RegistryObject<TileEntityType<EliteTableTileEntity>> ELITE_TABLE = register("elite_table", EliteTableTileEntity::new, () -> new Block[] { ModBlocks.ELITE_TABLE.get() });
	public static final RegistryObject<TileEntityType<UltimateTableTileEntity>> ULTIMATE_TABLE = register("ultimate_table", UltimateTableTileEntity::new, () -> new Block[] { ModBlocks.ULTIMATE_TABLE.get() });
	public static final RegistryObject<TileEntityType<CompressorTileEntity>> COMPRESSOR = register("compressor", CompressorTileEntity::new, () -> new Block[] { ModBlocks.COMPRESSOR.get() });
	public static final RegistryObject<TileEntityType<EnderCrafterTileEntity>> ENDER_CRAFTER = register("ender_crafter", EnderCrafterTileEntity::new,  () -> new Block[] { ModBlocks.ENDER_CRAFTER.get() });

	@OnlyIn(Dist.CLIENT)
	public static void onClientSetup() {
		ClientRegistry.bindTileEntityRenderer(PEDESTAL.get(), PedestalRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CRAFTING_CORE.get(), CraftingCoreRenderer::new);
		ClientRegistry.bindTileEntityRenderer(COMPRESSOR.get(), CompressorRenderer::new);
	}

	private static <T extends TileEntityType<?>> RegistryObject<T> register(String name, Supplier<TileEntity> tile, Supplier<Block[]> blocks) {
		ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
		ENTRIES.add(() -> TileEntityType.Builder.create(tile, blocks.get()).build(null).setRegistryName(loc));
		return RegistryObject.of(loc, ForgeRegistries.TILE_ENTITIES);
	}
}
