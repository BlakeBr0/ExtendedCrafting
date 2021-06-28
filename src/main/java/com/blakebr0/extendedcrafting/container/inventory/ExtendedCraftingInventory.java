package com.blakebr0.extendedcrafting.container.inventory;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

public class ExtendedCraftingInventory extends CraftingInventory {
    private final Container container;
    private final BaseItemStackHandler inventory;
    private final boolean autoTable;

    public ExtendedCraftingInventory(Container container, BaseItemStackHandler inventory, int size) {
        this(container, inventory, size, false);
    }

    public ExtendedCraftingInventory(Container container, BaseItemStackHandler inventory, int size, boolean autoTable) {
        super(container, size, size);
        this.container = container;
        this.inventory = inventory;
        this.autoTable = autoTable;
    }

    @Override
    public int getContainerSize() {
        return this.autoTable ? this.inventory.getSlots() - 1 : this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = this.inventory.extractItemSuper(slot, amount, false);
        this.container.slotsChanged(this);

        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = this.inventory.getStackInSlot(slot);
        this.inventory.setStackInSlot(slot, ItemStack.EMPTY);

        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inventory.setStackInSlot(slot, stack);
        this.container.slotsChanged(this);
    }

    @Override
    public void setChanged() { }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
