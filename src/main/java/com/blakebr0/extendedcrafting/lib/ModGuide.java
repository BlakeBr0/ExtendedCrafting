package com.blakebr0.extendedcrafting.lib;

import com.blakebr0.cucumber.guide.Guide;
import com.blakebr0.extendedcrafting.item.ModItems;

import net.minecraft.item.ItemStack;

public class ModGuide {

	public static final Guide GUIDE = Guide.create("guide.extendedcrafting.name", "BlakeBr0", 0x808080);
	
	public static void setup() {
		GUIDE.addEntry("introduction")
				.setIconStack(new ItemStack(ModItems.itemGuide))
				.addPageText("introduction_1");
	}
}
