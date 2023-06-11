package com.blakebr0.extendedcrafting.container;

import com.blakebr0.cucumber.container.BaseContainerMenu;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.container.inventory.ExtendedCraftingInventory;
import com.blakebr0.extendedcrafting.container.slot.AutoTableOutputSlot;
import com.blakebr0.extendedcrafting.container.slot.TableOutputSlot;
import com.blakebr0.extendedcrafting.init.ModContainerTypes;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class UltimateAutoTableContainer extends BaseContainerMenu {
	private final Level level;
	private final Container result;

	private UltimateAutoTableContainer(MenuType<?> type, int id, Inventory playerInventory, FriendlyByteBuf buffer) {
		this(type, id, playerInventory, AutoTableTileEntity.Ultimate.createInventoryHandler(), buffer.readBlockPos());
	}

	private UltimateAutoTableContainer(MenuType<?> type, int id, Inventory playerInventory, BaseItemStackHandler inventory, BlockPos pos) {
		super(type, id, pos);
		this.level = playerInventory.player.level();
		this.result = new ResultContainer();

		var matrix = new ExtendedCraftingInventory(this, inventory, 9, true);

		this.addSlot(new TableOutputSlot(this, matrix, this.result, 0, 225, 89));
		
		int i, j;
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				this.addSlot(new Slot(matrix, j + i * 9, 27 + j * 18, 18 + i * 18));
			}
		}

		this.addSlot(new AutoTableOutputSlot(this, matrix, inventory, 81, 225, 133));

		for (i = 0; i < 3; i++) {
			for (j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 47 + j * 18, 196 + i * 18));
			}
		}

		for (j = 0; j < 9; j++) {
			this.addSlot(new Slot(playerInventory, j, 47 + j * 18, 254));
		}

		this.slotsChanged(matrix);
	}

	@Override
	public void slotsChanged(Container matrix) {
		var recipe = this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.TABLE.get(), matrix, this.level);

		if (recipe.isPresent()) {
			var result = recipe.get().assemble(matrix, this.level.registryAccess());
			this.result.setItem(0, result);
		} else {
			this.result.setItem(0, ItemStack.EMPTY);
		}

		super.slotsChanged(matrix);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotNumber) {
		var itemstack = ItemStack.EMPTY;
		var slot = this.slots.get(slotNumber);

		if (slot.hasItem()) {
			var itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotNumber == 0 || slotNumber == 82) {
				if (!this.moveItemStackTo(itemstack1, 83, 119, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotNumber >= 83 && slotNumber < 119) {
				if (!this.moveItemStackTo(itemstack1, 1, 82, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 83, 119, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
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

	public static UltimateAutoTableContainer create(int windowId, Inventory playerInventory, FriendlyByteBuf buffer) {
		return new UltimateAutoTableContainer(ModContainerTypes.ULTIMATE_AUTO_TABLE.get(), windowId, playerInventory, buffer);
	}

	public static UltimateAutoTableContainer create(int windowId, Inventory playerInventory, BaseItemStackHandler inventory, BlockPos pos) {
		return new UltimateAutoTableContainer(ModContainerTypes.ULTIMATE_AUTO_TABLE.get(), windowId, playerInventory, inventory, pos);
	}
}