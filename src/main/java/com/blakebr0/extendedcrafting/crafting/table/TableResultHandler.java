package com.blakebr0.extendedcrafting.crafting.table;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class TableResultHandler extends Slot {

	private final TableCrafting crafting;
	private final IInventory matrix;
	private final World world;

	public TableResultHandler(InventoryCrafting crafting, IInventory inventory, World world, int slot, int x, int y) {
		super(inventory, slot, x, y);
		this.crafting = (TableCrafting) crafting;
		this.matrix = (IInventory) this.crafting.tile;
		this.world = world;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack stack) {
		NonNullList<ItemStack> remaining = TableRecipeManager.getRemainingItems(this.crafting, this.world);
		
        for (int i = 0; i < remaining.size(); i++) {
            ItemStack itemstack = this.matrix.getStackInSlot(i);
            ItemStack itemstack1 = remaining.get(i);

            if (!itemstack.isEmpty()) {
                this.matrix.decrStackSize(i, 1);
                itemstack = this.matrix.getStackInSlot(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.matrix.setInventorySlotContents(i, itemstack1);
                } else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    this.matrix.setInventorySlotContents(i, itemstack1);
                }
            }
        }
        
        this.crafting.container.onCraftMatrixChanged(this.crafting);
		
		return stack;
	}
}
