package com.blakebr0.extendedcrafting.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class EmptyContainer extends AbstractContainerMenu {
    public static final EmptyContainer INSTANCE = new EmptyContainer();

    private EmptyContainer() {
        super(null, -1);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
