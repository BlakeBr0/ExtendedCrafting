package com.blakebr0.extendedcrafting.container;

import com.blakebr0.extendedcrafting.init.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public class CraftingCoreContainer extends Container {
	private final Function<PlayerEntity, Boolean> isUsableByPlayer;
	private final IIntArray data;
	private final BlockPos pos;

	private CraftingCoreContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		this(type, id, playerInventory, p -> false, new IntArray(7), buffer.readBlockPos());
	}

	private CraftingCoreContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, IIntArray data, BlockPos pos) {
		super(type, id);
		this.isUsableByPlayer = isUsableByPlayer;
		this.data = data;
		this.pos = pos;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 112 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 170));
		}

		this.trackIntArray(data);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotNumber) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotNumber < 27) {
				if (!this.mergeItemStack(itemstack1, 27, 36, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slotNumber < 36) {
				if (!this.mergeItemStack(itemstack1, 0, 27, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, 27, false)) {
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
	public boolean canInteractWith(PlayerEntity player) {
		return this.isUsableByPlayer.apply(player);
	}

	public static CraftingCoreContainer create(int windowId, PlayerInventory playerInventory, PacketBuffer buffer) {
		return new CraftingCoreContainer(ModContainerTypes.CRAFTING_CORE.get(), windowId, playerInventory, buffer);
	}

	public static CraftingCoreContainer create(int windowId, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, IIntArray data, BlockPos pos) {
		return new CraftingCoreContainer(ModContainerTypes.CRAFTING_CORE.get(), windowId, playerInventory, isUsableByPlayer, data, pos);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public int getEnergyBarScaled(int pixels) {
		int i = this.getEnergyStored();
		int j = this.getMaxEnergyStored();
		return (int) (j != 0 && i != 0 ? i * (long) pixels / j : 0);
	}

	public int getProgressBarScaled(int pixels) {
		int i = this.getProgress();
		long j = this.getEnergyRequired();
		return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
	}

	public boolean hasRecipe() {
		return this.data.get(6) > 0;
	}

	public int getProgress() {
		return this.data.get(0);
	}

	public int getPedestalCount() {
		return this.data.get(1);
	}

	public int getEnergyStored() {
		return this.data.get(2);
	}

	public int getMaxEnergyStored() {
		return this.data.get(3);
	}

	public int getEnergyRequired() {
		return this.data.get(4);
	}

	public int getEnergyRate() {
		return this.data.get(5);
	}
}
