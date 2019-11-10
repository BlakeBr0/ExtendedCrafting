package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.config.ModConfig;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModTileEntities {
	public static final List<Supplier<TileEntityType<?>>> ENTRIES = new ArrayList<>();

	public static final RegistryObject<TileEntityType<PedestalTileEntity>> PEDESTAL = register("pedestal", PedestalTileEntity::new, () -> new Block[] { ModBlocks.PEDESTAL.get() });
	public static final RegistryObject<TileEntityType<CraftingCoreTileEntity>> CRAFTING_CORE = register("crafting_core", CraftingCoreTileEntity::new, () -> new Block[] { ModBlocks.CRAFTING_CORE.get() });

	public static final RegistryObject<TileEntityType<CompressorTileEntity>> COMPRESSOR = register("compressor", CompressorTileEntity::new, () -> new Block[] { ModBlocks.COMPRESSOR.get() });

	// TODO: 1.13, correct the namespaces
	public static void init() {

		if (ModConfig.confTableEnabled) {
			GameRegistry.registerTileEntity(TileBasicCraftingTable.class, "EC_Basic_Table");
			GameRegistry.registerTileEntity(TileAdvancedCraftingTable.class, "EC_Advanced_Table");
			GameRegistry.registerTileEntity(TileEliteCraftingTable.class, "EC_Elite_Table");
			GameRegistry.registerTileEntity(TileUltimateCraftingTable.class, "EC_Ultimate_Table");
		}

		
		if (ModConfig.confEnderEnabled) {
			GameRegistry.registerTileEntity(TileEnderCrafter.class, ExtendedCrafting.MOD_ID + "ender_crafter");
		}
	}

	private static <T extends TileEntityType<?>> RegistryObject<T> register(String name, Supplier<TileEntity> tile, Supplier<Block[]> blocks) {
		ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
		ENTRIES.add(() -> TileEntityType.Builder.create(tile, blocks.get()).build(null).setRegistryName(loc));
		return RegistryObject.of(loc, ForgeRegistries.TILE_ENTITIES);
	}
}
