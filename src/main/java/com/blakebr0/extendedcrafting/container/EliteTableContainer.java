package com.blakebr0.extendedcrafting.container;

import com.blakebr0.extendedcrafting.container.slot.TableOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Function;

public class EliteTableContainer extends Container {
	private final Function<PlayerEntity, Boolean> isUsableByPlayer;
	private final IItemHandlerModifiable inventory;

	private EliteTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory) {
		this(type, id, playerInventory, p -> false, new ItemStackHandler(49));
	}

	private EliteTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, IItemHandlerModifiable inventory) {
		super(type, id);
		this.isUsableByPlayer = isUsableByPlayer;
		this.inventory = inventory;
		
		this.addSlot(new TableOutputSlot(this, inventory, 0, 172, 71));
		
		int wy, ex;
		for (wy = 0; wy < 7; wy++) {
			for (ex = 0; ex < 7; ex++) {
				this.addSlot(new SlotItemHandler(inventory, ex + wy * 7, 8 + ex * 18, 18 + wy * 18));
			}
		}

		for (wy = 0; wy < 3; wy++) {
			for (ex = 0; ex < 9; ex++) {
				this.addSlot(new Slot(playerInventory, ex + wy * 9 + 9, 20 + ex * 18, 160 + wy * 18));
			}
		}

		for (ex = 0; ex < 9; ex++) {
			this.addSlot(new Slot(playerInventory, ex, 20 + ex * 18, 218));
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory matrix) {

	}

	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return this.isUsableByPlayer.apply(player);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotNumber) {
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
		}

		return itemstack;
	}

	public static EliteTableContainer create(int windowId, PlayerInventory playerInventory) {
		return new EliteTableContainer(ModContainerTypes.ELITE_TABLE.get(), windowId, playerInventory);
	}

	public static EliteTableContainer create(int windowId, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, IItemHandlerModifiable inventory) {
		return new EliteTableContainer(ModContainerTypes.ELITE_TABLE.get(), windowId, playerInventory, isUsableByPlayer, inventory);
	}
}