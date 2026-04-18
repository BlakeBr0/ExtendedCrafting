package com.blakebr0.extendedcrafting.container;

import com.blakebr0.cucumber.container.BaseContainerMenu;
import com.blakebr0.extendedcrafting.init.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FluxAlternatorContainer extends BaseContainerMenu {
	private final ContainerData data;

	public FluxAlternatorContainer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
		this(id, playerInventory, new SimpleContainerData(1), buffer.readBlockPos());
	}

	public FluxAlternatorContainer(int id, Inventory playerInventory, ContainerData data, BlockPos pos) {
		super(ModMenuTypes.FLUX_ALTERNATOR.get(), id, pos);
		this.data = data;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 112 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 170));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotNumber) {
		var itemstack = ItemStack.EMPTY;
		var slot = this.slots.get(slotNumber);

		if (slot.hasItem()) {
			var itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotNumber < 27) {
				if (!this.moveItemStackTo(itemstack1, 27, 36, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slotNumber < 36) {
				if (!this.moveItemStackTo(itemstack1, 0, 27, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, 27, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	public int getEnergyStored() {
		return this.data.get(0);
	}

	public int getMaxEnergyCapacity() {
		return this.data.get(1);
	}
}
