package com.blakebr0.extendedcrafting;

import com.blakebr0.extendedcrafting.init.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ECCreativeTab extends CreativeModeTab {
	public ECCreativeTab() {
		super(ExtendedCrafting.MOD_ID);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModItems.LUMINESSENCE.get());
	}
}
