package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PedestalTileEntity extends BaseInventoryTileEntity {
	private final BaseItemStackHandler inventory;

	public PedestalTileEntity(BlockPos pos, BlockState state) {
		super(ModTileEntities.PEDESTAL.get(), pos, state);
		this.inventory = new BaseItemStackHandler(1, this::markDirtyAndDispatch);

		this.inventory.setDefaultSlotLimit(1);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}
}
