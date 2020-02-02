package com.blakebr0.extendedcrafting.container;

import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderResultSlot;
import com.blakebr0.extendedcrafting.crafting.table.TableCraftResult;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Function;

public class EnderCrafterContainer extends Container {

	public IInventory result;
	public EnderCrafterTileEntity tile;

	private EliteTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory) {
		this(type, id, playerInventory, p -> false, new ItemStackHandler(9));
	}

	private EliteTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, IItemHandler inventory) {
		super(type, id);
		this.tile = tile;
		this.result = new TableCraftResult(tile);
		
		this.addSlotToContainer(new EnderResultSlot(this.result, 0, 124, 36));
		
		int wy, ex;
		for (wy = 0; wy < 3; wy++) {
			for (ex = 0; ex < 3; ex++) {
				this.addSlotToContainer(new Slot(this.tile, ex + wy * 3, 30 + ex * 18, 18 + wy * 18));
			}
		}

		for (wy = 0; wy < 3; wy++) {
			for (ex = 0; ex < 9; ex++) {
				this.addSlotToContainer(new Slot(player, ex + wy * 9 + 9, 8 + ex * 18, 88 + wy * 18));
			}
		}

		for (ex = 0; ex < 9; ex++) {
			this.addSlotToContainer(new Slot(player, ex, 8 + ex * 18, 146));
		}
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
		return new EnderCrafterContainer(ModContainerTypes.COMPRESSOR.get(), windowId, playerInventory);
	}

	public static EnderCrafterContainer create(int windowId, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, IItemHandler inventory, IIntArray data) {
		return new EnderCrafterContainer(ModContainerTypes.COMPRESSOR.get(), windowId, playerInventory, isUsableByPlayer, inventory, data);
	}

	@Override
	public boolean canInteractWith(PlayerEntity p_75145_1_) {
		return false;
	}
}