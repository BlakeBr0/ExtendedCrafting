package com.blakebr0.extendedcrafting.lib;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public interface IExtendedTable {
	
	IItemHandlerModifiable getMatrix();
	ItemStack getResult();
}
