package com.blakebr0.extendedcrafting.crafting.endercrafter;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class EnderResultSlot extends Slot {

	public EnderResultSlot(IInventory inventory, int index, int xPosition, int yPosition) {
		super(inventory, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}
