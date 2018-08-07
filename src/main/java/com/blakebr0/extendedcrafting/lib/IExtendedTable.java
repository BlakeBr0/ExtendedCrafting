package com.blakebr0.extendedcrafting.lib;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IExtendedTable {
	
	IItemHandlerModifiable getMatrix();
	ItemStack getResult();
	void setResult(ItemStack stack);
	void setInventorySlotContents(int slot, ItemStack stack);
	int getLineSize();
}
