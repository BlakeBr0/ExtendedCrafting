package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.guide.ItemGuide;
import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.lib.ModGuide;

public class ModItems {
	
	public static ItemGuide itemGuide = new ItemGuide("extendedcrafting", ExtendedCrafting.CREATIVE_TAB, ModGuide.GUIDE);

	public static ItemMaterial itemMaterial = new ItemMaterial();

	public static ItemHandheldTable itemHandheldTable = new ItemHandheldTable();
	
	public static ItemRecipeMaker itemRecipeMaker = new ItemRecipeMaker();

	public static ItemSingularity itemSingularity = new ItemSingularity();
	public static ItemSingularityCustom itemSingularityCustom = new ItemSingularityCustom();
	public static ItemSingularityUltimate itemSingularityUltimate = new ItemSingularityUltimate();

	public static void init() {
		final ModRegistry registry = ExtendedCrafting.REGISTRY;
		
		registry.register(itemGuide, "guide");

		registry.register(itemMaterial, "material");

		registry.register(itemHandheldTable, "handheld_table");
		
		registry.register(itemRecipeMaker, "recipe_maker");

		registry.register(itemSingularity, "singularity");
		registry.register(itemSingularityCustom, "singularity_custom");
		registry.register(itemSingularityUltimate, "singularity_ultimate");
	}
}
