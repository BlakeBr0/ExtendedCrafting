package com.blakebr0.extendedcrafting.client.container;

import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.tile.TileCompressor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCompressor extends Container {

	public CombinationRecipe recipe;
	private TileCompressor tile;

	public ContainerCompressor(InventoryPlayer player, TileCompressor tile) {
		this.tile = tile;

		this.addSlotToContainer(new Slot(tile, 0, 135, 48) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		});
		this.addSlotToContainer(new Slot(tile, 1, 65, 48));
		this.addSlotToContainer(new Slot(tile, 2, 38, 48) {
			@Override
			public int getSlotStackLimit() {
				return 1;
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 112 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 170));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotNumber < 3) {
				if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			} else if (slotNumber >= 3) {
				ItemStack inputStack = this.inventorySlots.get(1).getStack();
                if (inputStack.isEmpty() || (inputStack.isItemEqual(itemstack1) && inputStack.getCount() < inputStack.getMaxStackSize())) {
					if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slotNumber < 30) {
					if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slotNumber >= 30 && slotNumber < 39) {
					if (!this.mergeItemStack(itemstack1, 3, 30, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

}
