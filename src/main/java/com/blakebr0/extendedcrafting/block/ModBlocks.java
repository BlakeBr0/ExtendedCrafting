package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.cucumber.registry.Ore;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.automationinterface.BlockAdvancedInterface;
import com.blakebr0.extendedcrafting.block.automationinterface.BlockBasicInterface;
import com.blakebr0.extendedcrafting.block.automationinterface.BlockCrystaltineInterface;
import com.blakebr0.extendedcrafting.block.automationinterface.BlockEliteInterface;
import com.blakebr0.extendedcrafting.block.automationinterface.BlockUltimateInterface;
import com.blakebr0.extendedcrafting.block.automationinterface.BlockUltimaterInterface;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockAdvancedTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockBasicTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockEliteTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockUltimateTable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

public class ModBlocks {

	public static BlockStorage blockStorage = new BlockStorage();
	public static BlockFrame blockFrame = new BlockFrame();
	public static BlockLamp blockLamp = new BlockLamp();
	public static BlockTrimmed blockTrimmed = new BlockTrimmed();

	public static BlockPedestal blockPedestal = new BlockPedestal();
	public static BlockCraftingCore blockCraftingCore = new BlockCraftingCore();
	
	public static BlockBasic blockCraftingTable = new BlockBasic("ec.crafting_table", Material.WOOD, SoundType.WOOD, 2.5F, 10.0F);
	
	public static BlockBasicInterface blockBasicInterface = new BlockBasicInterface();
	public static BlockAdvancedInterface blockAdvancedInterface = new BlockAdvancedInterface();
	public static BlockEliteInterface blockEliteInterface = new BlockEliteInterface();
	public static BlockUltimateInterface blockUltimateInterface = new BlockUltimateInterface();
	public static BlockCrystaltineInterface blockCrystaltineInterface = new BlockCrystaltineInterface();
	public static BlockUltimaterInterface blockUltimaterInterface = new BlockUltimaterInterface();

	public static BlockBasicTable blockBasicTable = new BlockBasicTable();
	public static BlockAdvancedTable blockAdvancedTable = new BlockAdvancedTable();
	public static BlockEliteTable blockEliteTable = new BlockEliteTable();
	public static BlockUltimateTable blockUltimateTable = new BlockUltimateTable();

	public static BlockCompressor blockCompressor = new BlockCompressor();
	
	public static BlockEnderAlternator blockEnderAlternator = new BlockEnderAlternator();
	public static BlockEnderCrafter blockEnderCrafter = new BlockEnderCrafter();

	public static void init() {
		final ModRegistry registry = ExtendedCrafting.REGISTRY;

		registry.register(blockStorage, "storage", new ItemBlockStorage(blockStorage), 
				Ore.of(0, "blockBlackIron"), Ore.of(1, "blockLuminessence"), Ore.of(2, "blockNetherStar"),
				Ore.of(3, "blockCrystaltine"), Ore.of(4, "blockUltimate"));
		registry.register(blockFrame, "frame");
		registry.register(blockLamp, "lamp", new ItemBlockLamp(blockLamp));
		registry.register(blockTrimmed, "trimmed", new ItemBlockTrimmed(blockTrimmed));
		
		registry.register(blockCraftingTable, "crafting_table");

		registry.register(blockPedestal, "pedestal");
		registry.register(blockCraftingCore, "crafting_core");

		registry.register(blockBasicInterface, "interface");
		registry.register(blockBasicInterface, "interface_basic");
		registry.register(blockAdvancedInterface, "interface_advanced");
		registry.register(blockEliteInterface, "interface_elite");
		registry.register(blockUltimateInterface, "interface_ultimate");
		registry.register(blockCrystaltineInterface, "interface_crystaltine");
		registry.register(blockUltimaterInterface, "interface_ultimater");
		
		registry.register(blockBasicTable, "table_basic");
		registry.register(blockAdvancedTable, "table_advanced");
		registry.register(blockEliteTable, "table_elite");
		registry.register(blockUltimateTable, "table_ultimate");

		registry.register(blockCompressor, "compressor");
		
		registry.register(blockEnderAlternator, "ender_alternator");
		registry.register(blockEnderCrafter, "ender_crafter");
	}
}
