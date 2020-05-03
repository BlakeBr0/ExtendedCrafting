package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;

public class PedestalTileEntity extends BaseInventoryTileEntity {
	private final BaseItemStackHandler inventory = new BaseItemStackHandler(1, this::markDirtyAndDispatch);

	public PedestalTileEntity() {
		super(ModTileEntities.PEDESTAL.get());
		this.inventory.setDefaultSlotLimit(1);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}
}
