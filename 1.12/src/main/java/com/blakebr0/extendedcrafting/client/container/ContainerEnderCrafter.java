package com.blakebr0.extendedcrafting.client.container;

import javax.annotation.Nullable;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.basic.BasicCraftResult;
import com.blakebr0.extendedcrafting.crafting.table.basic.BasicCrafting;
import com.blakebr0.extendedcrafting.crafting.table.basic.BasicResultHandler;
import com.blakebr0.extendedcrafting.crafting.table.basic.BasicStackHandler;
import com.blakebr0.extendedcrafting.endercrafter.EnderCraftResult;
import com.blakebr0.extendedcrafting.endercrafter.SlotEnderOutput;
import com.blakebr0.extendedcrafting.tile.TileBasicCraftingTable;
import com.blakebr0.extendedcrafting.tile.TileEnderCrafter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEnderCrafter extends Container {

	public InventoryCrafting matrix;
	public IInventory result;
	private TileEnderCrafter tile;
	private IItemHandler handler;

	public ContainerEnderCrafter(InventoryPlayer player, TileEnderCrafter tile, World world) {
		this.tile = tile;
		this.handler = tile.matrix;
		this.result = new EnderCraftResult(tile);
		this.addSlotToContainer(new Slot(tile.result, 0, 124, 35));
		int wy;
		int ex;

		for (wy = 0; wy < 3; ++wy) {
			for (ex = 0; ex < 3; ++ex) {
				this.addSlotToContainer(new SlotItemHandler(handler, ex + wy * 3, 30 + ex * 18, 17 + wy * 18));
			}
		}

		for (wy = 0; wy < 3; ++wy) {
			for (ex = 0; ex < 9; ++ex) {
				this.addSlotToContainer(new Slot(player, ex + wy * 9 + 9, 8 + ex * 18, 84 + wy * 18));
			}
		}

		for (ex = 0; ex < 9; ++ex) {
			this.addSlotToContainer(new Slot(player, ex, 8 + ex * 18, 142));
		}
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
				if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (slotNumber >= 10 && slotNumber < 37) {
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slotNumber >= 37 && slotNumber < 46) {
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
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