package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CEnergyStorage;
import com.blakebr0.cucumber.util.ContainerDataBuilder;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.AutoFluxCrafterContainer;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Optional;

public class AutoFluxCrafterTileEntity extends FluxCrafterTileEntity implements MenuProvider {
    private final CEnergyStorage energy;
    private final TableRecipeStorage recipeStorage;

    private final ContainerData dataAccess;

    public AutoFluxCrafterTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.AUTO_FLUX_CRAFTER.get(), pos, state);
        this.energy = new CEnergyStorage(ModConfigs.AUTO_FLUX_CRAFTER_POWER_CAPACITY.get(), _ -> this.setChangedFast());
        this.recipeStorage = new TableRecipeStorage(10);

        this.dataAccess = ContainerDataBuilder.builder()
                .sync(this.energy::getAmountAsInt, this.energy::set)
                .sync(this.energy::getCapacityAsInt, this.energy::setMaxCapacity)
                .sync(this::getProgress)
                .sync(this::getProgressRequired)
                .sync(this.recipeStorage::getSelected, this.recipeStorage::setSelected)
                .build();
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.energy.deserialize(input.childOrEmpty("energy"));
        this.recipeStorage.deserialize(input.childOrEmpty("recipe_storage"));
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putChild("energy", this.energy);
        output.putChild("recipe_storage", this.recipeStorage);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        // on load, we will re-validate the recipe outputs to ensure they are still correct
        if (this.level != null && !this.level.isClientSide()) {
            this.getRecipeStorage().validate(inventory -> this.getRecipeManager()
                    .getRecipeFor(ModRecipeTypes.FLUX_CRAFTER.get(), inventory, this.level)
                    .map(r -> r.value().assemble(inventory))
                    .orElse(ItemStack.EMPTY)
            );
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return new AutoFluxCrafterContainer(windowId, playerInventory, this.getInventory(), this.dataAccess, this.getBlockPos());
    }

    @Override
    public TableRecipeStorage getRecipeStorage() {
        return this.recipeStorage;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AutoFluxCrafterTileEntity tile) {
        FluxCrafterTileEntity.tick(level, pos, state, tile);

        int insertPowerRate = ModConfigs.AUTO_FLUX_CRAFTER_INSERT_POWER_RATE.get();

        if (tile.getEnergy().getAmountAsInt() >= insertPowerRate) {
            int selected = tile.getRecipeStorage().getSelected();
            if (selected != -1) {
                tile.getAboveInventory().ifPresent(handler -> {
                    for (int i = 0; i < handler.size(); i++) {
                        var resource = handler.getResource(i);

                        try (var tx = Transaction.openRoot()) {
                            if (!resource.isEmpty() && handler.extract(i, resource, 1, tx) == 1) {
                                var inserted = tile.tryInsertItemIntoGrid(resource, tx);
                                if (inserted) {
                                    tx.commit();
                                    break;
                                }
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
        var recipe = this.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.FLUX_CRAFTER.get(), inventory, level)
                .orElse(null);

        var result = ItemStack.EMPTY;

        if (recipe != null) {
            result = recipe.value().assemble(inventory);
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

    private Optional<ResourceHandler<ItemResource>> getAboveInventory() {
        var level = this.getLevel();
        var pos = this.getBlockPos().above();

        if (level != null) {
            var capability = level.getCapability(Capabilities.Item.BLOCK, pos, Direction.DOWN);
            return Optional.ofNullable(capability);
        }

        return Optional.empty();
    }

    private boolean tryInsertItemIntoGrid(ItemResource input, TransactionContext tx) {
        var inventory = this.getInventory();
        var stackToPut = ItemResource.EMPTY;
        var stackAmountToPut = 0;
        var recipe = this.getRecipeStorage().getSelectedRecipe();
        int slotToPut = -1;
        boolean isGridChanged = false;

        // the last slot in the inventory is the output slot
        var slots = inventory.size() - 1;

        for (int i = 0; i < slots; i++) {
            var slot = inventory.getResource(i);
            var count = inventory.getAmountAsInt(i);
            var recipeResource = recipe.getResource(i);

            if (((slot.isEmpty() || input.matches(slot.toStack())) && input.matches(recipeResource.toStack()))) {
                if (slot.isEmpty() || count < slot.getMaxStackSize()) {
                    if (slot.isEmpty()) {
                        slotToPut = i;
                        isGridChanged = true;
                        break;
                    } else if (stackToPut.isEmpty() || count < stackAmountToPut) {
                        slotToPut = i;
                        stackToPut = slot;
                        stackAmountToPut = count;
                    }
                }
            }
        }

        this.isGridChanged = isGridChanged;

        if (slotToPut > -1) {
            int insertPowerRate = ModConfigs.AUTO_FLUX_CRAFTER_INSERT_POWER_RATE.get();

            this.getInventory().insert(slotToPut, input, stackAmountToPut, tx, true);
            this.getEnergy().extract(insertPowerRate, tx);

            return true;
        }

        return false;
    }
}
