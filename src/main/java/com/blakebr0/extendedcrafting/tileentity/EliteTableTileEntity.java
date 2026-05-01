package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class EliteTableTileEntity extends BaseInventoryTileEntity implements MenuProvider, Clearable {
	private final BaseItemStackHandler inventory;

	public EliteTableTileEntity(BlockPos pos, BlockState state) {
		super(ModTileEntities.ELITE_TABLE.get(), pos, state);
		this.inventory = createInventoryHandler((slot) -> this.setChangedAndDispatch());
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public Component getDisplayName() {
		return Localizable.of("container.extendedcrafting.elite_table").build();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return EliteTableContainer.create(windowId, playerInventory, this.inventory, this.getBlockPos());
	}

	public static BaseItemStackHandler createInventoryHandler() {
		return createInventoryHandler(null);
	}

	public static BaseItemStackHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
		return BaseItemStackHandler.create(49, onContentsChanged, builder -> {});
	}

    @Override
    public void clearContent() {
        getInventory().getStacks().clear();
    }
}
