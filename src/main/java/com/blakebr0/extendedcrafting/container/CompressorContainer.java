package com.blakebr0.extendedcrafting.container;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.slot.BaseItemStackHandlerSlot;
import com.blakebr0.cucumber.inventory.slot.OutputSlot;
import com.blakebr0.extendedcrafting.container.slot.CatalystSlot;
import com.blakebr0.extendedcrafting.init.ModContainerTypes;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
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

public class CompressorContainer extends Container {
	private final Function<PlayerEntity, Boolean> isUsableByPlayer;
	private final IIntArray data;
	private final BlockPos pos;

	private CompressorContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		this(type, id, playerInventory, p -> false, (new CompressorTileEntity()).getInventory(), new IntArray(10), buffer.readBlockPos());
	}

	private CompressorContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, BaseItemStackHandler inventory, IIntArray data, BlockPos pos) {
		super(type, id);
		this.isUsableByPlayer = isUsableByPlayer;
		this.data = data;
		this.pos = pos;

		this.addSlot(new OutputSlot(inventory, 0, 135, 48));
		this.addSlot(new BaseItemStackHandlerSlot(inventory, 1, 65, 48));
		this.addSlot(new CatalystSlot(inventory, 2, 38, 48));

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 112 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 170));
		}

		this.addDataSlots(data);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int slotNumber) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotNumber < 3) {
				if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}
				
				slot.onQuickCraft(itemstack1, itemstack);
			} else {
				ItemStack inputStack = this.slots.get(1).getItem();
                if (inputStack.isEmpty() || (inputStack.sameItem(itemstack1) && inputStack.getCount() < inputStack.getMaxStackSize())) {
					if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slotNumber < 30) {
					if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slotNumber < 39) {
					if (!this.moveItemStackTo(itemstack1, 3, 30, false)) {
						return ItemStack.EMPTY;
					}
				}
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

	@Override
	public boolean stillValid(PlayerEntity player) {
		return this.isUsableByPlayer.apply(player);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public static CompressorContainer create(int windowId, PlayerInventory playerInventory, PacketBuffer buffer) {
		return new CompressorContainer(ModContainerTypes.COMPRESSOR.get(), windowId, playerInventory, buffer);
	}

	public static CompressorContainer create(int windowId, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, BaseItemStackHandler inventory, IIntArray data, BlockPos pos) {
		return new CompressorContainer(ModContainerTypes.COMPRESSOR.get(), windowId, playerInventory, isUsableByPlayer, inventory, data, pos);
	}
}
