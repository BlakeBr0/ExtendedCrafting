package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class CompressorTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final BaseItemStackHandler inventory;
	private final BaseItemStackHandler recipeInventory;
	private final EnergyStorage energy;
	private final LazyOptional<IEnergyStorage> capability = LazyOptional.of(this::getEnergy);
	private CompressorRecipe recipe;
	private ItemStack materialStack = ItemStack.EMPTY;
	private int materialCount;
	private int progress;
	private boolean ejecting = false;
	private int oldEnergy;
	private boolean inputLimit = true;

	public CompressorTileEntity(BlockPos pos, BlockState state) {
		super(ModTileEntities.COMPRESSOR.get(), pos, state);
		this.inventory = createInventoryHandler(null);
		this.recipeInventory = new BaseItemStackHandler(2);
		this.energy = new EnergyStorage(ModConfigs.COMPRESSOR_POWER_CAPACITY.get());
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.materialCount = tag.getInt("MaterialCount");
		this.materialStack = ItemStack.of(tag.getCompound("MaterialStack"));
		this.progress = tag.getInt("Progress");
		this.ejecting = tag.getBoolean("Ejecting");
		this.energy.deserializeNBT(tag.get("Energy"));
		this.inputLimit = tag.getBoolean("InputLimit");
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag = super.save(tag);
		tag.putInt("MaterialCount", this.materialCount);
		tag.put("MaterialStack", this.materialStack.serializeNBT());
		tag.putInt("Progress", this.progress);
		tag.putBoolean("Ejecting", this.ejecting);
		tag.putInt("Energy", this.energy.getEnergyStored());
		tag.putBoolean("InputLimit", this.inputLimit);

		return tag;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (!this.isRemoved() && cap == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.orEmpty(cap, this.capability);
		}

		return super.getCapability(cap, side);
	}

	@Override
	public Component getDisplayName() {
		return Localizable.of("container.extendedcrafting.compressor").build();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
		return CompressorContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, new SimpleContainerData(0), this.getBlockPos());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CompressorTileEntity tile) {
		var mark = false;
		var output = tile.inventory.getStackInSlot(0);
		var input = tile.inventory.getStackInSlot(1);
		var catalyst = tile.inventory.getStackInSlot(2);

		tile.recipeInventory.setStackInSlot(0, tile.materialStack);
		tile.recipeInventory.setStackInSlot(1, catalyst);

		if (tile.recipe == null || !tile.recipe.matches(tile.recipeInventory)) {
			tile.recipe = (CompressorRecipe) level.getRecipeManager().getRecipeFor(RecipeTypes.COMPRESSOR, tile.recipeInventory.toIInventory(), level).orElse(null);
		}

		if (!level.isClientSide()) {
			if (!input.isEmpty()) {
				if (tile.materialStack.isEmpty() || tile.materialCount <= 0) {
					tile.materialStack = input.copy();
					mark = true;
				}

				if (!tile.inputLimit || (tile.recipe != null && tile.materialCount < tile.recipe.getInputCount())) {
					if (StackHelper.areStacksEqual(input, tile.materialStack)) {
						int consumeAmount = input.getCount();
						if (tile.inputLimit) {
							consumeAmount = Math.min(consumeAmount, tile.recipe.getInputCount() - tile.materialCount);
						}

						input.shrink(consumeAmount);
						tile.materialCount += consumeAmount;

						if (!mark)
							mark = true;
					}
				}
			}

			if (tile.recipe != null && tile.getEnergy().getEnergyStored() > 0) {
				if (tile.materialCount >= tile.recipe.getInputCount()) {
					if (tile.progress >= tile.recipe.getPowerCost()) {
						var result = tile.recipe.assemble(tile.inventory);

						if (StackHelper.canCombineStacks(result, output)) {
							tile.updateResult(result);
							tile.progress = 0;
							tile.materialCount -= tile.recipe.getInputCount();

							if (tile.materialCount <= 0) {
								tile.materialStack = ItemStack.EMPTY;
							}
						}
					} else {
						tile.process(tile.recipe);
					}
				}
			}

			if (tile.ejecting) {
				if (tile.materialCount > 0 && !tile.materialStack.isEmpty() && (output.isEmpty() || StackHelper.areStacksEqual(tile.materialStack, output))) {
					int addCount = Math.min(tile.materialCount, tile.materialStack.getMaxStackSize() - output.getCount());
					if (addCount > 0) {
						var toAdd = StackHelper.withSize(tile.materialStack, addCount, false);

						tile.updateResult(toAdd);
						tile.materialCount -= addCount;

						if (tile.materialCount < 1) {
							tile.materialStack = ItemStack.EMPTY;
							tile.ejecting = false;
						}

						if (tile.progress > 0)
							tile.progress = 0;

						if (!mark)
							mark = true;
					}
				}
			}
		}

		if (tile.oldEnergy != tile.energy.getEnergyStored()) {
			tile.oldEnergy = tile.energy.getEnergyStored();
			if (!mark)
				mark = true;
		}

		if (mark) {
			tile.markDirtyAndDispatch();
		}
	}

	public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
		var inventory = new BaseItemStackHandler(3, onContentsChanged);

		inventory.setOutputSlots(0);
		inventory.setSlotValidator((slot, stack) -> slot == 1);

		return inventory;
	}

	public EnergyStorage getEnergy() {
		return this.energy;
	}

	public ItemStack getMaterialStack() {
		return this.materialStack;
	}

	public boolean hasMaterialStack() {
		return !this.materialStack.isEmpty();
	}

	public int getMaterialCount() {
		return this.materialCount;
	}

	public boolean isEjecting() {
		return this.ejecting;
	}

	public void toggleEjecting() {
		if (this.materialCount > 0) {
			this.ejecting = !this.ejecting;
			this.markDirtyAndDispatch();
		}
	}

	public boolean isLimitingInput() {
		return this.inputLimit;
	}

	public void toggleInputLimit() {
		this.inputLimit = !this.inputLimit;
		this.markDirtyAndDispatch();
	}

	public int getProgress() {
		return this.progress;
	}

	public boolean hasRecipe() {
		return this.recipe != null;
	}

	public CompressorRecipe getActiveRecipe() {
		return this.recipe;
	}

	public int getEnergyRequired() {
		if (this.hasRecipe())
			return this.recipe.getPowerCost();

		return 0;
	}

	public int getMaterialsRequired() {
		if (this.hasRecipe())
			return this.recipe.getInputCount();

		return 0;
	}

	private void process(CompressorRecipe recipe) {
		int extract = recipe.getPowerRate();
		int difference = recipe.getPowerCost() - this.progress;
		if (difference < extract)
			extract = difference;

		int extracted = this.energy.extractEnergy(extract, false);
		this.progress += extracted;
	}

	private void updateResult(ItemStack stack) {
		var result = this.inventory.getStackInSlot(0);

		if (result.isEmpty()) {
			this.inventory.setStackInSlot(0, stack);
		} else {
			this.inventory.setStackInSlot(0, StackHelper.grow(result, stack.getCount()));
		}
	}
}
