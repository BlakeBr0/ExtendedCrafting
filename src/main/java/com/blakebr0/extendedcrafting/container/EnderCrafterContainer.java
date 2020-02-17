package com.blakebr0.extendedcrafting.container;

import com.blakebr0.cucumber.inventory.slot.OutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Function;

public class EnderCrafterContainer extends Container {
	private final Function<PlayerEntity, Boolean> isUsableByPlayer;
	private final IIntArray data;

	private EnderCrafterContainer(ContainerType<?> type, int id, PlayerInventory playerInventory) {
		this(type, id, playerInventory, p -> false, new ItemStackHandler(10), new IntArray(2));
	}

	private EnderCrafterContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, IItemHandlerModifiable inventory, IIntArray data) {
		super(type, id);
		this.isUsableByPlayer = isUsableByPlayer;
		this.data = data;

		this.addSlot(new OutputSlot(inventory, 9, 124, 36));
		
		int wy, ex;
		for (wy = 0; wy < 3; wy++) {
			for (ex = 0; ex < 3; ex++) {
				this.addSlot(new SlotItemHandler(inventory, ex + wy * 3, 30 + ex * 18, 18 + wy * 18));
			}
		}

		for (wy = 0; wy < 3; wy++) {
			for (ex = 0; ex < 9; ex++) {
				this.addSlot(new Slot(playerInventory, ex + wy * 9 + 9, 8 + ex * 18, 88 + wy * 18));
			}
		}

		for (ex = 0; ex < 9; ex++) {
			this.addSlot(new Slot(playerInventory, ex, 8 + ex * 18, 146));
		}

		this.trackIntArray(data);
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
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (slotNumber >= 10 && slotNumber < 46) {
				if (!this.mergeItemStack(itemstack1, 1, 10, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
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

	public static EnderCrafterContainer create(int windowId, PlayerInventory playerInventory) {
		return new EnderCrafterContainer(ModContainerTypes.ENDER_CRAFTER.get(), windowId, playerInventory);
	}

	public static EnderCrafterContainer create(int windowId, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, IItemHandlerModifiable inventory, IIntArray data) {
		return new EnderCrafterContainer(ModContainerTypes.ENDER_CRAFTER.get(), windowId, playerInventory, isUsableByPlayer, inventory, data);
	}

	public int getProgressBarScaled(int pixels) {
		int i = this.getProgress();
		int j = Math.max(this.getProgressRequired(), i);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	public int getProgress() {
		return this.data.get(0);
	}

	public int getProgressRequired() {
		return this.data.get(1);
	}
}