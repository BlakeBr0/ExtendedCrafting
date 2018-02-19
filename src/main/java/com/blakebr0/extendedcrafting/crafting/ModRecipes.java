package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.item.ModItems;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModRecipes {
	
	public static void init() {
		TableRecipeManager.getInstance().addShaped(ModItems.itemMaterial.itemCrystaltineIngot,
				"DLLLLLD", "DNIGIND", "DNIGIND", "DLLLLLD", 
				'D', "gemDiamond", 'L', new ItemStack(Items.DYE, 1, 4), 'N', ModItems.itemMaterial.itemNetherStarNugget, 'I', "ingotIron", 'G', "ingotGold");

		EnderCrafterRecipeManager.getInstance().addShaped(ModItems.itemMaterial.itemEnderStar, " E ", "ENE", " E ", 'E', Items.ENDER_EYE, 'N', Items.NETHER_STAR);
		EnderCrafterRecipeManager.getInstance().addShaped(new ItemStack(ModItems.itemMaterial, 4, 48), " I ", "INI", " I ", 'I', "ingotEnder", 'N', ModItems.itemMaterial.itemEnderStar);
		
		ModItems.itemSingularity.initRecipes();
		ModItems.itemSingularityUltimate.initRecipe();
	}
	
	public static void post() {
		ModItems.itemSingularityCustom.initRecipes();
	}
}
