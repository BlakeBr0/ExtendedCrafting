package com.blakebr0.extendedcrafting.container.slot;

import net.minecraft.inventory.container.Container;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TableOutputSlot extends SlotItemHandler {
    private final Container container;

    public TableOutputSlot(Container container, IItemHandler inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.container = container;
    }
}
