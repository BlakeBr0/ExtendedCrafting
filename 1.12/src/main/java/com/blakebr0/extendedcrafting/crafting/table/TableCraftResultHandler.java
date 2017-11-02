package com.blakebr0.extendedcrafting.crafting.table;

import javax.annotation.Nullable;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.crafting.table.advanced.AdvancedCrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TableCraftResultHandler extends Slot {

	private final AdvancedCrafting crafting;
	private final EntityPlayer player;
	private final ItemStackHandler matrix;

	public TableCraftResultHandler(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventory, int slot, int x, int y) {
		super(inventory, slot, x, y);
		this.crafting = (AdvancedCrafting) craftingInventory;
		this.player = player;
		this.matrix = ((AdvancedCrafting) craftingInventory).tile.matrix;
	}

	@Override
	public boolean isItemValid(@Nullable ItemStack stack) {
		return false;
	}
	
	@Override // TODO: make it so you cant take anything from the output if there is an interface (or something)
	public boolean canTakeStack(EntityPlayer player) {
		return super.canTakeStack(player);
	}

	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack stack) {
		for (int i = 0; i < this.matrix.getSlots(); i++) {
			ItemStack slotStack = this.matrix.getStackInSlot(i);
			if (!StackHelper.isNull(slotStack)) {
				if (slotStack.getItem().hasContainerItem(slotStack) && slotStack.getCount() == 1) {
					this.matrix.setStackInSlot(i, slotStack.getItem().getContainerItem(slotStack));
				} else {
					this.matrix.setStackInSlot(i, StackHelper.decrease(slotStack.copy(), 1, false));
				}
			}
		}
		this.crafting.container.onCraftMatrixChanged(this.crafting);
		return stack;
	}
}
