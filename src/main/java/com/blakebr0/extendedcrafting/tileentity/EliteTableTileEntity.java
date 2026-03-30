package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class EliteTableTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final CItemStacksHandler inventory;

	public EliteTableTileEntity(BlockPos pos, BlockState state) {
		super(ModTileEntities.ELITE_TABLE.get(), pos, state);
		this.inventory = createInventoryHandler((s_, _) -> this.setChangedAndDispatch());
	}

	@Override
	public CItemStacksHandler getInventory() {
		return this.inventory;
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.extendedcrafting.elite_table");
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return EliteTableContainer.create(windowId, playerInventory, this.inventory, this.getBlockPos());
	}

	public static CItemStacksHandler createInventoryHandler() {
		return createInventoryHandler(null);
	}

	public static CItemStacksHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
		return CItemStacksHandler.create(49, onContentsChanged, _ -> {});
	}
}
