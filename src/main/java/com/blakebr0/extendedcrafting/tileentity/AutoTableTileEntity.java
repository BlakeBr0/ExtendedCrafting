package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CustomEnergyStorage;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class AutoTableTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
    private int oldEnergy;
    protected final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return AutoTableTileEntity.this.getEnergy().getEnergyStored();
                case 1:
                    return AutoTableTileEntity.this.getEnergy().getMaxEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {

        }

        @Override
        public int size() {
            return 2;
        }
    };

    public AutoTableTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.putInt("Energy", this.getEnergy().getEnergyStored());

        return tag;
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        this.getEnergy().setEnergy(tag.getInt("Energy"));
    }

    @Override
    public void tick() {
        boolean mark = false;

        World world = this.getWorld();
        if (world != null && !world.isRemote()) {

        }

        if (this.oldEnergy != this.getEnergy().getEnergyStored()) {
            this.oldEnergy = this.getEnergy().getEnergyStored();
            if (!mark)
                mark = true;
        }

        if (mark)
            this.markDirty();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.isRemoved() && cap == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.orEmpty(cap, LazyOptional.of(this::getEnergy));
        }

        return super.getCapability(cap, side);
    }

    public abstract CustomEnergyStorage getEnergy();

    public static class Basic extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory = new BaseItemStackHandler(10);
        private final CustomEnergyStorage energy = new CustomEnergyStorage(500000);

        public Basic() {
            super(ModTileEntities.BASIC_AUTO_TABLE.get());
        }

        @Override
        public BaseItemStackHandler getInventory() {
            return this.inventory;
        }

        @Override
        public ITextComponent getDisplayName() {
            return Localizable.of("container.extendedcrafting.basic_table").build();
        }

        @Override
        public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
            return BasicAutoTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, this.data);
        }

        @Override
        public CustomEnergyStorage getEnergy() {
            return this.energy;
        }
    }

    public static class Advanced extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory = new BaseItemStackHandler(26);
        private final CustomEnergyStorage energy = new CustomEnergyStorage(1000000);

        public Advanced() {
            super(ModTileEntities.ADVANCED_AUTO_TABLE.get());
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
            return AdvancedAutoTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, this.data);
        }

        @Override
        public CustomEnergyStorage getEnergy() {
            return this.energy;
        }
    }

    public static class Elite extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory = new BaseItemStackHandler(50);
        private final CustomEnergyStorage energy = new CustomEnergyStorage(2000000);

        public Elite() {
            super(ModTileEntities.ELITE_AUTO_TABLE.get());
        }

        @Override
        public BaseItemStackHandler getInventory() {
            return this.inventory;
        }

        @Override
        public ITextComponent getDisplayName() {
            return Localizable.of("container.extendedcrafting.elite_table").build();
        }

        @Override
        public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
            return EliteAutoTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, this.data);
        }

        @Override
        public CustomEnergyStorage getEnergy() {
            return this.energy;
        }
    }

    public static class Ultimate extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory = new BaseItemStackHandler(82);
        private final CustomEnergyStorage energy = new CustomEnergyStorage(4000000);

        public Ultimate() {
            super(ModTileEntities.ULTIMATE_AUTO_TABLE.get());
        }

        @Override
        public BaseItemStackHandler getInventory() {
            return this.inventory;
        }

        @Override
        public ITextComponent getDisplayName() {
            return Localizable.of("container.extendedcrafting.ultimate_table").build();
        }

        @Override
        public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
            return UltimateAutoTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, this.data);
        }

        @Override
        public CustomEnergyStorage getEnergy() {
            return this.energy;
        }
    }
}
