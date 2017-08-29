package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.extendedcrafting.item.ModItems;

public class ModRecipes {
	
	public static void init() {
		ModItems.itemSingularity.initRecipes();
		ModItems.itemSingularityUltimate.initRecipe();
	}
	
	public static void post() {
		ModItems.itemSingularityCustom.initRecipes();
	}
}
