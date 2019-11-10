package com.blakebr0.extendedcrafting;

import com.blakebr0.extendedcrafting.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ECItemGroup extends ItemGroup {
	public ECItemGroup() {
		super(ExtendedCrafting.MOD_ID);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(ModItems.LUMINESSENCE.get());
	}
}
