package com.blakebr0.extendedcrafting.container;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.slot.OutputSlot;
import com.blakebr0.extendedcrafting.container.slot.CatalystSlot;
import com.blakebr0.extendedcrafting.init.ModContainerTypes;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Function;

public class CompressorContainer extends AbstractContainerMenu {
	private final Function<Player, Boolean> isUsableByPlayer;
	private final ContainerData data;
	private final BlockPos pos;

	private CompressorContainer(MenuType<?> type, int id, Inventory playerInventory, FriendlyByteBuf buffer) {
		this(type, id, playerInventory, p -> false, CompressorTileEntity.createInventoryHandler(null).forContainer(), new SimpleContainerData(10), buffer.readBlockPos());
	}

	private CompressorContainer(MenuType<?> type, int id, Inventory playerInventory, Function<Player, Boolean> isUsableByPlayer, BaseItemStackHandler inventory, ContainerData data, BlockPos pos) {
		super(type, id);
		this.isUsableByPlayer = isUsableByPlayer;
		this.data = data;
		this.pos = pos;

		this.addSlot(new OutputSlot(inventory, 0, 135, 48));
		this.addSlot(new SlotItemHandler(inventory, 1, 65, 48));
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
	public ItemStack quickMoveStack(Player player, int slotNumber) {
		var itemstack = ItemStack.EMPTY;
		var slot = this.slots.get(slotNumber);

		if (slot.hasItem()) {
			var itemstack1 = slot.getItem();
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
	public boolean stillValid(Player player) {
		return this.isUsableByPlayer.apply(player);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public static CompressorContainer create(int windowId, Inventory playerInventory, FriendlyByteBuf buffer) {
		return new CompressorContainer(ModContainerTypes.COMPRESSOR.get(), windowId, playerInventory, buffer);
	}

	public static CompressorContainer create(int windowId, Inventory playerInventory, Function<Player, Boolean> isUsableByPlayer, BaseItemStackHandler inventory, ContainerData data, BlockPos pos) {
		return new CompressorContainer(ModContainerTypes.COMPRESSOR.get(), windowId, playerInventory, isUsableByPlayer, inventory, data, pos);
	}
}
