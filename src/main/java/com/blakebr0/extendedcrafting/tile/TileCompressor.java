package com.blakebr0.extendedcrafting.tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.blakebr0.cucumber.energy.EnergyStorageCustom;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.VanillaPacketDispatcher;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileCompressor extends TileEntity implements ISidedInventory, ITickable {

	private NonNullList<ItemStack> inventoryStacks = NonNullList.<ItemStack> withSize(3, ItemStack.EMPTY);
	private final EnergyStorageCustom energy = new EnergyStorageCustom(ModConfig.confCompressorRFCapacity);
	private ItemStack materialStack = ItemStack.EMPTY;
	private int materialCount;
	private int progress;
	private boolean ejecting = false;
	private int oldEnergy;
	
	private List<CompressorRecipe> getValidRecipes(ItemStack stack) {
		List<CompressorRecipe> valid = new ArrayList<CompressorRecipe>();
		if (!stack.isEmpty()) {
			for (CompressorRecipe recipe : CompressorRecipeManager.getInstance().getRecipes()) {
				Object input = recipe.getInput();
				if (input instanceof ItemStack) {
					ItemStack inputStack = (ItemStack) input;
					if (inputStack.getItemDamage() == 32767) {
						if (inputStack.getItem() == stack.getItem()) {
							valid.add(recipe);
						}
					}
					
					if (inputStack.isItemEqual(stack)) {
						valid.add(recipe);
					}
				} else if (input instanceof List) {
					Iterator<ItemStack> itr = ((List<ItemStack>) input).iterator();
					while (itr.hasNext()) {
						ItemStack next = itr.next();
						if (next.getMetadata() == 32767 || next.getMetadata() == stack.getMetadata()) {
							if (next.getItem() == stack.getItem()) {
								valid.add(recipe);
							}
						}
					}
				}
			}
		}
		
		return valid;
	}

	@Override
	public void update() {
		boolean mark = false;

		if (!this.getWorld().isRemote) {
			CompressorRecipe recipe = this.getRecipe();
			ItemStack output = this.getStackInSlot(0);
			ItemStack input = this.getStackInSlot(1);

			if (!input.isEmpty()) {
				if (this.materialStack.isEmpty()) {
					this.materialStack = input.copy();
					if (!mark) {
						mark = true;
					}
				}
				
				// TODO: Possibly come up with a system to deal with differing itemstacks?
				if (StackHelper.areStacksEqual(input, this.materialStack)) {
					int consumeAmount = input.getCount();
					StackHelper.decrease(input, consumeAmount, false);
					this.materialCount += consumeAmount;
					if (!mark) {
						mark = true;
					}
				}
			}

			if (recipe != null && this.getEnergy().getEnergyStored() > 0) {
				if (this.materialCount >= recipe.getInputCount()) {
					this.process(recipe);
					if (this.progress == recipe.getPowerCost()) {
						ItemStack recipeOutput = recipe.getOutput();
						if ((output.isEmpty() || StackHelper.areStacksEqual(output, recipeOutput)) && output.getCount() < recipeOutput.getMaxStackSize()) {
							this.addStackToSlot(0, recipe.getOutput());
							if (recipe.consumeCatalyst()) {
								StackHelper.decrease(this.getStackInSlot(2), 1, false);
							}
							
							this.progress = 0;
							this.materialCount -= recipe.getInputCount();
							
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
					
					int added = this.addStackToSlot(0, toAdd);
					if (added > 0) {
						this.materialCount -= added;
						if (this.materialCount < 1) {
							this.materialStack = ItemStack.EMPTY;
							this.ejecting = false;
						}
						if (this.progress > 0) {
							this.progress = 0;
						}
						if (!mark) {
							mark = true;
						}
					}
				}
			}
		}

		if (this.oldEnergy != this.energy.getEnergyStored()) {
			this.oldEnergy = this.energy.getEnergyStored();
			if (!mark) {
				mark = true;
			}
		}

		if (mark) {
			this.markDirty();
		}
	}

	public CompressorRecipe getRecipe() {
		List<CompressorRecipe> recipes = this.getValidRecipes(this.materialStack);
		if (!recipes.isEmpty()) {
			for (CompressorRecipe recipe : recipes) {
				ItemStack mat = this.materialStack;
				Object input = recipe.getInput();

				if (input instanceof ItemStack) {
					if ((StackHelper.areItemsEqual((ItemStack) input, mat, true)) && this.getStackInSlot(2).isItemEqual(recipe.getCatalyst())) {
						return recipe;
					}
				} else if (input instanceof List) {
					Iterator<ItemStack> itr = ((List<ItemStack>) input).iterator();
					while (itr.hasNext()) {
						ItemStack next = itr.next();
						if ((StackHelper.areItemsEqual(next, mat, true)) && this.getStackInSlot(2).isItemEqual(recipe.getCatalyst())) {
							return recipe;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	private boolean process(CompressorRecipe recipe) {
		int extract = recipe.getPowerRate();
		int difference = recipe.getPowerCost() - this.progress;
		if (difference < extract) {
			extract = difference;
		}
		
		int extracted = this.getEnergy().extractEnergy(extract, false);
		this.progress += extracted;
		if (this.progress >= recipe.getPowerCost()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, this.inventoryStacks);
		compound.setInteger("MaterialCount", this.materialCount);
		if (this.materialStack == null) {
			this.materialStack = ItemStack.EMPTY;
		}
		if (!this.materialStack.isEmpty()) {
			compound.setTag("MaterialStack", this.materialStack.serializeNBT());
		}
		compound.setInteger("Progress", this.progress);
		compound.setBoolean("Ejecting", this.ejecting);
		compound.setInteger("Energy", this.energy.getEnergyStored());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventoryStacks = NonNullList.<ItemStack> withSize(3, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventoryStacks);
		this.materialCount = compound.getInteger("MaterialCount");
		this.materialStack = new ItemStack(compound.getCompoundTag("MaterialStack"));
		this.progress = compound.getInteger("Progress");
		this.ejecting = compound.getBoolean("Ejecting");
		this.energy.setEnergy(compound.getInteger("Energy"));
	}

	/**
	 * Tries to add a stack to the specified slot, returns the amount added
	 * @param slot the slot to insert to
	 * @param stack the stack to insert
	 * @return the amount added
	 */
	public int addStackToSlot(int slot, ItemStack stack) {
		ItemStack slotStack = this.getStackInSlot(slot);
		if (slotStack.isEmpty()) {
			this.setInventorySlotContents(slot, stack);
			return stack.getCount();
		} else {
			if (StackHelper.areStacksEqual(stack, slotStack) && slotStack.getCount() < slotStack.getMaxStackSize()) {
				ItemStack newStack = slotStack.copy();
				int newSize = Math.min(slotStack.getCount() + stack.getCount(), slotStack.getMaxStackSize());
				newStack.setCount(newSize);
				this.setInventorySlotContents(slot, newStack);
				return newSize - slotStack.getCount();
			}
		}
		
		return 0;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), -1, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public final NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	public EnergyStorageCustom getEnergy() {
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

	public int getProgress() {
		return this.progress;
	}

	@Override
	public int getSizeInventory() {
		return this.inventoryStacks.size();
	}

	@Override
	public boolean isEmpty() {
		return !this.inventoryStacks.stream().anyMatch(s -> !s.isEmpty());
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return (ItemStack) this.inventoryStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.inventoryStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventoryStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = (ItemStack) this.inventoryStacks.get(index);
		boolean flag = !stack.isEmpty() && StackHelper.areStacksEqual(stack, itemstack);
		this.inventoryStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq(this.getPos().add(0.5, 0.5, 0.5)) <= 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 2) {
			return false;
		} else if (index != 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {

	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? new int[] { 0 } : side == EnumFacing.UP ? new int[] { 1 } : new int[] { 0, 1 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return this.isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing side) {
		return this.getCapability(capability, side) != null;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) new SidedInvWrapper(this, facing);
		} else if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		}
		
		return super.getCapability(capability, facing);
	}
}
