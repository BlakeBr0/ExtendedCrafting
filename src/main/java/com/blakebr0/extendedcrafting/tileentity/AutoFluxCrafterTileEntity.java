package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.AutoFluxCrafterContainer;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class AutoFluxCrafterTileEntity extends FluxCrafterTileEntity implements MenuProvider {
    private final EnergyStorage energy;
    private final TableRecipeStorage recipeStorage;
    private int oldEnergy;

    public AutoFluxCrafterTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.AUTO_FLUX_CRAFTER.get(), pos, state);
        this.energy = new EnergyStorage(ModConfigs.AUTO_FLUX_CRAFTER_POWER_CAPACITY.get());
        this.recipeStorage = new TableRecipeStorage(10);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Energy", this.energy.serializeNBT());
        tag.merge(this.recipeStorage.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.energy.deserializeNBT(tag.get("Energy"));
        this.recipeStorage.deserializeNBT(tag);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        // on load we will re-validate the recipe outputs to ensure they are still correct
        if (this.level != null && !this.level.isClientSide()) {
            this.getRecipeStorage().onLoad(this.level, ModRecipeTypes.FLUX_CRAFTER.get());
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return AutoFluxCrafterContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.getInventory(), this.getBlockPos());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.isRemoved() && cap == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.orEmpty(cap, LazyOptional.of(this::getEnergy));
        }

        return super.getCapability(cap, side);
    }

    @Override
    public TableRecipeStorage getRecipeStorage() {
        return this.recipeStorage;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AutoFluxCrafterTileEntity tile) {
        FluxCrafterTileEntity.tick(level, pos, state, tile);

        int insertPowerRate = ModConfigs.AUTO_FLUX_CRAFTER_INSERT_POWER_RATE.get();
        if (!level.isClientSide() && tile.getEnergy().getEnergyStored() >= insertPowerRate) {
            int selected = tile.getRecipeStorage().getSelected();
            if (selected != -1) {
                tile.getAboveInventory().ifPresent(handler -> {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        var stack = handler.getStackInSlot(i);

                        if (!stack.isEmpty() && !handler.extractItem(i, 1, true).isEmpty()) {
                            var inserted = tile.tryInsertItemIntoGrid(stack);

                            if (inserted) {
                                handler.extractItem(i, 1, false);
                                break;
                            }
                        }
                    }
                });
            }
        }

        if (tile.oldEnergy != tile.energy.getEnergyStored()) {
            tile.oldEnergy = tile.energy.getEnergyStored();
            tile.markDirtyAndDispatch();
        }
    }

    public void selectRecipe(int index) {
        this.getRecipeStorage().setSelected(index);
        this.markDirtyAndDispatch();
    }

    public void saveRecipe(int index) {
        var level = this.getLevel();
        if (level == null)
            return;

        this.updateRecipeInventory();

        var recipeInventory = this.getRecipeInventory();
        var recipeIInventory = recipeInventory.toIInventory();
        var newRecipeInventory = new BaseItemStackHandler(recipeInventory.getSlots());

        for (int i = 0; i < recipeInventory.getSlots(); i++) {
            newRecipeInventory.setStackInSlot(i, recipeInventory.getStackInSlot(i).copy());
        }

        var result = ItemStack.EMPTY;
        var recipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.FLUX_CRAFTER.get(), recipeIInventory, level).orElse(null);

        if (recipe != null) {
            result = recipe.assemble(recipeIInventory);
        }

        this.getRecipeStorage().setRecipe(index, newRecipeInventory, result);
        this.markDirtyAndDispatch();
    }

    public void deleteRecipe(int index) {
        this.getRecipeStorage().unsetRecipe(index);
        this.markDirtyAndDispatch();
    }

    public EnergyStorage getEnergy() {
        return this.energy;
    }

    private void updateRecipeInventory() {
        var inventory = this.getInventory();

        this.getRecipeInventory().setSize(inventory.getSlots() - 1);

        for (int i = 0; i < inventory.getSlots() - 1; i++) {
            var stack = inventory.getStackInSlot(i);
            this.getRecipeInventory().setStackInSlot(i, stack);
        }
    }

    private void addStackToSlot(ItemStack stack, int slot) {
        var inventory = this.getInventory();
        var stackInSlot = inventory.getStackInSlot(slot);

        if (stackInSlot.isEmpty()) {
            inventory.setStackInSlot(slot, stack);
        } else {
            inventory.setStackInSlot(slot, StackHelper.grow(stackInSlot, stack.getCount()));
        }
    }

    private LazyOptional<IItemHandler> getAboveInventory() {
        var level = this.getLevel();
        var pos = this.getBlockPos().above();

        if (level != null) {
            var tile = level.getBlockEntity(pos);

            if (tile != null) {
                return tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN);
            }
        }

        return LazyOptional.empty();
    }

    private boolean tryInsertItemIntoGrid(ItemStack input) {
        var inventory = this.getInventory();
        var stackToPut = ItemStack.EMPTY;
        var recipe = this.getRecipeStorage().getSelectedRecipe();
        int slotToPut = -1;
        boolean isGridChanged = false;

        // last slot in the inventory is the output slot
        var slots = inventory.getSlots() - 1;

        for (int i = 0; i < slots; i++) {
            var slot = inventory.getStackInSlot(i);
            var recipeStack = recipe.getStackInSlot(i);

            if (((slot.isEmpty() || StackHelper.areStacksEqual(input, slot)) && StackHelper.areStacksEqual(input, recipeStack))) {
                if (slot.isEmpty() || slot.getCount() < slot.getMaxStackSize()) {
                    if (slot.isEmpty()) {
                        slotToPut = i;
                        isGridChanged = true;
                        break;
                    } else if (stackToPut.isEmpty() || slot.getCount() < stackToPut.getCount()) {
                        slotToPut = i;
                        stackToPut = slot;
                    }
                }
            }
        }

        this.isGridChanged = isGridChanged;

		if (slotToPut > -1) {
		    int insertPowerRate = ModConfigs.AUTO_FLUX_CRAFTER_INSERT_POWER_RATE.get();
            var toInsert = StackHelper.withSize(input, 1, false);

            this.addStackToSlot(toInsert, slotToPut);
			this.getEnergy().extractEnergy(insertPowerRate, false);

			return true;
		}

		return false;
	}
}
