package com.blakebr0.extendedcrafting;

import com.blakebr0.cucumber.helper.ConfigHelper;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.client.ModelHandler;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.ModContainerTypes;
import com.blakebr0.extendedcrafting.crafting.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.item.ModItems;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.tileentity.ModTileEntities;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExtendedCrafting.MOD_ID)
public class ExtendedCrafting {
	public static final String MOD_ID = "extendedcrafting";
	public static final String NAME = "Extended Crafting";

	public static final ItemGroup ITEM_GROUP = new ECItemGroup();

	public ExtendedCrafting() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.register(this);
		bus.register(new ModBlocks());
		bus.register(new ModItems());
		bus.register(new ModRecipeSerializers());
		bus.register(new ModTileEntities());
		bus.register(new ModContainerTypes());

		ConfigHelper.load(ModConfigs.COMMON, "extendedcrafting-common.toml");
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		DeferredWorkQueue.runLater(() -> {
			NetworkHandler.onCommonSetup();
		});
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		ModelHandler.onClientSetup();

		ModContainerTypes.onClientSetup();
	}
}
