package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CustomEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class AutoTableTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
    private ITableRecipe recipe;
    private int progress;
    private boolean running = true;
    private int oldEnergy;
    protected final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return AutoTableTileEntity.this.getEnergy().getEnergyStored();
                case 1:
                    return AutoTableTileEntity.this.getEnergy().getMaxEnergyStored();
                case 2:
                    return AutoTableTileEntity.this.getProgress();
                case 3:
                    return AutoTableTileEntity.this.getProgressRequired();
                case 4:
                    return AutoTableTileEntity.this.isRunning() ? 1 : 0;
                case 5:
                    return AutoTableTileEntity.this.getRecipeStorage().getSelected();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {

        }

        @Override
        public int size() {
            return 6;
        }
    };

    public AutoTableTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.putInt("Progress", this.progress);
        tag.putBoolean("Running", this.running);
        tag.putInt("Energy", this.getEnergy().getEnergyStored());
        tag.merge(this.getRecipeStorage().serializeNBT());

        return tag;
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        this.progress = tag.getInt("Progress");
        this.running = tag.getBoolean("Running");
        this.getEnergy().setEnergy(tag.getInt("Energy"));
        this.getRecipeStorage().deserializeNBT(tag);
    }

    @Override
    public void tick() {
        boolean mark = false;

        World world = this.getWorld();
        CustomEnergyStorage energy = this.getEnergy();
        if (world != null) {
            if (this.running) {
                this.updateRecipeInventory();
                IInventory recipeInventory = this.getRecipeInventory().toIInventory();
                if (this.recipe == null || !this.recipe.matches(recipeInventory, world)) {
                    this.recipe = world.getRecipeManager().getRecipe(RecipeTypes.TABLE, recipeInventory, world).orElse(null);
                }

                if (!world.isRemote()) {
                    if (this.recipe != null) {
                        BaseItemStackHandler inventory = this.getInventory();
                        ItemStack result = this.recipe.getCraftingResult(recipeInventory);
                        int outputSlot = inventory.getSlots() - 1;
                        ItemStack output = inventory.getStackInSlot(outputSlot);
                        int powerRate = ModConfigs.AUTO_TABLE_POWER_RATE.get();

                        if (StackHelper.canCombineStacks(result, output) && energy.getEnergyStored() >= powerRate) {
                            this.progress++;
                            energy.extractEnergy(powerRate, false);

                            if (this.progress >= this.getProgressRequired()) {
                                for (int i = 0; i < recipeInventory.getSizeInventory(); i++) {
                                    inventory.extractItemSuper(i, 1, false);
                                }

                                this.updateResult(result, outputSlot);
                                this.progress = 0;
                            }

                            mark = true;
                        }
                    } else {
                        if (this.progress > 0) {
                            this.progress = 0;
                            mark = true;
                        }
                    }
                }
            } else {
                if (this.progress > 0) {
                    this.progress = 0;
                    mark = true;
                }
            }

            int insertPowerRate = ModConfigs.AUTO_TABLE_INSERT_POWER_RATE.get();
            if (!world.isRemote() && this.getEnergy().getEnergyStored() >= insertPowerRate) {
                int selected = this.getRecipeStorage().getSelected();
                if (selected != -1) {
                    this.getAboveInventory().ifPresent(handler -> {
                        for (int i = 0; i < handler.getSlots(); i++) {
                            ItemStack stack = handler.getStackInSlot(i);
                            if (!stack.isEmpty() && !handler.extractItem(i, 1, true).isEmpty()) {
                                boolean inserted = this.tryInsertItemIntoGrid(stack);

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

        if (this.oldEnergy != energy.getEnergyStored()) {
            this.oldEnergy = energy.getEnergyStored();
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

    public int getProgress() {
        return this.progress;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void toggleRunning() {
        this.running = !this.running;
        this.markDirty();
    }

    public void selectRecipe(int index) {
        this.getRecipeStorage().setSelected(index);
        this.markDirtyAndDispatch();
    }

    public void saveRecipe(int index) {
        World world = this.getWorld();
        if (world == null)
            return;

        this.updateRecipeInventory();

        BaseItemStackHandler recipeInventory = this.getRecipeInventory();
        IInventory recipeIInventory = recipeInventory.toIInventory();
        ITableRecipe recipe = world.getRecipeManager().getRecipe(RecipeTypes.TABLE, recipeIInventory, world).orElse(null);
        if (recipe != null) {
            BaseItemStackHandler newRecipeInventory = new BaseItemStackHandler(recipeInventory.getSlots());
            for (int i = 0; i < recipeInventory.getSlots(); i++) {
                newRecipeInventory.setStackInSlot(i, recipeInventory.getStackInSlot(i).copy());
            }

            this.getRecipeStorage().setRecipe(index, newRecipeInventory, recipe.getCraftingResult(recipeIInventory));
            this.markDirtyAndDispatch();
        }
    }

    public void deleteRecipe(int index) {
        this.getRecipeStorage().unsetRecipe(index);
        this.markDirtyAndDispatch();
    }

    public abstract int getProgressRequired();

    public abstract BaseItemStackHandler getRecipeInventory();

    public abstract TableRecipeStorage getRecipeStorage();

    public abstract CustomEnergyStorage getEnergy();

    protected boolean canInsertStack(int slot, ItemStack stack) {
        return false;
    }

    private void updateRecipeInventory() {
        BaseItemStackHandler inventory = this.getInventory();
        this.getRecipeInventory().setSize(inventory.getSlots() - 1);
        for (int i = 0; i < inventory.getSlots() - 1; i++) {
            this.getRecipeInventory().setStackInSlot(i, inventory.getStackInSlot(i));
        }
    }

    private void updateResult(ItemStack stack, int slot) {
        BaseItemStackHandler inventory = this.getInventory();
        ItemStack result = inventory.getStackInSlot(inventory.getSlots() - 1);
        if (result.isEmpty()) {
            inventory.setStackInSlot(slot, stack);
        } else {
            inventory.setStackInSlot(slot, StackHelper.increase(result.copy(), stack.getCount()));
        }
    }

    private void addStackToSlot(ItemStack stack, int slot) {
        BaseItemStackHandler inventory = this.getInventory();
        ItemStack stackInSlot = inventory.getStackInSlot(slot);
        if (stackInSlot.isEmpty()) {
            inventory.setStackInSlot(slot, stack);
        } else {
            inventory.setStackInSlot(slot, StackHelper.increase(stackInSlot.copy(), stack.getCount()));
        }
    }

    private LazyOptional<IItemHandler> getAboveInventory() {
        World world = this.getWorld();
        BlockPos pos = this.getPos().up();

        if (world != null) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null) {
                return tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN);
            }
        }

        return LazyOptional.empty();
    }

    private boolean tryInsertItemIntoGrid(ItemStack input) {
		ItemStack toInsert = StackHelper.withSize(input.copy(), 1, false);
        BaseItemStackHandler inventory = this.getInventory();
        ItemStack stackToPut = ItemStack.EMPTY;
        int slotToPut = -1;

        BaseItemStackHandler recipe = this.getRecipeStorage().getSelectedRecipe();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack slot = inventory.getStackInSlot(i);
            ItemStack recipeStack = recipe.getStackInSlot(i);
            if (((slot.isEmpty() || StackHelper.areStacksEqual(input, slot)) && StackHelper.areStacksEqual(input, recipeStack))) {
                if (slot.isEmpty() || slot.getCount() < slot.getMaxStackSize()) {
                    if (slot.isEmpty()) {
                        slotToPut = i;
                        break;
                    } else if (stackToPut.isEmpty() || slot.getCount() < stackToPut.getCount()) {
                        slotToPut = i;
                        stackToPut = slot.copy();
                    }
                }
            }
        }

		if (slotToPut > -1) {
		    int insertPowerRate = ModConfigs.AUTO_TABLE_INSERT_POWER_RATE.get();
			this.addStackToSlot(toInsert, slotToPut);
			this.getEnergy().extractEnergy(insertPowerRate, false);

			return true;
		}

		return false;
	}

    public static class Basic extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory = new BaseItemStackHandler(10, this::markDirtyAndDispatch);
        private final BaseItemStackHandler recipeInventory = new BaseItemStackHandler(9);
        private final TableRecipeStorage recipeStorage = new TableRecipeStorage(10);
        private final CustomEnergyStorage energy = new CustomEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get());

        public Basic() {
            super(ModTileEntities.BASIC_AUTO_TABLE.get());
            this.inventory.setOutputSlots(9);
            this.inventory.setSlotValidator(super::canInsertStack);
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
            return BasicAutoTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, this.data, this.getPos());
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
        public CustomEnergyStorage getEnergy() {
            return this.energy;
        }
    }

    public static class Advanced extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory = new BaseItemStackHandler(26, this::markDirtyAndDispatch);
        private final BaseItemStackHandler recipeInventory = new BaseItemStackHandler(25);
        private final TableRecipeStorage recipeStorage = new TableRecipeStorage(26);
        private final CustomEnergyStorage energy = new CustomEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get() * 2);

        public Advanced() {
            super(ModTileEntities.ADVANCED_AUTO_TABLE.get());
            this.inventory.setOutputSlots(25);
            this.inventory.setSlotValidator(super::canInsertStack);
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
            return AdvancedAutoTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, this.data, this.getPos());
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
        public CustomEnergyStorage getEnergy() {
            return this.energy;
        }
    }

    public static class Elite extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory = new BaseItemStackHandler(50, this::markDirtyAndDispatch);
        private final BaseItemStackHandler recipeInventory = new BaseItemStackHandler(49);
        private final TableRecipeStorage recipeStorage = new TableRecipeStorage(50);
        private final CustomEnergyStorage energy = new CustomEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get() * 4);

        public Elite() {
            super(ModTileEntities.ELITE_AUTO_TABLE.get());
            this.inventory.setOutputSlots(49);
            this.inventory.setSlotValidator(super::canInsertStack);
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
            return EliteAutoTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, this.data, this.getPos());
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
        public CustomEnergyStorage getEnergy() {
            return this.energy;
        }
    }

    public static class Ultimate extends AutoTableTileEntity {
        private final BaseItemStackHandler inventory = new BaseItemStackHandler(82, this::markDirtyAndDispatch);
        private final BaseItemStackHandler recipeInventory = new BaseItemStackHandler(81);
        private final TableRecipeStorage recipeStorage = new TableRecipeStorage(82);
        private final CustomEnergyStorage energy = new CustomEnergyStorage(ModConfigs.AUTO_TABLE_POWER_CAPACITY.get() * 8);

        public Ultimate() {
            super(ModTileEntities.ULTIMATE_AUTO_TABLE.get());
            this.inventory.setOutputSlots(81);
            this.inventory.setSlotValidator(super::canInsertStack);
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
            return UltimateAutoTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, this.data, this.getPos());
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
        public CustomEnergyStorage getEnergy() {
            return this.energy;
        }
    }
}
