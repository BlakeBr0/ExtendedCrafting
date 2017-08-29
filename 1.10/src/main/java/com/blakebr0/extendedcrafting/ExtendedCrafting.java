package com.blakebr0.extendedcrafting;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ExtendedCrafting.MOD_ID, name = ExtendedCrafting.NAME, version = ExtendedCrafting.VERSION)
public class ExtendedCrafting {
	
	public static final String MOD_ID = "extendedcrafting";
	public static final String NAME = "Extended Crafting";
	public static final String VERSION = "1.0.0";
	
	@SidedProxy(clientSide = "com.blakebr0.extendedcrafting.proxy.ClientProxy",
				serverSide = "com.blakebr0.extendedcrafting.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	@Instance(ExtendedCrafting.MOD_ID)
	public static ExtendedCrafting instance = new ExtendedCrafting();
	
	public static CreativeTabs tabExtendedCrafting = new CreativeTabs("ec.extended_crafting"){
		@Override
		public Item getTabIconItem(){
			return new ItemStack(ModBlocks.blockCraftingCore).getItem();
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}
}
