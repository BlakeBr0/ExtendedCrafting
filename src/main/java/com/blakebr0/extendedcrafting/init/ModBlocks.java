package com.blakebr0.extendedcrafting.init;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.item.BaseBlockItem;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.AdvancedAutoTableBlock;
import com.blakebr0.extendedcrafting.block.AdvancedTableBlock;
import com.blakebr0.extendedcrafting.block.BasicAutoTableBlock;
import com.blakebr0.extendedcrafting.block.BasicTableBlock;
import com.blakebr0.extendedcrafting.block.CompressorBlock;
import com.blakebr0.extendedcrafting.block.CraftingCoreBlock;
import com.blakebr0.extendedcrafting.block.EliteAutoTableBlock;
import com.blakebr0.extendedcrafting.block.EliteTableBlock;
import com.blakebr0.extendedcrafting.block.EnderAlternatorBlock;
import com.blakebr0.extendedcrafting.block.EnderCrafterBlock;
import com.blakebr0.extendedcrafting.block.FrameBlock;
import com.blakebr0.extendedcrafting.block.PedestalBlock;
import com.blakebr0.extendedcrafting.block.UltimateAutoTableBlock;
import com.blakebr0.extendedcrafting.block.UltimateTableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blakebr0.extendedcrafting.ExtendedCrafting.ITEM_GROUP;

public final class ModBlocks {
	public static final Map<RegistryObject<Block>, Supplier<Block>> ENTRIES = new LinkedHashMap<>();

	public static final RegistryObject<Block> LUMINESSENCE_BLOCK = register("luminessence_block", () -> new BaseBlock(Material.ROCK, SoundType.STONE, 5.0F, 10.0F, ToolType.PICKAXE));
	public static final RegistryObject<Block> BLACK_IRON_BLOCK = register("black_iron_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE));
	public static final RegistryObject<Block> REDSTONE_INGOT_BLOCK = register("redstone_ingot_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE));
	public static final RegistryObject<Block> ENDER_INGOT_BLOCK = register("ender_ingot_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE));
	public static final RegistryObject<Block> ENHANCED_ENDER_INGOT_BLOCK = register("enhanced_ender_ingot_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE));
	public static final RegistryObject<Block> CRYSTALTINE_BLOCK = register("crystaltine_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE));
	public static final RegistryObject<Block> THE_ULTIMATE_BLOCK = register("the_ultimate_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE));
	public static final RegistryObject<Block> NETHER_STAR_BLOCK = register("nether_star_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE));
	public static final RegistryObject<Block> ENDER_STAR_BLOCK = register("ender_star_block", () -> new BaseBlock(Material.IRON, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE));

	public static final RegistryObject<Block> FRAME = register("frame", FrameBlock::new);
	public static final RegistryObject<Block> PEDESTAL = register("pedestal", PedestalBlock::new);
	public static final RegistryObject<Block> CRAFTING_CORE = register("crafting_core", CraftingCoreBlock::new);
	public static final RegistryObject<Block> BASIC_TABLE = register("basic_table", BasicTableBlock::new);
	public static final RegistryObject<Block> ADVANCED_TABLE = register("advanced_table", AdvancedTableBlock::new);
	public static final RegistryObject<Block> ELITE_TABLE = register("elite_table", EliteTableBlock::new);
	public static final RegistryObject<Block> ULTIMATE_TABLE = register("ultimate_table", UltimateTableBlock::new);
	public static final RegistryObject<Block> BASIC_AUTO_TABLE = register("basic_auto_table", BasicAutoTableBlock::new);
	public static final RegistryObject<Block> ADVANCED_AUTO_TABLE = register("advanced_auto_table", AdvancedAutoTableBlock::new);
	public static final RegistryObject<Block> ELITE_AUTO_TABLE = register("elite_auto_table", EliteAutoTableBlock::new);
	public static final RegistryObject<Block> ULTIMATE_AUTO_TABLE = register("ultimate_auto_table", UltimateAutoTableBlock::new);
	public static final RegistryObject<Block> COMPRESSOR = register("compressor", CompressorBlock::new);
	public static final RegistryObject<Block> ENDER_ALTERNATOR = register("ender_alternator", EnderAlternatorBlock::new);
	public static final RegistryObject<Block> ENDER_CRAFTER = register("ender_crafter", EnderCrafterBlock::new);

	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		ENTRIES.forEach((reg, block) -> {
			registry.register(block.get());
			reg.updateReference(registry);
		});
	}

	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, b -> () -> new BaseBlockItem(b.get(), p -> p.group(ITEM_GROUP)));
	}

	private static RegistryObject<Block> register(String name, Supplier<Block> block, Function<RegistryObject<Block>, Supplier<? extends BlockItem>> item) {
		ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
		RegistryObject<Block> reg = RegistryObject.of(loc, ForgeRegistries.BLOCKS);
		ENTRIES.put(reg, () -> block.get().setRegistryName(loc));
		ModItems.BLOCK_ENTRIES.add(() -> item.apply(reg).get().setRegistryName(loc));
		return reg;
	}
}
