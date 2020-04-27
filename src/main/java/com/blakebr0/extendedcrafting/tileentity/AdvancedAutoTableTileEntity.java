package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;

public class AdvancedAutoTableTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
    private final BaseItemStackHandler inventory = new BaseItemStackHandler(26);

    public AdvancedAutoTableTileEntity() {
        super(ModTileEntities.ADVANCED_AUTO_TABLE.get());
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
        return Localizable.of("container.extendedcrafting.advanced_table").build();
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        return AdvancedAutoTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory);
    }
}
