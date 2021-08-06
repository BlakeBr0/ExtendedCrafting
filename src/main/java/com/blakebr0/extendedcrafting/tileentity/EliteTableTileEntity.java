package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class EliteTableTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final BaseItemStackHandler inventory;

	public EliteTableTileEntity() {
		super(ModTileEntities.ELITE_TABLE.get());
		this.inventory = new BaseItemStackHandler(49, this::markDirtyAndDispatch);
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
		return EliteTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return !this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? LazyOptional.empty() : super.getCapability(cap, side);
	}
}
