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

//    TODO test that this isn't a thing anymore
//    @Override
//    public ItemStack getItem() {
//        var stack = super.getItem();
//
//        // TODO: this is a shitty workaround for a dupe bug #146
//        if (!stack.equals(this.lastStack)) {
//            this.lastStack = stack;
//            this.container.slotsChanged(this.matrix);
//        }
//
//        return stack;
//    }
}
