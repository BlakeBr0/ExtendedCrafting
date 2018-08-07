package com.blakebr0.extendedcrafting.crafting.table;

import com.blakebr0.cucumber.helper.StackHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TableResultHandler extends Slot {

	private final TableCrafting crafting;
	private final ItemStackHandler matrix;

	public TableResultHandler(InventoryCrafting crafting, IInventory inventory, int slot, int x, int y) {
		super(inventory, slot, x, y);
		this.crafting = (TableCrafting) crafting;
		this.matrix = (ItemStackHandler) ((TableCrafting) crafting).tile.getMatrix();
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack stack) {
		for (int i = 0; i < this.matrix.getSlots(); i++) {
			ItemStack slotStack = this.matrix.getStackInSlot(i);
			if (!slotStack.isEmpty()) {
				if (slotStack.getItem().hasContainerItem(slotStack) && slotStack.getCount() == 1) {
					this.matrix.setStackInSlot(i, slotStack.getItem().getContainerItem(slotStack));
				} else {
					this.matrix.setStackInSlot(i, StackHelper.decrease(slotStack.copy(), 1, false));
				}
			}
		}
		
		this.crafting.container.onCraftMatrixChanged(this.crafting);
		return stack;
	}
}
