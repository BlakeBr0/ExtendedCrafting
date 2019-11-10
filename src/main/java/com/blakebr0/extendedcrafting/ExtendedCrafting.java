package com.blakebr0.extendedcrafting;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.crafting.ExtendedRecipeManager;
import com.blakebr0.extendedcrafting.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExtendedCrafting.MOD_ID)
public class ExtendedCrafting {
	public static final String MOD_ID = "extendedcrafting";
	public static final String NAME = "Extended Crafting";

	public static final Logger LOGGER = LogManager.getLogger(NAME);
	public static final ItemGroup ITEM_GROUP = new ECItemGroup();

	public ExtendedCrafting() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.register(this);
		bus.register(new ModBlocks());
		bus.register(new ModItems());
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(ExtendedRecipeManager.getInstance());
	}

	@SubscribeEvent
	public void onInterModEnqueue(InterModEnqueueEvent event) {

	}

	@SubscribeEvent
	public void onInterModProcess(InterModProcessEvent event) {

	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {

	}
}
