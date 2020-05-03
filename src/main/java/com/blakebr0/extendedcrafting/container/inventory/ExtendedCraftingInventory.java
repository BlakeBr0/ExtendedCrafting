package com.blakebr0.extendedcrafting.container.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ExtendedCraftingInventory implements IInventory {
    private final Container container;
    private final IItemHandlerModifiable inventory;
    private final boolean autoTable;

    public ExtendedCraftingInventory(Container container, IItemHandlerModifiable inventory) {
        this(container, inventory, false);
    }

    public ExtendedCraftingInventory(Container container, IItemHandlerModifiable inventory, boolean autoTable) {
        this.container = container;
        this.inventory = inventory;
        this.autoTable = autoTable;
    }

    @Override
    public int getSizeInventory() {
        return this.autoTable ? this.inventory.getSlots() - 1 : this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getSizeInventory(); i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = this.inventory.extractItem(slot, amount, false);
        this.container.onCraftMatrixChanged(this);

        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = this.inventory.getStackInSlot(slot);
        this.inventory.setStackInSlot(slot, ItemStack.EMPTY);

        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.inventory.setStackInSlot(slot, stack);
        this.container.onCraftMatrixChanged(this);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.getSizeInventory(); i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
