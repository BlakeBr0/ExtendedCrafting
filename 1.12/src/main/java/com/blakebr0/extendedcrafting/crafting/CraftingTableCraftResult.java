package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.tile.TileCraftingTable;

import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;

public class CraftingTableCraftResult extends InventoryCraftResult {

	private TileCraftingTable tile;

	public CraftingTableCraftResult(TileCraftingTable tile) {
		this.tile = tile;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot == 0 ? this.tile.getResult() : ItemStack.EMPTY;
	}

	@Override
	public ItemStack decrStackSize(int slot, int decrement) {
		ItemStack stack = this.tile.getResult();
		if (!StackHelper.isNull(stack)) {
			ItemStack resultStack = stack;
			this.tile.setResult(ItemStack.EMPTY);
			return resultStack;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.tile.setResult(stack);
	}
}