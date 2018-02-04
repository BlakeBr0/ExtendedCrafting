package com.blakebr0.extendedcrafting.crafting.table;

import com.blakebr0.extendedcrafting.tile.TileAdvancedCraftingTable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.items.ItemStackHandler;

public class TableCraftingStackHandler extends ItemStackHandler {

	public InventoryCrafting crafting;
	private TileAdvancedCraftingTable tile;

	public TableCraftingStackHandler(int size, TileAdvancedCraftingTable tile) {
		super(size);
		this.tile = tile;
	}

	@Override
	protected void onContentsChanged(int slot) {
		if (crafting != null)
			tile.setResult(TableRecipeManager.getInstance().findMatchingRecipe(this.crafting, tile.getWorld()));
		super.onContentsChanged(slot);
	}
}