package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CEnergyStorage;
import com.blakebr0.cucumber.tileentity.BaseTileEntity;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.FluxAlternatorContainer;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class FluxAlternatorTileEntity extends BaseTileEntity implements MenuProvider {
    private final CEnergyStorage energy;

    public FluxAlternatorTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.FLUX_ALTERNATOR.get(), pos, state);
        this.energy = new CEnergyStorage(ModConfigs.FLUX_ALTERNATOR_POWER_CAPACITY.get(), _ -> this.setChangedAndDispatch());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.energy.deserialize(input);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.energy.serialize(output);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.extendedcrafting.flux_alternator");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return FluxAlternatorContainer.create(windowId, playerInventory, this.getBlockPos());
    }

    public CEnergyStorage getEnergy() {
        return this.energy;
    }
}
