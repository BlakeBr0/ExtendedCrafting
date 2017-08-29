package com.blakebr0.extendedcrafting.block;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockAdvancedTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockBasicTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockEliteTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockUltimateTable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
	
	public static BlockStorage blockStorage = new BlockStorage();
	
	public static BlockPedestal blockPedestal = new BlockPedestal();
	public static BlockCraftingCore blockCraftingCore = new BlockCraftingCore();
	
	public static BlockBasicTable blockBasicTable = new BlockBasicTable();
	public static BlockAdvancedTable blockAdvancedTable = new BlockAdvancedTable();
	public static BlockEliteTable blockEliteTable = new BlockEliteTable();
	public static BlockUltimateTable blockUltimateTable = new BlockUltimateTable();
	
	public static void init(){
		blockStorage.init();
		
		register(blockPedestal);
		register(blockCraftingCore);
		
		register(blockBasicTable);
		register(blockAdvancedTable);
		register(blockEliteTable);
		register(blockUltimateTable);
	}
	
	public static void register(Block block){
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels(){
		blockStorage.initModels();
		
		registerModel(blockPedestal);
		registerModel(blockCraftingCore);
		
		registerModel(blockBasicTable);
		registerModel(blockAdvancedTable);
		registerModel(blockEliteTable);
		registerModel(blockUltimateTable);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerModel(Block block){
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(ExtendedCrafting.MOD_ID + ":" + block.getUnlocalizedName().substring(8), "inventory"));	
	}
}
