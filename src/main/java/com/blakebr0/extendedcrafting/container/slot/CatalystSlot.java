package com.blakebr0.extendedcrafting.container.slot;

import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.cucumber.inventory.slot.CSingleSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CatalystSlot extends CSingleSlot {
    private final CItemStacksHandler inventory;
    private final int index;

    public CatalystSlot(CItemStacksHandler inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.inventory = inventory;
        this.index = index;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return true;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack remove(int amount) {
        return this.inventory.extractItem(this.index, amount, false, true);
    }
}
