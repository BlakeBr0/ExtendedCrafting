package com.blakebr0.extendedcrafting.container.slot;

import com.blakebr0.cucumber.inventory.slot.OutputSlot;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class AutoTableOutputSlot extends OutputSlot {
    private final AbstractContainerMenu container;
    private final CraftingContainer matrix;
    private ItemStack lastStack = ItemStack.EMPTY;

    public AutoTableOutputSlot(AbstractContainerMenu container, CraftingContainer matrix, IItemHandler inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.container = container;
        this.matrix = matrix;
    }

    @Override
    public ItemStack getItem() {
        var stack = super.getItem();

        // TODO: this is a shitty workaround for a dupe bug #146
        if (!stack.equals(this.lastStack, false)) {
            this.lastStack = stack;
            this.container.slotsChanged(this.matrix);
        }

        return stack;
    }
}
