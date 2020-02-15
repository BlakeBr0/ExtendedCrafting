package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.item.BaseBlockItem;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blakebr0.extendedcrafting.ExtendedCrafting.ITEM_GROUP;

public class ModBlocks {
	public static final List<Supplier<? extends Block>> ENTRIES = new ArrayList<>();

	public static final RegistryObject<BaseBlock> LUMINESSENCE_BLOCK = register("luminessence_block", () -> new BaseBlock(Material.ROCK, SoundType.STONE, 5.0F, 10.0F));
	public static final RegistryObject<BaseBlock> BLACK_IRON_BLOCK = register("black_iron_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F));
	public static final RegistryObject<BaseBlock> REDSTONE_INGOT_BLOCK = register("redstone_ingot_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F));
	public static final RegistryObject<BaseBlock> ENDER_INGOT_BLOCK = register("ender_ingot_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F));
	public static final RegistryObject<BaseBlock> ENHANCED_ENDER_INGOT_BLOCK = register("enhanced_ender_ingot_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F));
	public static final RegistryObject<BaseBlock> CRYSTALTINE_BLOCK = register("crystaltine_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F));
	public static final RegistryObject<BaseBlock> THE_ULTIMATE_BLOCK = register("the_ultimate_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F));
	public static final RegistryObject<BaseBlock> NETHER_STAR_BLOCK = register("nether_star_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F));
	public static final RegistryObject<BaseBlock> ENDER_STAR_BLOCK = register("ender_star_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F));

	public static final RegistryObject<FrameBlock> FRAME = register("frame", FrameBlock::new);
	public static final RegistryObject<PedestalBlock> PEDESTAL = register("pedestal", PedestalBlock::new);
	public static final RegistryObject<CraftingCoreBlock> CRAFTING_CORE = register("crafting_core", CraftingCoreBlock::new);
	public static final RegistryObject<BasicTableBlock> BASIC_TABLE = register("basic_table", BasicTableBlock::new);
	public static final RegistryObject<AdvancedTableBlock> ADVANCED_TABLE = register("advanced_table", AdvancedTableBlock::new);
	public static final RegistryObject<EliteTableBlock> ELITE_TABLE = register("elite_table", EliteTableBlock::new);
	public static final RegistryObject<UltimateTableBlock> ULTIMATE_TABLE = register("ultimate_table", UltimateTableBlock::new);
	public static final RegistryObject<CompressorBlock> COMPRESSOR = register("compressor", CompressorBlock::new);
	public static final RegistryObject<EnderAlternatorBlock> ENDER_ALTERNATOR = register("ender_alternator", EnderAlternatorBlock::new);
	public static final RegistryObject<EnderCrafterBlock> ENDER_CRAFTER = register("ender_crafter", EnderCrafterBlock::new);

	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		ENTRIES.stream().map(Supplier::get).forEach(registry::register);
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		return register(name, block, b -> () -> new BaseBlockItem(b.get(), p -> p.group(ITEM_GROUP)));
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, Function<RegistryObject<T>, Supplier<? extends BlockItem>> item) {
		ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
		ENTRIES.add(() -> block.get().setRegistryName(loc));
		RegistryObject<T> reg = RegistryObject.of(loc, ForgeRegistries.BLOCKS);
		ModItems.BLOCK_ENTRIES.add(() -> item.apply(reg).get().setRegistryName(loc));
		return reg;
	}
}
