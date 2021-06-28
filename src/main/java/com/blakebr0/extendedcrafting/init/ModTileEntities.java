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
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class ModTileEntities {
	public static final List<Supplier<? extends TileEntityType<?>>> ENTRIES = new ArrayList<>();

	public static final RegistryObject<TileEntityType<PedestalTileEntity>> PEDESTAL = register("pedestal", PedestalTileEntity::new, () -> new Block[] { ModBlocks.PEDESTAL.get() });
	public static final RegistryObject<TileEntityType<CraftingCoreTileEntity>> CRAFTING_CORE = register("crafting_core", CraftingCoreTileEntity::new, () -> new Block[] { ModBlocks.CRAFTING_CORE.get() });
	public static final RegistryObject<TileEntityType<BasicTableTileEntity>> BASIC_TABLE = register("basic_table", BasicTableTileEntity::new, () -> new Block[] { ModBlocks.BASIC_TABLE.get() });
	public static final RegistryObject<TileEntityType<AdvancedTableTileEntity>> ADVANCED_TABLE = register("advanced_table", AdvancedTableTileEntity::new, () -> new Block[] { ModBlocks.ADVANCED_TABLE.get() });
	public static final RegistryObject<TileEntityType<EliteTableTileEntity>> ELITE_TABLE = register("elite_table", EliteTableTileEntity::new, () -> new Block[] { ModBlocks.ELITE_TABLE.get() });
	public static final RegistryObject<TileEntityType<UltimateTableTileEntity>> ULTIMATE_TABLE = register("ultimate_table", UltimateTableTileEntity::new, () -> new Block[] { ModBlocks.ULTIMATE_TABLE.get() });
	public static final RegistryObject<TileEntityType<AutoTableTileEntity.Basic>> BASIC_AUTO_TABLE = register("basic_auto_table", AutoTableTileEntity.Basic::new, () -> new Block[] { ModBlocks.BASIC_AUTO_TABLE.get() });
	public static final RegistryObject<TileEntityType<AutoTableTileEntity.Advanced>> ADVANCED_AUTO_TABLE = register("advanced_auto_table", AutoTableTileEntity.Advanced::new, () -> new Block[] { ModBlocks.ADVANCED_AUTO_TABLE.get() });
	public static final RegistryObject<TileEntityType<AutoTableTileEntity.Elite>> ELITE_AUTO_TABLE = register("elite_auto_table", AutoTableTileEntity.Elite::new, () -> new Block[] { ModBlocks.ELITE_AUTO_TABLE.get() });
	public static final RegistryObject<TileEntityType<AutoTableTileEntity.Ultimate>> ULTIMATE_AUTO_TABLE = register("ultimate_auto_table", AutoTableTileEntity.Ultimate::new, () -> new Block[] { ModBlocks.ULTIMATE_AUTO_TABLE.get() });
	public static final RegistryObject<TileEntityType<CompressorTileEntity>> COMPRESSOR = register("compressor", CompressorTileEntity::new, () -> new Block[] { ModBlocks.COMPRESSOR.get() });
	public static final RegistryObject<TileEntityType<EnderCrafterTileEntity>> ENDER_CRAFTER = register("ender_crafter", EnderCrafterTileEntity::new, () -> new Block[] { ModBlocks.ENDER_CRAFTER.get() });

	@SubscribeEvent
	public void onRegisterTypes(RegistryEvent.Register<TileEntityType<?>> event) {
		IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();

		ENTRIES.stream().map(Supplier::get).forEach(registry::register);
	}

	@OnlyIn(Dist.CLIENT)
	public static void onClientSetup() {
		ClientRegistry.bindTileEntityRenderer(PEDESTAL.get(), PedestalRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CRAFTING_CORE.get(), CraftingCoreRenderer::new);
		ClientRegistry.bindTileEntityRenderer(COMPRESSOR.get(), CompressorRenderer::new);
	}

	private static <T extends TileEntityType<?>> RegistryObject<T> register(String name, Supplier<TileEntity> tile, Supplier<Block[]> blocks) {
		ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
		ENTRIES.add(() -> TileEntityType.Builder.of(tile, blocks.get()).build(null).setRegistryName(loc));
		return RegistryObject.of(loc, ForgeRegistries.TILE_ENTITIES);
	}
}
