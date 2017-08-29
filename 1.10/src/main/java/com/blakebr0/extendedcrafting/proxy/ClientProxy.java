package com.blakebr0.extendedcrafting.proxy;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.item.ModItems;
import com.blakebr0.extendedcrafting.render.ModRenders;
import com.blakebr0.extendedcrafting.render.RenderPedestal;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event){
		super.preInit(event);
		ModBlocks.initModels();
		ModItems.initModels();
	}
	
	@Override
	public void init(FMLInitializationEvent event){
		super.init(event);
		ModRenders.init();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event){
		super.postInit(event);
	}
}
