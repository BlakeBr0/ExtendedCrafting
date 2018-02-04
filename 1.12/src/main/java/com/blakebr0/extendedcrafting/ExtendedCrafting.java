package com.blakebr0.extendedcrafting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ExtendedCrafting.MOD_ID, name = ExtendedCrafting.NAME, version = ExtendedCrafting.VERSION, dependencies = ExtendedCrafting.DEPENDENCIES, guiFactory = ExtendedCrafting.GUI_FACTORY)
public class ExtendedCrafting {

	public static final String MOD_ID = "extendedcrafting";
	public static final String NAME = "Extended Crafting";
	public static final String VERSION = "${version}";
	public static final String GUI_FACTORY = "com.blakebr0.extendedcrafting.config.GuiFactory";
	public static final String DEPENDENCIES = "required-after:cucumber@[1.0.4,)";

	public static final ModRegistry REGISTRY = ModRegistry.create(MOD_ID);
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@SidedProxy(clientSide = "com.blakebr0.extendedcrafting.proxy.ClientProxy", serverSide = "com.blakebr0.extendedcrafting.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Instance(ExtendedCrafting.MOD_ID)
	public static ExtendedCrafting instance = new ExtendedCrafting();

	public static CreativeTabs tabExtendedCrafting = new CreativeTabs("ec.extended_crafting") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModBlocks.blockCraftingCore);
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
