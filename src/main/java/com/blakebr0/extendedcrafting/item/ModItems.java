package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.extendedcrafting.ExtendedCrafting;

public class ModItems {

	public static ItemMaterial itemMaterial = new ItemMaterial();

	public static ItemHandheldTable itemHandheldTable = new ItemHandheldTable();
	
	public static ItemRecipeMaker itemRecipeMaker = new ItemRecipeMaker();

	public static ItemSingularity itemSingularity = new ItemSingularity();
	public static ItemSingularityCustom itemSingularityCustom = new ItemSingularityCustom();
	public static ItemSingularityUltimate itemSingularityUltimate = new ItemSingularityUltimate();

	public static void init() {
		final ModRegistry registry = ExtendedCrafting.REGISTRY;

		registry.register(itemMaterial, "material");

		registry.register(itemHandheldTable, "handheld_table");
		
		registry.register(itemRecipeMaker, "recipe_maker");

		registry.register(itemSingularity, "singularity");
		registry.register(itemSingularityCustom, "singularity_custom");
		registry.register(itemSingularityUltimate, "singularity_ultimate");
	}
}
