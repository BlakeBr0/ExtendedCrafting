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
import com.blakebr0.extendedcrafting.block.FluxAlternatorBlock;
import com.blakebr0.extendedcrafting.block.FluxCrafterBlock;
import com.blakebr0.extendedcrafting.block.FrameBlock;
import com.blakebr0.extendedcrafting.block.PedestalBlock;
import com.blakebr0.extendedcrafting.block.TheUltimateBlock;
import com.blakebr0.extendedcrafting.block.UltimateAutoTableBlock;
import com.blakebr0.extendedcrafting.block.UltimateTableBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blakebr0.extendedcrafting.ExtendedCrafting.CREATIVE_TAB;

public final class ModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ExtendedCrafting.MOD_ID);
	public static final Map<String, Supplier<BlockItem>> BLOCK_ITEMS = new LinkedHashMap<>();

	public static final RegistryObject<Block> LUMINESSENCE_BLOCK = register("luminessence_block", () -> new BaseBlock(Material.STONE, SoundType.STONE, 5.0F, 10.0F, true));
	public static final RegistryObject<Block> BLACK_IRON_BLOCK = register("black_iron_block", () -> new BaseBlock(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true));
	public static final RegistryObject<Block> REDSTONE_INGOT_BLOCK = register("redstone_ingot_block", () -> new BaseBlock(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true));
	public static final RegistryObject<Block> ENHANCED_REDSTONE_INGOT_BLOCK = register("enhanced_redstone_ingot_block", () -> new BaseBlock(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);
	public static final RegistryObject<Block> ENDER_INGOT_BLOCK = register("ender_ingot_block", () -> new BaseBlock(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true));
	public static final RegistryObject<Block> ENHANCED_ENDER_INGOT_BLOCK = register("enhanced_ender_ingot_block", () -> new BaseBlock(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);
	public static final RegistryObject<Block> CRYSTALTINE_BLOCK = register("crystaltine_block", () -> new BaseBlock(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);
	public static final RegistryObject<Block> THE_ULTIMATE_BLOCK = register("the_ultimate_block", TheUltimateBlock::new, Rarity.EPIC);
	public static final RegistryObject<Block> NETHER_STAR_BLOCK = register("nether_star_block", () -> new BaseBlock(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);
	public static final RegistryObject<Block> FLUX_STAR_BLOCK = register("flux_star_block", () -> new BaseBlock(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);
	public static final RegistryObject<Block> ENDER_STAR_BLOCK = register("ender_star_block", () -> new BaseBlock(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);

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
	public static final RegistryObject<Block> FLUX_ALTERNATOR = register("flux_alternator", FluxAlternatorBlock::new);
	public static final RegistryObject<Block> FLUX_CRAFTER = register("flux_crafter", FluxCrafterBlock::new);

	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, b -> () -> new BaseBlockItem(b.get(), p -> p.tab(CREATIVE_TAB)));
	}

	private static RegistryObject<Block> register(String name, Supplier<Block> block, Rarity rarity) {
		return register(name, block, b -> () -> new BaseBlockItem(b.get(), p -> p.tab(CREATIVE_TAB).rarity(rarity)));
	}

	private static RegistryObject<Block> register(String name, Supplier<Block> block, Function<RegistryObject<Block>, Supplier<? extends BlockItem>> item) {
		var reg = REGISTRY.register(name, block);
		BLOCK_ITEMS.put(name, () -> item.apply(reg).get());
		return reg;
	}
}
