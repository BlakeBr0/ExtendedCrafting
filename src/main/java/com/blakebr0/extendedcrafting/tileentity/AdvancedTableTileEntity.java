package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.container.AdvancedTableContainer;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class AdvancedTableTileEntity extends BaseInventoryTileEntity implements INamedContainerProvider {
    private final BaseItemStackHandler inventory;

    public AdvancedTableTileEntity() {
        super(ModTileEntities.ADVANCED_TABLE.get());
        this.inventory = new BaseItemStackHandler(25, this::markDirtyAndDispatch);
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public ITextComponent getDisplayName() {
        return Localizable.of("container.extendedcrafting.advanced_table").build();
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        return AdvancedTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? LazyOptional.empty() : super.getCapability(cap, side);
    }
}
