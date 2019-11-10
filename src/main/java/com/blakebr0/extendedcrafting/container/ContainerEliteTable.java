package com.blakebr0.extendedcrafting.container;

import com.blakebr0.extendedcrafting.crafting.table.TableCraftResult;
import com.blakebr0.extendedcrafting.crafting.table.TableCrafting;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableResultHandler;
import com.blakebr0.extendedcrafting.tileentity.TileEliteCraftingTable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEliteTable extends Container {

	public InventoryCrafting matrix;
	public IInventory result;
	public TileEliteCraftingTable tile;

	public ContainerEliteTable(InventoryPlayer player, TileEliteCraftingTable tile) {
		this.tile = tile;
		this.matrix = new TableCrafting(this, tile);
		this.result = new TableCraftResult(tile);
		
		this.addSlotToContainer(new TableResultHandler(this.matrix, this.result, tile.getWorld(), 0, 172, 71));
		
		int wy, ex;
		for (wy = 0; wy < 7; wy++) {
			for (ex = 0; ex < 7; ex++) {
				this.addSlotToContainer(new Slot(this.matrix, ex + wy * 7, 8 + ex * 18, 18 + wy * 18));
			}
		}

		for (wy = 0; wy < 3; wy++) {
			for (ex = 0; ex < 9; ex++) {
				this.addSlotToContainer(new Slot(player, ex + wy * 9 + 9, 20 + ex * 18, 160 + wy * 18));
			}
		}

		for (ex = 0; ex < 9; ex++) {
			this.addSlotToContainer(new Slot(player, ex, 20 + ex * 18, 218));
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory matrix) {
		this.result.setInventorySlotContents(0, TableRecipeManager.getInstance().findMatchingRecipe(this.matrix, this.tile.getWorld()));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotNumber == 0) {
				if (!this.mergeItemStack(itemstack1, 50, 86, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (slotNumber >= 50 && slotNumber < 86) {
				if (!this.mergeItemStack(itemstack1, 1, 50, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 50, 86, false)) {
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
			this.onCraftMatrixChanged(matrix);
		}

		return itemstack;
	}
}