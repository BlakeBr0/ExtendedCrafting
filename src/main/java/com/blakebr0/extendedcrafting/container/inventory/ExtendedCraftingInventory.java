package com.blakebr0.extendedcrafting.container.inventory;

import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.extendedcrafting.api.TableCraftingInput;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class ExtendedCraftingInventory extends TransientCraftingContainer {
    private final AbstractContainerMenu container;
    private final CItemStacksHandler inventory;
    private final boolean autoTable;

    public ExtendedCraftingInventory(AbstractContainerMenu container, CItemStacksHandler inventory, int size) {
        this(container, inventory, size, false);
    }

    public ExtendedCraftingInventory(AbstractContainerMenu container, CItemStacksHandler inventory, int size, boolean autoTable) {
        super(container, size, size);
        this.container = container;
        this.inventory = inventory;
        this.autoTable = autoTable;
    }

    @Override
    public int getContainerSize() {
        return this.autoTable ? this.inventory.size() - 1 : this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!this.inventory.getResource(i).isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.getResource(slot).toStack(this.inventory.getAmountAsInt(slot));
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        try (var tx = Transaction.openRoot()) {
            var resource = this.inventory.getResource(slot);
            var removed = this.inventory.extract(slot, resource, amount, tx, true);

            this.container.slotsChanged(this);

            return resource.toStack(removed);
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        var stack = this.inventory.getResource(slot).toStack(this.inventory.getAmountAsInt(slot));

        this.inventory.set(slot, ItemResource.EMPTY, 0);

        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inventory.set(slot, ItemResource.of(stack), stack.count());
        this.container.slotsChanged(this);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.inventory.set(i, ItemResource.EMPTY, 0);
        }
    }

    public TableCraftingInput asCraftInput() {
        var tier = Math.floorDiv(this.getWidth(), 2);
        return TableCraftingInput.of(this.getWidth(), this.getHeight(), this.inventory.getStacks(), tier);
    }
}
