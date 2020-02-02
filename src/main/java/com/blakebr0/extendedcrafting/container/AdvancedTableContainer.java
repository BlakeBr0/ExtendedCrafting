package com.blakebr0.extendedcrafting.container;

import com.blakebr0.extendedcrafting.crafting.table.TableCraftResult;
import com.blakebr0.extendedcrafting.crafting.table.TableCrafting;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableResultHandler;
import com.blakebr0.extendedcrafting.tileentity.TileAdvancedCraftingTable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Function;

public class AdvancedTableContainer extends Container {

	public InventoryCrafting matrix;
	public IInventory result;
	public TileAdvancedCraftingTable tile;

	private AdvancedTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory) {
		this(type, id, playerInventory, p -> false, new ItemStackHandler(9));
	}

	private AdvancedTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, IItemHandler inventory) {
		super(type, id);
		this.tile = tile;
		this.matrix = new TableCrafting(this, tile);
		this.result = new TableCraftResult(tile);
		
		this.addSlot(new TableResultHandler(this.matrix, this.result, tile.getWorld(), 0, 142, 53));
		
		int wy, ex;
		for (wy = 0; wy < 5; wy++) {
			for (ex = 0; ex < 5; ex++) {
				this.addSlot(new Slot(this.matrix, ex + wy * 5, 14 + ex * 18, 18 + wy * 18));
			}
		}

		for (wy = 0; wy < 3; wy++) {
			for (ex = 0; ex < 9; ex++) {
				this.addSlot(new Slot(player, ex + wy * 9 + 9, 8 + ex * 18, 124 + wy * 18));
			}
		}

		for (ex = 0; ex < 9; ex++) {
			this.addSlot(new Slot(player, ex, 8 + ex * 18, 182));
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory matrix) {
		this.result.setInventorySlotContents(0, TableRecipeManager.getInstance().findMatchingRecipe(this.matrix, this.tile.getWorld()));
	}

	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return this.tile.isUsableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotNumber) {
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
			} else if (slotNumber >= 26 && slotNumber < 62) {
				if (!this.mergeItemStack(itemstack1, 1, 26, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 26, 62, false)) {
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
			this.onCraftMatrixChanged(this.matrix);
		}

		return itemstack;
	}

	public static AdvancedTableContainer create(int windowId, PlayerInventory playerInventory) {
		return new AdvancedTableContainer(ModContainerTypes.BASIC_TABLE.get(), windowId, playerInventory);
	}

	public static AdvancedTableContainer create(int windowId, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, IItemHandler inventory) {
		return new AdvancedTableContainer(ModContainerTypes.COMPRESSOR.get(), windowId, playerInventory, isUsableByPlayer, inventory);
	}
}