package com.blakebr0.extendedcrafting.container.slot;

import com.blakebr0.cucumber.inventory.slot.OutputSlot;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
import com.blakebr0.extendedcrafting.container.BasicTableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
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
        boolean isVanilla = false;

        if (this.container instanceof BasicTableContainer) {
            isVanilla = ((BasicTableContainer) this.container).isVanillaRecipe();
        } else if (this.container instanceof BasicAutoTableContainer) {
            isVanilla = ((BasicAutoTableContainer) this.container).isVanillaRecipe();
        }

        NonNullList<ItemStack> remaining;

        if (isVanilla) {
            remaining = player.world.getRecipeManager().getRecipeNonNull(IRecipeType.CRAFTING, (CraftingInventory) this.matrix, player.world);
        } else {
            remaining = player.world.getRecipeManager().getRecipeNonNull(RecipeTypes.TABLE, this.matrix, player.world);
        }

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
