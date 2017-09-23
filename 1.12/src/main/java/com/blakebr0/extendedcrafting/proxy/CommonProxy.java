package com.blakebr0.extendedcrafting.proxy;

import java.io.File;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.client.gui.GuiHandler;
import com.blakebr0.extendedcrafting.compat.crafttweaker.CombinationCrafting;
import com.blakebr0.extendedcrafting.compat.crafttweaker.CompressionCrafting;
import com.blakebr0.extendedcrafting.compat.crafttweaker.TableCrafting;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;
import com.blakebr0.extendedcrafting.crafting.ModRecipes;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.item.ModItems;
import com.blakebr0.extendedcrafting.tile.ModTiles;
import com.blakebr0.extendedcrafting.util.NetworkThingy;

import crafttweaker.CraftTweakerAPI;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@EventBusSubscriber
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModConfig.init(new File(event.getModConfigurationDirectory(), ExtendedCrafting.MOD_ID + ".cfg"));
		MinecraftForge.EVENT_BUS.register(new ModConfig());

		ModBlocks.init();
		ModItems.init();
		ModTiles.init();

		MinecraftForge.EVENT_BUS.register(ExtendedCrafting.REGISTRY);
		
		if (Loader.isModLoaded("crafttweaker")) {
			CraftTweakerAPI.registerClass(TableCrafting.class);
			CraftTweakerAPI.registerClass(CombinationCrafting.class);
			CraftTweakerAPI.registerClass(CompressionCrafting.class);
		}
	}

	public void init(FMLInitializationEvent event) {		
		NetworkThingy.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(ExtendedCrafting.instance, new GuiHandler());
		FMLInterModComms.sendMessage("waila", "register", "com.blakebr0.extendedcrafting.compat.WailaDataProvider.callbackRegister");
		//ModRecipes.init();
/*		CombinationRecipeManager.getInstance().addRecipe(new ItemStack(Items.DIAMOND), 50000, 100,
				new ItemStack(Items.COAL), new ItemStack(Items.STICK), new ItemStack(Items.LAVA_BUCKET), "ingotIron",
				new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT),
				"dyeRed", new ItemStack(Items.WHEAT));

		TableRecipeManager.getInstance().addShaped(new ItemStack(Items.DIAMOND), "XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX",
				"XXXXXXXXX", "XXXXXXXXX", 'X', new ItemStack(Items.DIAMOND));
		TableRecipeManager.getInstance().addShaped(4, new ItemStack(Items.CARROT), "X", 'X',
				new ItemStack(Items.WATER_BUCKET));
		TableRecipeManager.getInstance().addShapeless(new ItemStack(Items.ACACIA_DOOR), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL),
				new ItemStack(Items.COAL));
		TableRecipeManager.getInstance().addShapeless(1, new ItemStack(Items.APPLE), new ItemStack(Items.BEETROOT));

		CompressorRecipeManager.getInstance().addRecipe(new ItemStack(ModItems.itemSingularity, 1, 2),
				new ItemStack(Items.IRON_INGOT), 64, new ItemStack(Items.COAL), false, 1234567);
		CompressorRecipeManager.getInstance().addRecipe(new ItemStack(ModItems.itemSingularity, 1, 2),
				new ItemStack(Items.IRON_INGOT), 100, ItemStack.EMPTY, false, 100000);
		CompressorRecipeManager.getInstance().addRecipe(new ItemStack(ModItems.itemSingularity, 1, 2),
				new ItemStack(Items.IRON_INGOT), 2000, new ItemStack(Items.DIAMOND), false, 100000);

		CompressorRecipeManager.getInstance().addRecipe(new ItemStack(Blocks.ACACIA_FENCE), "sand", 50, ModItems.itemMaterial.itemUltimateCatalyst, false, 1000000);
*/	} // TODO: remember to remove this LUL

	public void postInit(FMLPostInitializationEvent event) {
		ModRecipes.post();
	}
	
	@SubscribeEvent // TODO: figure out why crafttweaker sucks
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		ModRecipes.init();
	}
}
