package com.blakebr0.extendedcrafting.client.container;

import javax.annotation.Nullable;

import com.blakebr0.extendedcrafting.crafting.table.TableCraftResultHandler;
import com.blakebr0.extendedcrafting.crafting.table.TableCraftingCraftResult;
import com.blakebr0.extendedcrafting.crafting.table.TableCraftingStackHandler;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.advanced.AdvancedCrafting;
import com.blakebr0.extendedcrafting.tile.TileAdvancedCraftingTable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAdvancedTable extends Container {

	public InventoryCrafting matrix;
	public IInventory result;
	private TileAdvancedCraftingTable tile;
	private IItemHandler handler;

	public ContainerAdvancedTable(InventoryPlayer player, TileAdvancedCraftingTable tile, World world) {
		this.tile = tile;
		this.handler = tile.matrix;
		this.matrix = new AdvancedCrafting(this, tile);
		this.result = new TableCraftingCraftResult(tile);
		this.addSlotToContainer(new TableCraftResultHandler(player.player, this.matrix, this.result, 0, 142, 53));
		int wy;
		int ex;

		for (wy = 0; wy < 5; ++wy) {
			for (ex = 0; ex < 5; ++ex) {
				this.addSlotToContainer(new SlotItemHandler(handler, ex + wy * 5, 14 + ex * 18, 18 + wy * 18));
			}
		}

		for (wy = 0; wy < 3; ++wy) {
			for (ex = 0; ex < 9; ++ex) {
				this.addSlotToContainer(new Slot(player, ex + wy * 9 + 9, 8 + ex * 18, 124 + wy * 18));
			}
		}

		for (ex = 0; ex < 9; ++ex) {
			this.addSlotToContainer(new Slot(player, ex, 8 + ex * 18, 182));
		}

		this.onCraftMatrixChanged(this.matrix);
		((TableCraftingStackHandler) handler).crafting = matrix;
	}

	public void onCraftMatrixChanged(IInventory matrix) {
		this.result.setInventorySlotContents(0, TableRecipeManager.getInstance().findMatchingRecipe(this.matrix, this.tile.getWorld()));
		this.tile.markDirty();
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tile.isUseableByPlayer(player);
	}

	@Nullable
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotNumber == 0) {
				if (!this.mergeItemStack(itemstack1, 26, 62, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (slotNumber >= 26 && slotNumber < 53) {
				if (!this.mergeItemStack(itemstack1, 53, 62, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slotNumber >= 53 && slotNumber < 62) {
				if (!this.mergeItemStack(itemstack1, 26, 53, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 26, 62, false)) {
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