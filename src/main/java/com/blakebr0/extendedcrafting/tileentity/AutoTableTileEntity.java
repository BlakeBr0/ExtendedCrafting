package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.BaseEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import com.blakebr0.extendedcrafting.container.inventory.ExtendedCraftingInventory;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.util.EmptyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AutoTableTileEntity extends BaseInventoryTileEntity implements MenuProvider {
    private final LazyOptional<IEnergyStorage> energyCapability = LazyOptional.of(this::getEnergy);
    private WrappedRecipe recipe;
    private int progress;
    private boolean running = true;
    private boolean isGridChanged = true;

    public AutoTableTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Progress", this.progress);
        tag.putBoolean("Running", this.running);
        tag.put("Energy", this.getEnergy().serializeNBT());
        tag.merge(this.getRecipeStorage().serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.progress = tag.getInt("Progress");
        this.running = tag.getBoolean("Running");
        this.getEnergy().deserializeNBT(tag.get("Energy"));
        this.getRecipeStorage().deserializeNBT(tag);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        // on load we will re-validate the recipe outputs to ensure they are still correct
        if (this.level != null && !this.level.isClientSide()) {
            this.getRecipeStorage().onLoad(this.level, ModRecipeTypes.TABLE.get());
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.isRemoved() && cap == ForgeCapabilities.ENERGY) {
            return ForgeCapabilities.ENERGY.orEmpty(cap, this.energyCapability);
        }

        return super.getCapability(cap, side);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AutoTableTileEntity tile) {
        var mark = false;
        var energy = tile.getEnergy();

        if (tile.running) {
            var recipe = tile.getActiveRecipe();
            var selectedRecipe = tile.getRecipeStorage().getSelectedRecipeGrid();

            if (recipe != null && (selectedRecipe == null || recipe.matchesSavedRecipe(selectedRecipe, level))) {
                var recipeInventory = tile.getRecipeInventory().toIInventory();
                var inventory = tile.getInventory();
                var result = recipe.getCraftingResult(recipeInventory);
                int outputSlot = inventory.getSlots() - 1;
                var output = inventory.getStackInSlot(outputSlot);
                int powerRate = ModConfigs.AUTO_TABLE_POWER_RATE.get();

                if (StackHelper.canCombineStacks(result, output) && energy.getEnergyStored() >= powerRate) {
                    tile.progress++;
                    energy.extractEnergy(powerRate, false);

                    if (tile.progress >= tile.getProgressRequired()) {
                        var remaining = recipe.getRemainingItems(recipeInventory);

                        for (int i = 0; i < recipeInventory.getContainerSize(); i++) {
                            if (!remaining.get(i).isEmpty()) {
                                inventory.setStackInSlot(i, remaining.get(i));
                            } else {
                                inventory.setStackInSlot(i, StackHelper.shrink(inventory.getStackInSlot(i), 1, false));
                            }
                        }

                        tile.updateResult(result, outputSlot);
                        tile.progress = 0;
                        tile.isGridChanged = true;
                    }

                    mark = true;
                }
            } else {
                if (tile.progress > 0) {
                    tile.progress = 0;
                    mark = true;
                }
            }
        } else {
            if (tile.progress > 0) {
                tile.progress = 0;
                mark = true;
            }
        }

        int insertPowerRate = ModConfigs.AUTO_TABLE_INSERT_POWER_RATE.get();

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

        if (mark) {
            tile.markDirtyAndDispatch();
        }
    }

    public int getProgress() {
        return this.progress;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void toggleRunning() {
        this.running = !this.running;
        this.markDirtyAndDispatch();
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
        var newRecipeInventory = BaseItemStackHandler.create(recipeInventory.getSlots());

        for (int i = 0; i < recipeInventory.getSlots(); i++) {
            newRecipeInventory.setStackInSlot(i, recipeInventory.getStackInSlot(i).copy());
        }

        var result = ItemStack.EMPTY;
        var recipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.TABLE.get(), recipeIInventory, level).orElse(null);

        if (recipe != null) {
            result = recipe.assemble(recipeIInventory);
        } else {
            var craftingInventory = new ExtendedCraftingInventory(EmptyContainer.INSTANCE, recipeInventory, 3);
            var vanilla = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInventory, level).orElse(null);

            if (vanilla != null) {
                result = vanilla.assemble(craftingInventory);
            }
        }

        this.getRecipeStorage().setRecipe(index, newRecipeInventory, result);
        this.markDirtyAndDispatch();
    }

    public void deleteRecipe(int index) {
        this.getRecipeStorage().unsetRecipe(index);
        this.markDirtyAndDispatch();
    }

    public WrappedRecipe getActiveRecipe() {
        if (this.level == null)
            return null;

        this.updateRecipeInventory();

        var recipeInventory = this.getRecipeInventory().toIInventory();

        if (this.isGridChanged && (this.recipe == null || !this.recipe.matches(recipeInventory, level))) {
            var recipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.TABLE.get(), recipeInventory, level).orElse(null);

            this.recipe = recipe != null ? new WrappedRecipe(recipe) : null;

            if (this.recipe == null && ModConfigs.TABLE_USE_VANILLA_RECIPES.get() && this instanceof Basic) {
                var craftingInventory = new ExtendedCraftingInventory(EmptyContainer.INSTANCE, this.getRecipeInventory(), 3);
                var vanilla = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInventory, level).orElse(null);

                this.recipe = vanilla != null ? new WrappedRecipe(vanilla, craftingInventory) : null;
            }

            this.isGridChanged = false;
        }

        return this.recipe;
    }

    public abstract int getProgressRequired();

    public abstract BaseItemStackHandler getRecipeInventory();

    public abstract TableRecipeStorage getRecipeStorage();

    public abstract BaseEnergyStorage getEnergy();

    protected void onContentsChanged() {
        this.isGridChanged = true;
        this.markDirtyAndDispatch();
    }

    private void updateRecipeInventory() {
        var inventory = this.getInventory();

        this.getRecipeInventory().setSize(inventory.getSlots() - 1);

        for (int i = 0; i < inventory.getSlots() - 1; i++) {
            var stack = inventory.getStackInSlot(i);
            this.getRecipeInventory().setStackInSlot(i, stack);
        }
    }

    private void updateResult(ItemStack stack, int slot) {
        var inventory = this.getInventory();
        var result = inventory.getStackInSlot(inventory.getSlots() - 1);

        if (result.isEmpty()) {
            inventory.setStackInSlot(slot, stack);
        } else {
            inventory.setStackInSlot(slot, StackHelper.grow(result, stack.getCount()));
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
                return tile.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN);
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

        this.isGridChanged |= isGridChanged;

		if (slotToPut > -1) {
		    int insertPowerRate = ModConfigs.AUTO_TABLE_INSERT_POWER_RATE.get();
            var toInsert = StackHelper.withSize(input, 1, false);

            this.addStackToSlot(toInsert, slotToPut);
			this.getEnergy().extractEnergy(insertPowerRate, false);

			return true;
		}

		return false;
	}

    public static class WrappedRecipe {
        private final Function<Container, ItemStack> resultFunc;
        private final BiFunction<Container, Level, Boolean> matchesFunc;
        private final BiFunction<CraftingContainer, Level, Boolean> matchesSavedRecipeFunc;
        private final Function<Container, NonNullList<ItemStack>> remainingItemsFunc;

        public WrappedRecipe(CraftingRecipe recipe, CraftingContainer craftingInventory) {
            this.resultFunc = inventory -> recipe.assemble(craftingInventory);
            this.matchesFunc = (inventory, level) -> recipe.matches(craftingInventory, level);
            this.matchesSavedRecipeFunc = recipe::matches;
            this.remainingItemsFunc = inventory -> recipe.getRemainingItems(craftingInventory);
        }

        public WrappedRecipe(ITableRecipe recipe) {
            this.resultFunc = recipe::assemble;
            this.matchesFunc = recipe::matches;
            this.matchesSavedRecipeFunc = recipe::matches;
            this.remainingItemsFunc = recipe::getRemainingItems;
        }

        public ItemStack getCraftingResult(Container inventory) {
            return this.resultFunc.apply(inventory);
        }

        public boolean matches(Container inventory, Level level) {
            return this.matchesFunc.apply(inventory, level);
        }

        public boolean matchesSavedRecipe(BaseItemStackHandler inventory, Level level) {
            var size = (int) Math.sqrt(inventory.getSlots());
            return this.matchesSavedRecipeFunc.apply(new ExtendedCraftingInventory(EmptyContainer.INSTANCE, inventory, size), level);
        }

        public NonNullList<ItemStack> getRemainingItems(Container inventory) {
            return this.remainingItemsFunc.apply(inventory);
        }
    }

    public static class Basic extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory;
        private final BaseItemStackHandler recipeInventory;
        private final BaseEnergyStorage energy;
        private final TableRecipeStorage recipeStorage;

        public Basic(BlockPos pos, BlockState state) {
            super(ModTileEntities.BASIC_AUTO_TABLE.get(), pos, state);
            this.inventory = createInventoryHandler(this::onContentsChanged);
            this.recipeInventory = BaseItemStackHandler.create(9);
            this.recipeStorage = new TableRecipeStorage(10);
            this.energy = new BaseEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get(), this::markDirtyAndDispatch);
        }

        @Override
        public BaseItemStackHandler getInventory() {
            return this.inventory;
        }

        @Override
        public Component getDisplayName() {
            return Localizable.of("container.extendedcrafting.basic_table").build();
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
            return BasicAutoTableContainer.create(windowId, playerInventory, this.inventory, this.getBlockPos());
        }

        @Override
        public int getProgressRequired() {
            return 20;
        }

        @Override
        public BaseItemStackHandler getRecipeInventory() {
            return this.recipeInventory;
        }

        @Override
        public TableRecipeStorage getRecipeStorage() {
            return this.recipeStorage;
        }

        @Override
        public BaseEnergyStorage getEnergy() {
            return this.energy;
        }

        public static BaseItemStackHandler createInventoryHandler() {
            return createInventoryHandler(null);
        }

        public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
            return BaseItemStackHandler.create(10, onContentsChanged, builder -> {
                builder.setOutputSlots(9);
                builder.setCanInsert((slot, stack) -> false);
            });
        }
    }

    public static class Advanced extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory;
        private final BaseItemStackHandler recipeInventory;
        private final BaseEnergyStorage energy;
        private final TableRecipeStorage recipeStorage;

        public Advanced(BlockPos pos, BlockState state) {
            super(ModTileEntities.ADVANCED_AUTO_TABLE.get(), pos, state);
            this.inventory = createInventoryHandler(this::onContentsChanged);
            this.recipeInventory = BaseItemStackHandler.create(25);
            this.recipeStorage = new TableRecipeStorage(26);
            this.energy = new BaseEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get() * 2, this::markDirtyAndDispatch);
        }

        @Override
        public BaseItemStackHandler getInventory() {
            return this.inventory;
        }

        @Override
        public Component getDisplayName() {
            return Localizable.of("container.extendedcrafting.advanced_table").build();
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
            return AdvancedAutoTableContainer.create(windowId, playerInventory, this.inventory, this.getBlockPos());
        }

        @Override
        public int getProgressRequired() {
            return 40;
        }

        @Override
        public BaseItemStackHandler getRecipeInventory() {
            return this.recipeInventory;
        }

        @Override
        public TableRecipeStorage getRecipeStorage() {
            return this.recipeStorage;
        }

        @Override
        public BaseEnergyStorage getEnergy() {
            return this.energy;
        }

        public static BaseItemStackHandler createInventoryHandler() {
            return createInventoryHandler(null);
        }

        public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
            return BaseItemStackHandler.create(26, onContentsChanged, builder -> {
                builder.setOutputSlots(25);
                builder.setCanInsert((slot, stack) -> false);
            });
        }
    }

    public static class Elite extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory;
        private final BaseItemStackHandler recipeInventory;
        private final BaseEnergyStorage energy;
        private final TableRecipeStorage recipeStorage;

        public Elite(BlockPos pos, BlockState state) {
            super(ModTileEntities.ELITE_AUTO_TABLE.get(), pos, state);
            this.inventory = createInventoryHandler(this::onContentsChanged);
            this.recipeInventory = BaseItemStackHandler.create(49);
            this.recipeStorage = new TableRecipeStorage(50);
            this.energy = new BaseEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get() * 4, this::markDirtyAndDispatch);
        }

        @Override
        public BaseItemStackHandler getInventory() {
            return this.inventory;
        }

        @Override
        public Component getDisplayName() {
            return Localizable.of("container.extendedcrafting.elite_table").build();
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
            return EliteAutoTableContainer.create(windowId, playerInventory, this.inventory, this.getBlockPos());
        }

        @Override
        public int getProgressRequired() {
            return 60;
        }

        @Override
        public BaseItemStackHandler getRecipeInventory() {
            return this.recipeInventory;
        }

        @Override
        public TableRecipeStorage getRecipeStorage() {
            return this.recipeStorage;
        }

        @Override
        public BaseEnergyStorage getEnergy() {
            return this.energy;
        }

        public static BaseItemStackHandler createInventoryHandler() {
            return createInventoryHandler(null);
        }

        public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
            return BaseItemStackHandler.create(50, onContentsChanged, builder -> {
                builder.setOutputSlots(49);
                builder.setCanInsert((slot, stack) -> false);
            });
        }
    }

    public static class Ultimate extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory;
        private final BaseItemStackHandler recipeInventory;
        private final BaseEnergyStorage energy;
        private final TableRecipeStorage recipeStorage;

        public Ultimate(BlockPos pos, BlockState state) {
            super(ModTileEntities.ULTIMATE_AUTO_TABLE.get(), pos, state);
            this.inventory = createInventoryHandler(this::onContentsChanged);
            this.recipeInventory = BaseItemStackHandler.create(81);
            this.recipeStorage = new TableRecipeStorage(82);
            this.energy = new BaseEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get() * 8, this::markDirtyAndDispatch);
        }

        @Override
        public BaseItemStackHandler getInventory() {
            return this.inventory;
        }

        @Override
        public Component getDisplayName() {
            return Localizable.of("container.extendedcrafting.ultimate_table").build();
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
            return UltimateAutoTableContainer.create(windowId, playerInventory, this.inventory, this.getBlockPos());
        }

        @Override
        public int getProgressRequired() {
            return 80;
        }

        @Override
        public BaseItemStackHandler getRecipeInventory() {
            return this.recipeInventory;
        }

        @Override
        public TableRecipeStorage getRecipeStorage() {
            return this.recipeStorage;
        }

        @Override
        public BaseEnergyStorage getEnergy() {
            return this.energy;
        }

        public static BaseItemStackHandler createInventoryHandler() {
            return createInventoryHandler(null);
        }

        public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
            return BaseItemStackHandler.create(82, onContentsChanged, builder -> {
                builder.setOutputSlots(81);
                builder.setCanInsert((slot, stack) -> false);
            });
        }
    }
}
