package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.BaseEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
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
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class CompressorTileEntity extends BaseInventoryTileEntity implements TickableBlockEntity, MenuProvider {
	private final BaseItemStackHandler inventory;
	private final BaseItemStackHandler recipeInventory;
	private final BaseEnergyStorage energy;
	private final LazyOptional<IEnergyStorage> capability = LazyOptional.of(this::getEnergy);
	private CompressorRecipe recipe;
	private ItemStack materialStack = ItemStack.EMPTY;
	private int materialCount;
	private int progress;
	private boolean ejecting = false;
	private int oldEnergy;
	private boolean inputLimit = true;

	public CompressorTileEntity() {
		super(ModTileEntities.COMPRESSOR.get());
		this.inventory = new BaseItemStackHandler(3);
		this.recipeInventory = new BaseItemStackHandler(2);
		this.energy = new BaseEnergyStorage(ModConfigs.COMPRESSOR_POWER_CAPACITY.get());

		this.inventory.setSlotValidator(this::canInsertStack);
		this.inventory.setOutputSlots(0);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		this.materialCount = tag.getInt("MaterialCount");
		this.materialStack = ItemStack.of(tag.getCompound("MaterialStack"));
		this.progress = tag.getInt("Progress");
		this.ejecting = tag.getBoolean("Ejecting");
		this.energy.setEnergy(tag.getInt("Energy"));
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
	public void tick() {
		boolean mark = false;

		Level world = this.getLevel();
		if (world != null) {
			ItemStack output = this.inventory.getStackInSlot(0);
			ItemStack input = this.inventory.getStackInSlot(1);
			ItemStack catalyst = this.inventory.getStackInSlot(2);

			this.recipeInventory.setStackInSlot(0, this.materialStack);
			this.recipeInventory.setStackInSlot(1, catalyst);

			if (this.recipe == null || !this.recipe.matches(this.recipeInventory)) {
				this.recipe = (CompressorRecipe) world.getRecipeManager().getRecipeFor(RecipeTypes.COMPRESSOR, this.recipeInventory.toIInventory(), world).orElse(null);
			}

			if (!world.isClientSide()) {
				if (!input.isEmpty()) {
					if (this.materialStack.isEmpty() || this.materialCount <= 0) {
						this.materialStack = input.copy();
						mark = true;
					}

					if (!this.inputLimit || (this.recipe != null && this.materialCount < this.recipe.getInputCount())) {
						if (StackHelper.areStacksEqual(input, this.materialStack)) {
							int consumeAmount = input.getCount();
							if (this.inputLimit) {
								consumeAmount = Math.min(consumeAmount, this.recipe.getInputCount() - this.materialCount);
							}

							input.shrink(consumeAmount);
							this.materialCount += consumeAmount;

							if (!mark)
								mark = true;
						}
					}
				}

				if (this.recipe != null && this.getEnergy().getEnergyStored() > 0) {
					if (this.materialCount >= this.recipe.getInputCount()) {
						if (this.progress >= this.recipe.getPowerCost()) {
							ItemStack result = this.recipe.getCraftingResult(this.inventory);
							if (StackHelper.canCombineStacks(result, output)) {
								this.updateResult(result);
								this.progress = 0;
								this.materialCount -= this.recipe.getInputCount();

								if (this.materialCount <= 0) {
									this.materialStack = ItemStack.EMPTY;
								}
							}
						} else {
							this.process(this.recipe);
						}
					}
				}

				if (this.ejecting) {
					if (this.materialCount > 0 && !this.materialStack.isEmpty() && (output.isEmpty() || StackHelper.areStacksEqual(this.materialStack, output))) {
						int addCount = Math.min(this.materialCount, this.materialStack.getMaxStackSize() - output.getCount());
						if (addCount > 0) {
							ItemStack toAdd = StackHelper.withSize(this.materialStack, addCount, false);

							this.updateResult(toAdd);
							this.materialCount -= addCount;

							if (this.materialCount < 1) {
								this.materialStack = ItemStack.EMPTY;
								this.ejecting = false;
							}

							if (this.progress > 0)
								this.progress = 0;

							if (!mark)
								mark = true;
						}
					}
				}
			}

			if (this.oldEnergy != this.energy.getEnergyStored()) {
				this.oldEnergy = this.energy.getEnergyStored();
				if (!mark)
					mark = true;
			}

			if (mark) {
				this.markDirtyAndDispatch();
			}
		}
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

	public BaseEnergyStorage getEnergy() {
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
		ItemStack result = this.inventory.getStackInSlot(0);
		if (result.isEmpty()) {
			this.inventory.setStackInSlot(0, stack);
		} else {
			this.inventory.setStackInSlot(0, StackHelper.grow(result, stack.getCount()));
		}
	}

	private boolean canInsertStack(int slot, ItemStack stack) {
		return slot == 1;
	}
}
