package com.blakebr0.extendedcrafting;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ECCreativeTab extends ItemGroup {
	public ECCreativeTab() {
		super(ExtendedCrafting.MOD_ID);
	}

	@Override
	public ItemStack createIcon() {
		return ItemStack.EMPTY;
	}
}
