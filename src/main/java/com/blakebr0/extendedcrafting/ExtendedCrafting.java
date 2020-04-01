package com.blakebr0.extendedcrafting;

import com.blakebr0.cucumber.helper.ConfigHelper;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.client.ModelHandler;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.ModContainerTypes;
import com.blakebr0.extendedcrafting.crafting.DynamicRecipeManager;
import com.blakebr0.extendedcrafting.crafting.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.handler.ColorHandler;
import com.blakebr0.extendedcrafting.item.ModItems;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.tileentity.ModTileEntities;
import net.minecraft.item.ItemGroup;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
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

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON);

		ConfigHelper.load(ModConfigs.COMMON, "extendedcrafting-common.toml");
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(this);

		DeferredWorkQueue.runLater(() -> {
			NetworkHandler.onCommonSetup();
			SingularityRegistry.getInstance().loadSingularities();
			ModRecipeSerializers.initUltimateSingularityRecipe();
		});
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		ColorHandler.onClientSetup();
		ModelHandler.onClientSetup();

		ModTileEntities.onClientSetup();
		ModContainerTypes.onClientSetup();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		IReloadableResourceManager manager = event.getServer().getResourceManager();

		manager.addReloadListener(new DynamicRecipeManager());
	}
}
