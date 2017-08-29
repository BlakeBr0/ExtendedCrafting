package com.blakebr0.extendedcrafting.crafting.table.basic;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.tile.TileBasicCraftingTable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.items.ItemStackHandler;

public class BasicStackHandler extends ItemStackHandler {

	public InventoryCrafting crafting;
	private TileBasicCraftingTable tile;

	public BasicStackHandler(int size, TileBasicCraftingTable tile) {
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