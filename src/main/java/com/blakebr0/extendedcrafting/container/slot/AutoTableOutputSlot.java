package com.blakebr0.extendedcrafting.container.slot;

import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.cucumber.inventory.slot.COutputSlot;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

public class AutoTableOutputSlot extends COutputSlot {
    private final AbstractContainerMenu container;
    private final CraftingContainer matrix;
    private ItemStack lastStack = ItemStack.EMPTY;

    public AutoTableOutputSlot(AbstractContainerMenu container, CraftingContainer matrix, CItemStacksHandler inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.container = container;
        this.matrix = matrix;
    }

    @Override
    public ItemStack getStackCopy() {
        var stack = super.getStackCopy();

        // TODO: this is a workaround for a dupe bug #146
        // this is because the tile entity updating the inventory does not seem to trigger slotsChanged.
        // one day I'll come up with a better solution but for now this accomplishes the same goal
        if (!ItemStack.matches(stack, this.lastStack)) {
            this.lastStack = stack;
            this.container.slotsChanged(this.matrix);
        }

        return stack;
    }
}
