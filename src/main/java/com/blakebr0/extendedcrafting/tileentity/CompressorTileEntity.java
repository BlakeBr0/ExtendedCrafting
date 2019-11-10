package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CustomEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.crafting.ExtendedRecipeManager;
import com.blakebr0.extendedcrafting.crafting.SpecialRecipeTypes;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class CompressorTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
	private final BaseItemStackHandler inventory = new BaseItemStackHandler(3);
	private final CustomEnergyStorage energy = new CustomEnergyStorage(ModConfig.confCompressorRFCapacity);
	private final ItemStackHandler recipeInventory = new ItemStackHandler(2);
	private CompressorRecipe recipe;
	private ItemStack materialStack = ItemStack.EMPTY;
	private int materialCount;
	private int progress;
	private boolean ejecting = false;
	private int oldEnergy;
	private boolean inputLimit = true;
	protected IIntArray data = new IIntArray() {
		@Override
		public int get(int i) {
			switch (i) {
				case 0:
					return 0;
				default:
					return 0;
			}
		}

		@Override
		public void set(int i, int value) {

		}

		@Override
		public int size() {
			return 0;
		}
	};

	public CompressorTileEntity() {
		super(ModTileEntities.COMPRESSOR.get());
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);
		this.materialCount = tag.getInt("MaterialCount");
		this.materialStack = ItemStack.read(tag.getCompound("MaterialStack"));
		this.progress = tag.getInt("Progress");
		this.ejecting = tag.getBoolean("Ejecting");
		this.energy.setEnergy(tag.getInt("Energy"));
		this.inputLimit = tag.getBoolean("InputLimit");
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		tag = super.write(tag);
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
		if (this.getWorld() == null || this.getWorld().isRemote())
			return;

		boolean mark = false;
		ItemStack output = this.inventory.getStackInSlot(0);
		ItemStack input = this.inventory.getStackInSlot(1);
		ItemStack catalyst = this.inventory.getStackInSlot(2);

		this.recipeInventory.setStackInSlot(0, this.materialStack);
		this.recipeInventory.setStackInSlot(1, catalyst);

		if (this.recipe == null || this.recipe.matches(this.recipeInventory)) {
			this.recipe = (CompressorRecipe) ExtendedRecipeManager.getInstance().getRecipe(SpecialRecipeTypes.COMPRESSOR, this.recipeInventory);
		}

		if (!input.isEmpty()) {
			if (this.materialStack.isEmpty()) {
				this.materialStack = input.copy();
				mark = true;
			}

			if (!this.inputLimit || (this.recipe != null && this.materialCount < this.recipe.getInputCount())) {
				if (StackHelper.areStacksEqual(input, this.materialStack)) {
					int consumeAmount = input.getCount();
					if (this.inputLimit) {
						consumeAmount = Math.min(consumeAmount, this.recipe.getInputCount() - this.materialCount);
					}

					StackHelper.decrease(input, consumeAmount, false);
					this.materialCount += consumeAmount;
					if (!mark)
						mark = true;
				}
			}
		}

		if (this.recipe != null && this.getEnergy().getEnergyStored() > 0) {
			if (this.materialCount >= this.recipe.getInputCount()) {
				this.process(this.recipe);
				if (this.progress == this.recipe.getPowerCost()) {
					ItemStack recipeOutput = this.recipe.getOutput();
					if ((output.isEmpty() || StackHelper.areStacksEqual(output, recipeOutput)) && output.getCount() < recipeOutput.getMaxStackSize()) {
						this.inventory.insertItem(0, this.recipe.getOutput().copy(), false);
						this.progress = 0;
						this.materialCount -= this.recipe.getInputCount();

						if (this.materialCount <= 0) {
							this.materialStack = ItemStack.EMPTY;
						}
					}
				}
			}
		}

		if (this.ejecting) {
			if (this.materialCount > 0 && !this.materialStack.isEmpty()) {
				ItemStack toAdd = this.materialStack.copy();
				int addCount = Math.min(this.materialCount, toAdd.getMaxStackSize());
				toAdd.setCount(addCount);

				int added = toAdd.getCount() - this.inventory.insertItem(0, toAdd, false).getCount();
				if (added > 0) {
					this.materialCount -= added;
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

		if (this.oldEnergy != this.energy.getEnergyStored()) {
			this.oldEnergy = this.energy.getEnergyStored();
			if (!mark)
				mark = true;
		}

		if (mark)
			this.markDirty();
	}

	@Override
	public ITextComponent getDisplayName() {
		return Localizable.of("container.extendedcrafting.compressor").build();
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return CompressorContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, this.data);
	}

	private void process(CompressorRecipe recipe) {
		int extract = recipe.getPowerRate();
		int difference = recipe.getPowerCost() - this.progress;
		if (difference < extract)
			extract = difference;

		int extracted = this.energy.extractEnergy(extract, false);
		this.progress += extracted;
	}

	public CustomEnergyStorage getEnergy() {
		return this.energy;
	}

	public ItemStack getMaterialStack() {
		return this.materialStack;
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
			this.markDirty();
		}
	}

	public boolean isLimitingInput() {
		return this.inputLimit;
	}

	public void toggleInputLimit() {
		this.inputLimit = !this.inputLimit;
		this.markDirty();
	}

	public int getProgress() {
		return this.progress;
	}
}
