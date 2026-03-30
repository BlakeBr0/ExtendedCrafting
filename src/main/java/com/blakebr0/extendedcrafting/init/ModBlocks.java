package com.blakebr0.extendedcrafting.init;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.item.BaseBlockItem;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.AdvancedAutoTableBlock;
import com.blakebr0.extendedcrafting.block.AdvancedTableBlock;
import com.blakebr0.extendedcrafting.block.AutoEnderCrafterBlock;
import com.blakebr0.extendedcrafting.block.AutoFluxCrafterBlock;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class ModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Registries.BLOCK, ExtendedCrafting.MOD_ID);
	public static final Map<String, Function<Identifier, BlockItem>> BLOCK_ITEMS = new LinkedHashMap<>();

	public static final DeferredHolder<Block, Block> LUMINESSENCE_BLOCK = register("luminessence_block", id -> new BaseBlock(id, SoundType.STONE, 5.0F, 10.0F, true));
	public static final DeferredHolder<Block, Block> BLACK_IRON_BLOCK = register("black_iron_block", id -> new BaseBlock(id, SoundType.METAL, 5.0F, 10.0F, true));
	public static final DeferredHolder<Block, Block> REDSTONE_INGOT_BLOCK = register("redstone_ingot_block", id -> new BaseBlock(id, SoundType.METAL, 5.0F, 10.0F, true));
	public static final DeferredHolder<Block, Block> ENHANCED_REDSTONE_INGOT_BLOCK = register("enhanced_redstone_ingot_block", id -> new BaseBlock(id, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);
	public static final DeferredHolder<Block, Block> ENDER_INGOT_BLOCK = register("ender_ingot_block", id -> new BaseBlock(id, SoundType.METAL, 5.0F, 10.0F, true));
	public static final DeferredHolder<Block, Block> ENHANCED_ENDER_INGOT_BLOCK = register("enhanced_ender_ingot_block", id -> new BaseBlock(id, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);
	public static final DeferredHolder<Block, Block> CRYSTALTINE_BLOCK = register("crystaltine_block", id -> new BaseBlock(id, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);
	public static final DeferredHolder<Block, Block> THE_ULTIMATE_BLOCK = register("the_ultimate_block", TheUltimateBlock::new, Rarity.EPIC);
	public static final DeferredHolder<Block, Block> NETHER_STAR_BLOCK = register("nether_star_block", id -> new BaseBlock(id, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);
	public static final DeferredHolder<Block, Block> FLUX_STAR_BLOCK = register("flux_star_block", id -> new BaseBlock(id, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);
	public static final DeferredHolder<Block, Block> ENDER_STAR_BLOCK = register("ender_star_block", id -> new BaseBlock(id, SoundType.METAL, 5.0F, 10.0F, true), Rarity.UNCOMMON);

	public static final DeferredHolder<Block, Block> FRAME = register("frame", FrameBlock::new);
	public static final DeferredHolder<Block, Block> PEDESTAL = register("pedestal", PedestalBlock::new);
	public static final DeferredHolder<Block, Block> CRAFTING_CORE = register("crafting_core", CraftingCoreBlock::new);
	public static final DeferredHolder<Block, Block> BASIC_TABLE = register("basic_table", BasicTableBlock::new);
	public static final DeferredHolder<Block, Block> ADVANCED_TABLE = register("advanced_table", AdvancedTableBlock::new);
	public static final DeferredHolder<Block, Block> ELITE_TABLE = register("elite_table", EliteTableBlock::new);
	public static final DeferredHolder<Block, Block> ULTIMATE_TABLE = register("ultimate_table", UltimateTableBlock::new);
	public static final DeferredHolder<Block, Block> BASIC_AUTO_TABLE = register("basic_auto_table", BasicAutoTableBlock::new);
	public static final DeferredHolder<Block, Block> ADVANCED_AUTO_TABLE = register("advanced_auto_table", AdvancedAutoTableBlock::new);
	public static final DeferredHolder<Block, Block> ELITE_AUTO_TABLE = register("elite_auto_table", EliteAutoTableBlock::new);
	public static final DeferredHolder<Block, Block> ULTIMATE_AUTO_TABLE = register("ultimate_auto_table", UltimateAutoTableBlock::new);
	public static final DeferredHolder<Block, Block> COMPRESSOR = register("compressor", CompressorBlock::new);
	public static final DeferredHolder<Block, Block> ENDER_ALTERNATOR = register("ender_alternator", EnderAlternatorBlock::new);
	public static final DeferredHolder<Block, Block> ENDER_CRAFTER = register("ender_crafter", EnderCrafterBlock::new);
	public static final DeferredHolder<Block, Block> AUTO_ENDER_CRAFTER = register("auto_ender_crafter", AutoEnderCrafterBlock::new);
	public static final DeferredHolder<Block, Block> FLUX_ALTERNATOR = register("flux_alternator", FluxAlternatorBlock::new);
	public static final DeferredHolder<Block, Block> FLUX_CRAFTER = register("flux_crafter", FluxCrafterBlock::new);
	public static final DeferredHolder<Block, Block> AUTO_FLUX_CRAFTER = register("auto_flux_crafter", AutoFluxCrafterBlock::new);

	private static DeferredHolder<Block, Block> register(String name, Function<Identifier, Block> block) {
		return register(name, block, b -> id -> new BaseBlockItem(id, b.get()));
	}

	private static DeferredHolder<Block, Block> register(String name, Function<Identifier, Block> block, Rarity rarity) {
		return register(name, block, b -> id -> new BaseBlockItem(id, b.get(), p -> p.rarity(rarity)));
	}

	private static DeferredHolder<Block, Block> register(String name, Function<Identifier, Block> block, Function<DeferredHolder<Block, Block>, Function<Identifier, BlockItem>> item) {
		var reg = REGISTRY.register(name, block);
		BLOCK_ITEMS.put(name, id -> item.apply(reg).apply(id));
		return reg;
	}
}
