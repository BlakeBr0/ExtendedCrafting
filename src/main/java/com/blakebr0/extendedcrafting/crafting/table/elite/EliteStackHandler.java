package com.blakebr0.extendedcrafting.crafting.table.elite;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.tile.TileEliteCraftingTable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.items.ItemStackHandler;

public class EliteStackHandler extends ItemStackHandler {

	public InventoryCrafting crafting;
	private TileEliteCraftingTable tile;

	public EliteStackHandler(int size, TileEliteCraftingTable tile) {
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