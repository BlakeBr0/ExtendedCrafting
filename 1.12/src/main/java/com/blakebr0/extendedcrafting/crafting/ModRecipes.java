package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.item.ModItems;

import net.minecraft.init.Items;

public class ModRecipes {
	
	public static void init() {
		// Crystaltine Ingot
		TableRecipeManager.getInstance().addShaped(ModItems.itemMaterial.itemCrystaltineIngot,
				"DLLLLLD", "DNIGIND", "DNIGIND", "DLLLLLD", 
				'D', "gemDiamond", 'L', StackHelper.to(Items.DYE, 1, 4), 'N', ModItems.itemMaterial.itemNetherStarNugget, 'I', "ingotIron", 'G', "ingotGold");
		// TODO: Ultimate Ingot
//		TableRecipeManager.getInstance().addShaped(ModItems.itemMaterial.itemUltimateIngot, 
//				"XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX");

		ModItems.itemSingularity.initRecipes();
		ModItems.itemSingularityUltimate.initRecipe();
	}
	
	public static void post() {
		ModItems.itemSingularityCustom.initRecipes();
	}
}
