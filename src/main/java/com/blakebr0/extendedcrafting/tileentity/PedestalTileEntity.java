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
		this.inventory = createInventoryHandler(this::setChangedAndDispatch);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
		return BaseItemStackHandler.create(1, onContentsChanged, builder -> {
			builder.setDefaultSlotLimit(1);
		});
	}
}
