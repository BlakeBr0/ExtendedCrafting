package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;

public class UltimateAutoTableTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
    private final BaseItemStackHandler inventory = new BaseItemStackHandler(82);

    public UltimateAutoTableTileEntity() {
        super(ModTileEntities.ULTIMATE_AUTO_TABLE.get());
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public void tick() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return Localizable.of("container.extendedcrafting.ultimate_table").build();
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        return UltimateAutoTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory);
    }
}
