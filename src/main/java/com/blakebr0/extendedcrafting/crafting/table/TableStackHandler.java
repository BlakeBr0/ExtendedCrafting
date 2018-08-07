package com.blakebr0.extendedcrafting.crafting.table;

import com.blakebr0.extendedcrafting.lib.IExtendedTable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TableStackHandler extends ItemStackHandler {

	private IExtendedTable tile;
	public InventoryCrafting crafting;
	private World world;

	public TableStackHandler(int size, IExtendedTable tile, World world) {
		super(size);
		this.tile = tile;
		this.world = world;
	}

	@Override
	protected void onContentsChanged(int slot) {
		if (this.crafting != null) {
			this.tile.setResult(TableRecipeManager.getInstance().findMatchingRecipe(this.crafting, this.world));
		}
		
		super.onContentsChanged(slot);
	}
}