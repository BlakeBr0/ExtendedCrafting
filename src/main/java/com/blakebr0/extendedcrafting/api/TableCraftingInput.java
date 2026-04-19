package com.blakebr0.extendedcrafting.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

import java.util.List;

public class TableCraftingInput extends CraftingInput {
    private final int tier;
    // these are hoisted from CraftInput.Positioned
    private final int top;
    private final int left;

    private TableCraftingInput(int width, int height, List<ItemStack> items, int tier, int top, int left) {
        super(width, height, items);
        this.tier = tier;
        this.top = top;
        this.left = left;
    }

    public int tier() {
        return this.tier;
    }

    public int top() {
        return this.top;
    }

    public int left() {
        return this.left;
    }

    // we still want the positioning and sizing logic to run
    public static TableCraftingInput of(int width, int height, List<ItemStack> items, int tier) {
        var positioned = CraftingInput.ofPositioned(width, height, items);
        var input = positioned.input();
        return new TableCraftingInput(input.width(), input.height(), input.items(), tier, positioned.top(), positioned.left());
    }

    public static TableCraftingInput empty(int tier) {
        return new TableCraftingInput(0, 0, List.of(), tier, 0, 0);
    }
}
