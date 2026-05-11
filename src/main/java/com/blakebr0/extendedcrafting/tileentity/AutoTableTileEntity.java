package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CEnergyStorage;
import com.blakebr0.cucumber.helper.ItemResourceHelper;
import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.ContainerDataBuilder;
import com.blakebr0.extendedcrafting.api.TableCraftingInput;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AutoTableTileEntity extends BaseInventoryTileEntity implements MenuProvider {
    @Nullable
    private Either<CraftingRecipe, ITableRecipe> recipe;
    private int progress;
    private boolean running = true;
    private boolean isGridChanged = true;

    protected final ContainerData dataAccess;

    public AutoTableTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        this.dataAccess = ContainerDataBuilder.builder()
                .sync(() -> this.getEnergy().getAmountAsInt(), value -> this.getEnergy().set(value))
                .sync(() -> this.getEnergy().getCapacityAsInt(), value -> this.getEnergy().setMaxCapacity(value))
                .sync(() -> this.progress, value -> this.progress = value)
                .sync(this::getProgressRequired)
                .sync(() -> this.running ? 1 : 0, value -> this.running = value != 0)
                .sync(() -> this.getRecipeStorage().getSelected(), value -> this.getRecipeStorage().setSelected(value))
                .build();
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.progress = input.getIntOr("progress", 0);
        this.running = input.getBooleanOr("running", false);
        this.getEnergy().deserialize(input.childOrEmpty("energy"));
        this.getRecipeStorage().deserialize(input.childOrEmpty("recipe_storage"));
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("progress", this.progress);
        output.putBoolean("running", this.running);
        output.putChild("energy", this.getEnergy());
        output.putChild("recipe_storage", this.getRecipeStorage());
    }

    @Override
    public void onLoad() {
        super.onLoad();

        // on load, we will re-validate the recipe outputs to ensure they are still correct
        if (this.level != null && !this.level.isClientSide()) {
            this.getRecipeStorage().validate(inventory -> {
                var tableInventory = TableCraftingInput.of(inventory.width(), inventory.height(), inventory.items(), this.getTier());
                return this.getRecipeManager()
                        .getRecipeFor(ModRecipeTypes.TABLE.get(), tableInventory, this.level)
                        .map(r -> r.value().assemble(tableInventory))
                        .orElse(ItemStack.EMPTY);
            });
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AutoTableTileEntity tile) {
        var energy = tile.getEnergy();

        if (tile.running) {
            var recipe = tile.getActiveRecipe();

            if (recipe != null && tile.matchesSelectedRecipe(recipe)) {
                var recipeInventory = tile.getRecipeInventory();
                var inventory = tile.getInventory();
                var result = recipe.map(v -> v.assemble(recipeInventory), t -> t.assemble(recipeInventory));
                int outputSlot = inventory.size() - 1;
                int powerRate = ModConfigs.AUTO_TABLE_POWER_RATE.get();

                if (energy.getAmountAsInt() >= powerRate) {
                    if (ItemResourceHelper.canCombine(inventory, outputSlot, result)) {
                        try (var tx = Transaction.openRoot()) {
                            tile.progress++;

                            energy.extract(powerRate, tx);

                            if (tile.progress >= tile.getProgressRequired()) {
                                var remaining = recipe.map(v -> v.getRemainingItems(recipeInventory), t -> t.getRemainingItems(recipeInventory));

                                for (int k = 0; k < recipeInventory.height(); k++) {
                                    for (int l = 0; l < recipeInventory.width(); l++) {
                                        var size = (recipeInventory.tier() * 2) + 1;
                                        var index = l + recipeInventory.left() + (k + recipeInventory.top()) * size;
                                        var remainingStack = remaining.get(l + k * recipeInventory.width());
                                        var currentStack = inventory.getResource(index);

                                        if (!currentStack.isEmpty()) {
                                            inventory.extract(index, currentStack, 1, tx, true);
                                        }

                                        if (!remainingStack.isEmpty()) {
                                            inventory.insert(index, ItemResource.of(remainingStack), remainingStack.count(), tx, true);
                                        }
                                    }
                                }

                                inventory.insert(outputSlot, ItemResource.of(result), result.count(), tx, true);

                                tile.progress = 0;
                                tile.isGridChanged = true;
                            }

                            tx.commit();

                            tile.setChangedFast();
                        }
                    }
                }
            } else {
                if (tile.progress > 0) {
                    tile.progress = 0;
                    tile.setChangedFast();
                }
            }
        } else {
            if (tile.progress > 0) {
                tile.progress = 0;
                tile.setChangedFast();
            }
        }

        int insertPowerRate = ModConfigs.AUTO_TABLE_INSERT_POWER_RATE.get();

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

        tile.dispatchIfChanged();
    }

    public int getProgress() {
        return this.progress;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void toggleRunning() {
        this.running = !this.running;
        this.setChangedAndDispatch();
    }

    public void selectRecipe(int index) {
        this.getRecipeStorage().setSelected(index);
        this.setChangedAndDispatch();
    }

    public void saveRecipe(int index) {
        var level = this.getLevel();
        if (level == null)
            return;

        var inventory = this.getRecipeInventory();
        var recipe = this.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.FLUX_CRAFTER.get(), inventory, level)
                .orElse(null);

        var result = ItemStack.EMPTY;

        if (recipe != null) {
            result = recipe.value().assemble(inventory);
        } else {
            var vanilla = this.getRecipeManager()
                    .getRecipeFor(RecipeType.CRAFTING, inventory, level)
                    .orElse(null);

            if (vanilla != null) {
                result = vanilla.value().assemble(inventory);
            }
        }

        this.getRecipeStorage().setRecipe(index, this.getInventory(), result);
        this.setChangedAndDispatch();
    }

    public void deleteRecipe(int index) {
        this.getRecipeStorage().unsetRecipe(index);
        this.setChangedAndDispatch();
    }

    public TableCraftingInput getRecipeInventory() {
        var inventory = this.getInventory();
        var size = (int) Math.sqrt(inventory.size() - 1);
        return TableCraftingInput.of(size, size, inventory.getStacks().subList(0, inventory.size() - 1), this.getTier());
    }

    public Either<CraftingRecipe, ITableRecipe> getActiveRecipe() {
        if (this.level == null)
            return null;

        var inventory = this.getRecipeInventory();

        if (this.isGridChanged && (this.recipe == null || !this.recipe.map(v -> v.matches(inventory, this.level), t -> t.matches(inventory, this.level)))) {
            var recipe = this.getRecipeManager()
                    .getRecipeFor(ModRecipeTypes.TABLE.get(), inventory, this.level)
                    .map(RecipeHolder::value)
                    .orElse(null);

            if (recipe != null) {
                this.recipe = Either.right(recipe);
            } else {
                this.recipe = null;
            }

            if (recipe == null && ModConfigs.TABLE_USE_VANILLA_RECIPES.get() && this instanceof Basic) {
                var vanillaRecipe = this.getRecipeManager()
                        .getRecipeFor(RecipeType.CRAFTING, inventory, this.level)
                        .map(RecipeHolder::value)
                        .orElse(null);

                if (vanillaRecipe != null) {
                    this.recipe = Either.left(vanillaRecipe);
                } else {
                    this.recipe = null;
                }
            }

            this.isGridChanged = false;
        }

        return this.recipe;
    }

    public boolean matchesSelectedRecipe(Either<CraftingRecipe, ITableRecipe> recipe) {
        if (this.level == null)
            return false;

        var selectedRecipe = this.getRecipeStorage().getSelectedRecipe();
        if (selectedRecipe == null)
            return true;

        var inventory = this.getRecipeInventory();

        return recipe.map(v -> v.matches(inventory, this.level), t -> t.matches(inventory, this.level));
    }

    public abstract int getProgressRequired();

    public abstract TableRecipeStorage getRecipeStorage();

    public abstract CEnergyStorage getEnergy();

    public abstract int getTier();

    protected void onContentsChanged(int slot, ItemStack oldStack) {
        if (!this.isGridChanged) {
            this.isGridChanged = true;
            this.setChanged();
        }
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

        this.isGridChanged |= isGridChanged;

        if (slotToPut > -1) {
            int insertPowerRate = ModConfigs.AUTO_TABLE_INSERT_POWER_RATE.get();

            this.getInventory().insert(slotToPut, input, 1, tx, true);
            this.getEnergy().extract(insertPowerRate, tx);

            return true;
        }

        return false;
    }

    public static class Basic extends AutoTableTileEntity {
        private final CItemStacksHandler inventory;
        private final CEnergyStorage energy;
        private final TableRecipeStorage recipeStorage;

        public Basic(BlockPos pos, BlockState state) {
            super(ModTileEntities.BASIC_AUTO_TABLE.get(), pos, state);
            this.inventory = createInventoryHandler(this::onContentsChanged);
            this.recipeStorage = new TableRecipeStorage(10);
            this.energy = new CEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get(), _ -> this.setChangedFast());
        }

        @Override
        public CItemStacksHandler getInventory() {
            return this.inventory;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("container.extendedcrafting.basic_table");
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
            return new BasicAutoTableContainer(windowId, playerInventory, this.inventory, this.dataAccess, this.getBlockPos());
        }

        @Override
        public int getProgressRequired() {
            return ModConfigs.AUTO_TABLE_TIME_REQUIRED.get();
        }

        @Override
        public TableRecipeStorage getRecipeStorage() {
            return this.recipeStorage;
        }

        @Override
        public CEnergyStorage getEnergy() {
            return this.energy;
        }

        @Override
        public int getTier() {
            return 1;
        }

        public static CItemStacksHandler createInventoryHandler() {
            return createInventoryHandler(null);
        }

        public static CItemStacksHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
            return CItemStacksHandler.create(10, onContentsChanged, builder -> {
                builder.setOutputSlots(9);
                builder.setCanInsert((_, _) -> false);
            });
        }
    }

    public static class Advanced extends AutoTableTileEntity {
        private final CItemStacksHandler inventory;
        private final CEnergyStorage energy;
        private final TableRecipeStorage recipeStorage;

        public Advanced(BlockPos pos, BlockState state) {
            super(ModTileEntities.ADVANCED_AUTO_TABLE.get(), pos, state);
            this.inventory = createInventoryHandler(this::onContentsChanged);
            this.recipeStorage = new TableRecipeStorage(26);
            this.energy = new CEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get() * 2, _ -> this.setChangedFast());
        }

        @Override
        public CItemStacksHandler getInventory() {
            return this.inventory;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("container.extendedcrafting.advanced_table");
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
            return new AdvancedAutoTableContainer(windowId, playerInventory, this.inventory, this.dataAccess, this.getBlockPos());
        }

        @Override
        public int getProgressRequired() {
            return ModConfigs.AUTO_TABLE_TIME_REQUIRED.get() * 2;
        }

        @Override
        public TableRecipeStorage getRecipeStorage() {
            return this.recipeStorage;
        }

        @Override
        public CEnergyStorage getEnergy() {
            return this.energy;
        }

        @Override
        public int getTier() {
            return 2;
        }

        public static CItemStacksHandler createInventoryHandler() {
            return createInventoryHandler(null);
        }

        public static CItemStacksHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
            return CItemStacksHandler.create(26, onContentsChanged, builder -> {
                builder.setOutputSlots(25);
                builder.setCanInsert((_, _) -> false);
            });
        }
    }

    public static class Elite extends AutoTableTileEntity {
        private final CItemStacksHandler inventory;
        private final CEnergyStorage energy;
        private final TableRecipeStorage recipeStorage;

        public Elite(BlockPos pos, BlockState state) {
            super(ModTileEntities.ELITE_AUTO_TABLE.get(), pos, state);
            this.inventory = createInventoryHandler(this::onContentsChanged);
            this.recipeStorage = new TableRecipeStorage(50);
            this.energy = new CEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get() * 4, _ -> this.setChangedFast());
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
            return new EliteAutoTableContainer(windowId, playerInventory, this.inventory, this.dataAccess, this.getBlockPos());
        }

        @Override
        public int getProgressRequired() {
            return ModConfigs.AUTO_TABLE_TIME_REQUIRED.get() * 3;
        }

        @Override
        public TableRecipeStorage getRecipeStorage() {
            return this.recipeStorage;
        }

        @Override
        public CEnergyStorage getEnergy() {
            return this.energy;
        }

        @Override
        public int getTier() {
            return 3;
        }

        public static CItemStacksHandler createInventoryHandler() {
            return createInventoryHandler(null);
        }

        public static CItemStacksHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
            return CItemStacksHandler.create(50, onContentsChanged, builder -> {
                builder.setOutputSlots(49);
                builder.setCanInsert((_, _) -> false);
            });
        }
    }

    public static class Ultimate extends AutoTableTileEntity {
        private final CItemStacksHandler inventory;
        private final CEnergyStorage energy;
        private final TableRecipeStorage recipeStorage;

        public Ultimate(BlockPos pos, BlockState state) {
            super(ModTileEntities.ULTIMATE_AUTO_TABLE.get(), pos, state);
            this.inventory = createInventoryHandler(this::onContentsChanged);
            this.recipeStorage = new TableRecipeStorage(82);
            this.energy = new CEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get() * 8, _ -> this.setChangedFast());
        }

        @Override
        public CItemStacksHandler getInventory() {
            return this.inventory;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("container.extendedcrafting.ultimate_table");
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
            return new UltimateAutoTableContainer(windowId, playerInventory, this.inventory, this.dataAccess, this.getBlockPos());
        }

        @Override
        public int getProgressRequired() {
            return ModConfigs.AUTO_TABLE_TIME_REQUIRED.get() * 4;
        }

        @Override
        public TableRecipeStorage getRecipeStorage() {
            return this.recipeStorage;
        }

        @Override
        public CEnergyStorage getEnergy() {
            return this.energy;
        }

        @Override
        public int getTier() {
            return 4;
        }

        public static CItemStacksHandler createInventoryHandler() {
            return createInventoryHandler(null);
        }

        public static CItemStacksHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
            return CItemStacksHandler.create(82, onContentsChanged, builder -> {
                builder.setOutputSlots(81);
                builder.setCanInsert((_, _) -> false);
            });
        }
    }
}
