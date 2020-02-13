package com.blakebr0.extendedcrafting.container.slot;

import com.blakebr0.cucumber.inventory.slot.OutputSlot;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

public class TableOutputSlot extends OutputSlot {
    private final Container container;
    private final IInventory matrix;

    public TableOutputSlot(Container container, IInventory matrix, IItemHandler inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.container = container;
        this.matrix = matrix;
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
        NonNullList<ItemStack> remaining = player.world.getRecipeManager().getRecipeNonNull(RecipeTypes.TABLE, this.matrix, player.world);

        for (int i = 0; i < remaining.size(); i++) {
            ItemStack slotStack = this.matrix.getStackInSlot(i);
            ItemStack remainingStack = remaining.get(i);

            if (!slotStack.isEmpty()) {
                this.matrix.decrStackSize(i, 1);
                slotStack = this.matrix.getStackInSlot(i);
            }

            if (!remainingStack.isEmpty()) {
                if (slotStack.isEmpty()) {
                    this.matrix.setInventorySlotContents(i, remainingStack);
                } else if (ItemStack.areItemsEqual(slotStack, remainingStack) && ItemStack.areItemStackTagsEqual(slotStack, remainingStack)) {
                    remainingStack.grow(slotStack.getCount());
                    this.matrix.setInventorySlotContents(i, remainingStack);
                }
            }
        }

        this.container.onCraftMatrixChanged(this.matrix);

        return stack;
    }
}
