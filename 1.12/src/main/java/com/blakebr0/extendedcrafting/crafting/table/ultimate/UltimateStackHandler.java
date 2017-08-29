package com.blakebr0.extendedcrafting.crafting.table.ultimate;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.tile.TileUltimateCraftingTable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.items.ItemStackHandler;

public class UltimateStackHandler extends ItemStackHandler {

	public InventoryCrafting crafting;
	private TileUltimateCraftingTable tile;

	public UltimateStackHandler(int size, TileUltimateCraftingTable tile) {
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