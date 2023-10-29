package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.container.UltimateTableContainer;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

public class UltimateTableTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final BaseItemStackHandler inventory;

	public UltimateTableTileEntity(BlockPos pos, BlockState state) {
		super(ModTileEntities.ULTIMATE_TABLE.get(), pos, state);
		this.inventory = createInventoryHandler(this::setChangedAndDispatch);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public Component getDisplayName() {
		return Localizable.of("container.extendedcrafting.ultimate_table").build();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return UltimateTableContainer.create(windowId, playerInventory, this.inventory, this.getBlockPos());
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return !this.remove && cap == ForgeCapabilities.ITEM_HANDLER ? LazyOptional.empty() : super.getCapability(cap, side);
	}

	public static BaseItemStackHandler createInventoryHandler() {
		return createInventoryHandler(null);
	}

	public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
		return BaseItemStackHandler.create(81, onContentsChanged);
	}
}
