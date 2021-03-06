package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.init.ModTileEntities;

public class PedestalTileEntity extends BaseInventoryTileEntity {
	private final BaseItemStackHandler inventory;

	public PedestalTileEntity() {
		super(ModTileEntities.PEDESTAL.get());
		this.inventory = new BaseItemStackHandler(1, this::markDirtyAndDispatch);

		this.inventory.setDefaultSlotLimit(1);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}
}
