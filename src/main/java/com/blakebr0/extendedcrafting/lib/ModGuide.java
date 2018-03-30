package com.blakebr0.extendedcrafting.lib;

import com.blakebr0.cucumber.guide.Guide;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.item.ModItems;

import net.minecraft.item.ItemStack;

public class ModGuide {

	public static final Guide GUIDE = Guide.create("extendedcrafting", "BlakeBr0", 0x808080);
	
	public static void setup() {
		GUIDE.addEntry("introduction")
				.setIconStack(new ItemStack(ModItems.itemGuide))
				.addPageText();
		
		GUIDE.addEntry("combination_crafting")
				.setIconStack(new ItemStack(ModBlocks.blockCraftingCore))
				.addPageText()
				.addPageText();
		
		GUIDE.addEntry("table_crafting")
				.setIconStack(new ItemStack(ModBlocks.blockUltimateTable))
				.addPageText();
		
		GUIDE.addEntry("quantum_compression")
				.setIconStack(new ItemStack(ModBlocks.blockCompressor))
				.addPageText()
				.addPageText();
		
		GUIDE.addEntry("ender_crafting")
				.setIconStack(new ItemStack(ModBlocks.blockEnderCrafter))
				.addPageText();
		
		GUIDE.addEntry("interface")
				.setIconStack(new ItemStack(ModBlocks.blockAutomationInterface))
				.addPageText()
				.addPageText()
				.addPageText();
		
		GUIDE.addEntry("singularity")
				.setIconStack(new ItemStack(ModItems.itemSingularityUltimate))
				.addPageText();
	}
}
