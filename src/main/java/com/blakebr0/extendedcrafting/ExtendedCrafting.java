package com.blakebr0.extendedcrafting;

import com.blakebr0.cucumber.helper.ConfigHelper;
import com.blakebr0.extendedcrafting.client.ModelHandler;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.DynamicRecipeManager;
import com.blakebr0.extendedcrafting.handler.ColorHandler;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.init.ModContainerTypes;
import com.blakebr0.extendedcrafting.init.ModItems;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.init.ModReloadListeners;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ExtendedCrafting.MOD_ID)
public final class ExtendedCrafting {
	public static final String MOD_ID = "extendedcrafting";
	public static final String NAME = "Extended Crafting";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	public static final CreativeModeTab CREATIVE_TAB = new ECCreativeTab();

	public ExtendedCrafting() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.register(this);
		bus.register(new ModRecipeSerializers());

		ModBlocks.REGISTRY.register(bus);
		ModItems.REGISTRY.register(bus);
		ModTileEntities.REGISTRY.register(bus);
		ModContainerTypes.REGISTRY.register(bus);
		ModRecipeSerializers.REGISTRY.register(bus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.register(new ColorHandler());
		});

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON);

		ConfigHelper.load(ModConfigs.COMMON, "extendedcrafting-common.toml");
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new ModReloadListeners());
		MinecraftForge.EVENT_BUS.register(DynamicRecipeManager.getInstance());
		MinecraftForge.EVENT_BUS.register(SingularityRegistry.getInstance());

		event.enqueueWork(() -> {
			NetworkHandler.onCommonSetup();
		});

		SingularityRegistry.getInstance().writeDefaultSingularityFiles();
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		ModelHandler.onClientSetup();

		ModTileEntities.onClientSetup();
		ModContainerTypes.onClientSetup();
	}
}
