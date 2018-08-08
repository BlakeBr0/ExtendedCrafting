package com.blakebr0.extendedcrafting.crafting.table;

import com.blakebr0.extendedcrafting.lib.IExtendedTable;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class TableCrafting extends InventoryCrafting implements ISidedInventory {

	public IExtendedTable tile;
	public IItemHandler handler;
	public Container container;
	public int lineSize;

	public TableCrafting(Container container, IExtendedTable tile) {
		super(container, tile.getLineSize(), tile.getLineSize());
		this.tile = tile;
		this.handler = tile.getMatrix();
		this.container = container;
		this.lineSize = tile.getLineSize();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot >= this.getSizeInventory() ? ItemStack.EMPTY : this.handler.getStackInSlot(slot);
	}

	@Override
	public ItemStack getStackInRowAndColumn(int row, int column) {
		if (row >= 0 && row < this.lineSize) {
			int x = row + column * this.lineSize;
			return this.getStackInSlot(x);
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.tile.setInventorySlotContents(slot, stack);
		this.container.onCraftMatrixChanged(this);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}
}