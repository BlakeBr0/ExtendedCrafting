package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.extendedcrafting.tile.TileCraftingTable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.items.ItemStackHandler;

public class CraftingTableStackHandler extends ItemStackHandler {

	public InventoryCrafting crafting;
	private TileCraftingTable tile;

	public CraftingTableStackHandler(int size, TileCraftingTable tile) {
		super(size);
		this.tile = tile;
	}

	@Override
	protected void onContentsChanged(int slot) {
		if (crafting != null) {
			ItemStack recipe = CraftingManager.findMatchingResult(this.crafting, this.tile.getWorld());
			tile.setResult(recipe);
		}
		super.onContentsChanged(slot);
	}
}