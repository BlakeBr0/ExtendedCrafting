package com.blakebr0.extendedcrafting.proxy;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.client.gui.GuiHandler;
import com.blakebr0.extendedcrafting.compat.crafttweaker.CombinationCrafting;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
import com.blakebr0.extendedcrafting.item.ModItems;
import com.blakebr0.extendedcrafting.tile.ModTiles;

import minetweaker.MineTweakerAPI;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event){
		ModBlocks.init();
		ModItems.init();
		ModTiles.init();
	}
	
	public void init(FMLInitializationEvent event){
		NetworkRegistry.INSTANCE.registerGuiHandler(ExtendedCrafting.instance, new GuiHandler());
		FMLInterModComms.sendMessage("Waila", "register", "com.blakebr0.extendedcrafting.compat.WailaDataProvider.callbackRegister");
		CombinationRecipeManager.addRecipe(new ItemStack(Items.DIAMOND), 50000, 100, new ItemStack(Items.COAL), new ItemStack(Items.STICK), new ItemStack(Items.LAVA_BUCKET), "ingotIron", new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT));
	} // TODO: remember to remove this LUL
	
	public void postInit(FMLPostInitializationEvent event){
		if(Loader.isModLoaded("MineTweaker3")){
	        MineTweakerAPI.registerClass(CombinationCrafting.class);
		}
	}
}
