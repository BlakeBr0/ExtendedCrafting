package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.AutoEnderCrafterContainer;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Optional;

public class AutoEnderCrafterTileEntity extends EnderCrafterTileEntity implements MenuProvider {
    private final CEnergyStorage energy;
    private final TableRecipeStorage recipeStorage;

    public AutoEnderCrafterTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.AUTO_ENDER_CRAFTER.get(), pos, state);
        this.energy = new CEnergyStorage(ModConfigs.AUTO_ENDER_CRAFTER_POWER_CAPACITY.get(), _ -> this.setChangedFast());
        this.recipeStorage = new TableRecipeStorage(10);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.energy.deserialize(input);
        this.recipeStorage.deserialize(input);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.energy.serialize(output);
        this.recipeStorage.serialize(output);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        // on load, we will re-validate the recipe outputs to ensure they are still correct
        if (this.level != null && !this.level.isClientSide()) {
            this.getRecipeStorage().validate(inventory -> ((ServerLevel) this.level).recipeAccess()
                    .getRecipeFor(ModRecipeTypes.ENDER_CRAFTER.get(), inventory, this.level)
                    .map(r -> r.value().assemble(inventory))
                    .orElse(ItemStack.EMPTY)
            );
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return AutoEnderCrafterContainer.create(windowId, playerInventory, this.getInventory(), this.getBlockPos());
    }

    @Override
    public TableRecipeStorage getRecipeStorage() {
        return this.recipeStorage;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AutoEnderCrafterTileEntity tile) {
        EnderCrafterTileEntity.tick(level, pos, state, tile);

        int insertPowerRate = ModConfigs.AUTO_ENDER_CRAFTER_INSERT_POWER_RATE.get();

        if (tile.getEnergy().getEnergyStored() >= insertPowerRate) {
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
    }

    public void selectRecipe(int index) {
        this.getRecipeStorage().setSelected(index);
        this.setChangedAndDispatch();
    }

    public void saveRecipe(int index) {
        var level = this.getLevel();
        if (level == null)
            return;

        var inventory = this.getInventory().toCraftingInput(3, 3, 0, 9);
        var recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.ENDER_CRAFTER.get(), inventory, level)
                .orElse(null);

        var result = ItemStack.EMPTY;

        if (recipe != null) {
            result = recipe.value().assemble(inventory, level.registryAccess());
        }

        this.getRecipeStorage().setRecipe(index, this.getInventory(), result);
        this.setChangedAndDispatch();
    }

    public void deleteRecipe(int index) {
        this.getRecipeStorage().unsetRecipe(index);
        this.setChangedAndDispatch();
    }

    public CEnergyStorage getEnergy() {
        return this.energy;
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

    private Optional<IItemHandler> getAboveInventory() {
        var level = this.getLevel();
        var pos = this.getBlockPos().above();

        if (level != null) {
            var capability = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, Direction.DOWN);
            return Optional.ofNullable(capability);
        }

        return Optional.empty();
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
		    int insertPowerRate = ModConfigs.AUTO_ENDER_CRAFTER_INSERT_POWER_RATE.get();
            var toInsert = StackHelper.withSize(input, 1, false);

            this.addStackToSlot(toInsert, slotToPut);
			this.getEnergy().extractEnergy(insertPowerRate, false);

			return true;
		}

		return false;
	}
}
