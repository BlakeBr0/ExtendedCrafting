package com.blakebr0.extendedcrafting.container.slot;

import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
import com.blakebr0.extendedcrafting.container.BasicTableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;

public class TableOutputSlot extends Slot {
    private final Container container;
    private final IInventory matrix;

    public TableOutputSlot(Container container, IInventory matrix, IInventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.container = container;
        this.matrix = matrix;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
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
            remaining = player.level.getRecipeManager().getRemainingItemsFor(IRecipeType.CRAFTING, (CraftingInventory) this.matrix, player.level);
        } else {
            remaining = player.level.getRecipeManager().getRemainingItemsFor(RecipeTypes.TABLE, this.matrix, player.level);
        }

        for (int i = 0; i < remaining.size(); i++) {
            ItemStack slotStack = this.matrix.getItem(i);
            ItemStack remainingStack = remaining.get(i);

            if (!slotStack.isEmpty()) {
                this.matrix.removeItem(i, 1);
                slotStack = this.matrix.getItem(i);
            }

            if (!remainingStack.isEmpty()) {
                if (slotStack.isEmpty()) {
                    this.matrix.setItem(i, remainingStack);
                } else if (ItemStack.isSame(slotStack, remainingStack) && ItemStack.tagMatches(slotStack, remainingStack)) {
                    remainingStack.grow(slotStack.getCount());
                    this.matrix.setItem(i, remainingStack);
                }
            }
        }

        this.container.slotsChanged(this.matrix);

        return stack;
    }
}
