package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.tileentity.BaseTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.FluxAlternatorContainer;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;

public class FluxAlternatorTileEntity extends BaseTileEntity implements MenuProvider {
    private final EnergyStorage energy;
    private int oldEnergy;

    public FluxAlternatorTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.FLUX_ALTERNATOR.get(), pos, state);
        this.energy = new EnergyStorage(ModConfigs.FLUX_ALTERNATOR_POWER_CAPACITY.get());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.energy.deserializeNBT(tag.get("Energy"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Energy", this.energy.getEnergyStored());
    }

    @Override
    public Component getDisplayName() {
        return Localizable.of("container.extendedcrafting.flux_alternator").build();
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return FluxAlternatorContainer.create(windowId, playerInventory, this.getBlockPos());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.isRemoved() && cap == ForgeCapabilities.ENERGY) {
            return ForgeCapabilities.ENERGY.orEmpty(cap, LazyOptional.of(this::getEnergy));
        }

        return super.getCapability(cap, side);
    }

    public EnergyStorage getEnergy() {
        return this.energy;
    }

    // TODO: ideally this gets removed
    public static void tick(Level level, BlockPos pos, BlockState state, FluxAlternatorTileEntity tile) {
        if (tile.oldEnergy != tile.energy.getEnergyStored()) {
            tile.oldEnergy = tile.energy.getEnergyStored();
            tile.markDirtyAndDispatch();
        }
    }
}
