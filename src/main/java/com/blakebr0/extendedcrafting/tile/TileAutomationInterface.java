package com.blakebr0.extendedcrafting.tile;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.blakebr0.cucumber.energy.EnergyStorageCustom;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.cucumber.util.VanillaPacketDispatcher;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableCrafting;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.lib.EmptyContainer;
import com.blakebr0.extendedcrafting.lib.FakeRecipeHandler;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileAutomationInterface extends TileEntity implements ITickable, ISidedInventory {
	
	private final ItemStackHandler inventory = new StackHandler(2);
	private final ItemStackHandler recipe = new FakeRecipeHandler();
	private final EnergyStorageCustom energy = new EnergyStorageCustom(ModConfig.confInterfaceRFCapacity);
	private int oldEnergy;
	private ItemStack result = ItemStack.EMPTY;
	private boolean hasRecipe = false;
	private int autoInsert = -1;
	private int autoExtract = -1;
	private boolean autoEject = false;
	private boolean smartInsert = true;
	protected int ticks = 0;
	private int tickRollover = 19; //  Max(itemRate,powerRate) * LCM(itemRate,powerRate) - 1
	private int itemTickRate = 10;
	private int powerTickRate = 4;
	public int powerMultiplier = 1;
	
	@Override
	public void update() {
		boolean mark = false;
		this.ticks++;
		if (!this.getWorld().isRemote) {
			ItemStack input = this.getInventory().getStackInSlot(0);
			ItemStack output = this.getInventory().getStackInSlot(1);
			boolean hasTable = this.hasTable();
			
			if (!input.isEmpty() && this.getEnergy().getEnergyStored() >= ModConfig.confInterfaceRFRate * powerMultiplier) {
				this.handleInput(input, hasTable && this.hasRecipe());
			}
			
			if (hasTable && this.hasRecipe() && this.getEnergy().getEnergyStored() >= ModConfig.confInterfaceRFRate * powerMultiplier && this.ticks % this.itemTickRate == 0) {
				this.handleOutput(output);
			}
			
			if (this.getInserterFace() != null && this.getEnergy().getEnergyStored() >= ModConfig.confInterfaceRFRate * powerMultiplier && this.ticks % this.powerTickRate == 0) {
				TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(this.getInserterFace()));
				if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
					IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
					for (int i = 0; i < handler.getSlots(); i++) {
						ItemStack stack = handler.getStackInSlot(i);
						if (!stack.isEmpty()) {
							ItemStack toInsert = StackHelper.withSize(stack.copy(), 1, false);
							if (this.checkStackSmartly(toInsert)) {
								if (input.isEmpty() || (StackHelper.canCombineStacks(input, toInsert))) {
									this.getInventory().insertItem(0, toInsert, false);
									handler.extractItem(i, 1, false);
									this.getEnergy().extractEnergy(ModConfig.confInterfaceRFRate * powerMultiplier, false);
									break;
								}
							}
						}
					}
				}
			}
			
			if (this.getExtractorFace() != null && this.getEnergy().getEnergyStored() >= ModConfig.confInterfaceRFRate * powerMultiplier && this.ticks % this.powerTickRate == 0) {
				TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(this.getExtractorFace()));
				if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
					IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
					for (int i = 0; i < handler.getSlots(); i++) {
						ItemStack stack = handler.getStackInSlot(i);
						if (!output.isEmpty()) {
							ItemStack toInsert = StackHelper.withSize(output.copy(), 1, false);
							if (stack.isEmpty() || (StackHelper.canCombineStacks(stack, toInsert))) {
								handler.insertItem(i, toInsert, false);
								output.shrink(1);
								this.getEnergy().extractEnergy(ModConfig.confInterfaceRFRate * powerMultiplier, false);
								break;
							}
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
		
		if (this.ticks >= this.tickRollover ) {
			this.ticks = 0;
		}
		
		if (mark) {
			this.markDirty();
		}
	}
	
	private void handleInput(ItemStack input, boolean canInsert) {
		ItemStack output = this.getInventory().getStackInSlot(1);
		ItemStack toInsert = StackHelper.withSize(input.copy(), 1, false);
		IExtendedTable table = null;
		IInventory matrix = null;
		int slotToPut = -1;

		if (canInsert) {
			table = this.getTable();
			ItemStackHandler recipe = this.getRecipe();
			matrix = (IInventory) table;
			
			ItemStack stackToPut = ItemStack.EMPTY;
			for (int i = 0; i < matrix.getSizeInventory(); i++) {
				ItemStack slot = matrix.getStackInSlot(i);
				ItemStack recipeStack = recipe.getStackInSlot(i);
				if (((slot.isEmpty() || StackHelper.areStacksEqual(input, slot)) && StackHelper.areStacksEqual(input, recipeStack))) {
					if (slot.isEmpty() || slot.getCount() < slot.getMaxStackSize()) {
						if (slot.isEmpty()) {
							slotToPut = i;
							break;
						} else if (stackToPut.isEmpty() || (!stackToPut.isEmpty() && slot.getCount() < stackToPut.getCount())) {
							slotToPut = i;
							stackToPut = slot.copy();
						}
					}
				}
			}
		}
		
		if (matrix != null && slotToPut > -1) {
			this.insertItem(matrix, slotToPut, toInsert);
			input.shrink(1);
			
			if (this.isCraftingTable()) {
				table.setResult(TableRecipeManager.getInstance().findMatchingRecipe(new TableCrafting(new EmptyContainer(), table), this.getWorld()));
			}
			
			this.getEnergy().extractEnergy(ModConfig.confInterfaceRFRate * powerMultiplier, false); 
		} else if (this.getAutoEject() && (output.isEmpty() || StackHelper.canCombineStacks(output, toInsert))) { 
			this.getInventory().insertItem(1, toInsert, false);
			input.shrink(1);
			this.getEnergy().extractEnergy(ModConfig.confInterfaceRFRate * powerMultiplier, false);
		}
	}
	
	public void handleOutput(ItemStack output) {
		IExtendedTable table = this.getTable();
		ItemStack result = table.getResult();
		IInventory matrix = (IInventory) table;
		if (!result.isEmpty() && (output.isEmpty() || StackHelper.canCombineStacks(output, result))) {				
			if (this.getEnergy().getEnergyStored() >= ModConfig.confInterfaceRFRate * powerMultiplier) {
				ItemStack toInsert = result.copy();
				
				if (this.isEnderCrafter()) {
					table.setResult(ItemStack.EMPTY);
				} else {
					for (int i = 0; i < matrix.getSizeInventory(); i++) {
						ItemStack slotStack = matrix.getStackInSlot(i);
						ItemStack recipeStack = this.getRecipe().getStackInSlot(i);
						if (!recipeStack.isEmpty() && (slotStack.isEmpty() || !StackHelper.areStacksEqual(recipeStack, slotStack))) {
							return;
						}
					}
					
					for (int i = 0; i < matrix.getSizeInventory(); i++) {
						ItemStack slotStack = matrix.getStackInSlot(i);
						if (!slotStack.isEmpty()) {
							if (slotStack.getItem().hasContainerItem(slotStack) && slotStack.getCount() == 1) {
								matrix.setInventorySlotContents(i, slotStack.getItem().getContainerItem(slotStack));
							} else {
								matrix.decrStackSize(i, 1);
							}
						}
					}
					
					table.setResult(TableRecipeManager.getInstance().findMatchingRecipe(new TableCrafting(new EmptyContainer(), table), this.getWorld()));
				}
				
				this.getInventory().insertItem(1, toInsert, false);
				this.getEnergy().extractEnergy(ModConfig.confInterfaceRFRate * powerMultiplier, false);
			}
		}
	}
		
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.merge(this.inventory.serializeNBT());
		tag.merge(this.recipe.serializeNBT());
		tag.setInteger("Energy", this.energy.getEnergyStored());
		tag.setTag("Result", this.result.serializeNBT());
		tag.setBoolean("HasRecipe", this.hasRecipe);
		tag.setInteger("AutoInsert", this.autoInsert);
		tag.setInteger("AutoExtract", this.autoExtract);
		tag.setBoolean("AutoEject", this.autoEject);
		tag.setBoolean("SmartInsert", this.smartInsert);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.inventory.deserializeNBT(tag);
		this.recipe.deserializeNBT(tag);
		this.energy.setEnergy(tag.getInteger("Energy"));
		this.result = new ItemStack(tag.getCompoundTag("Result"));
		this.hasRecipe = tag.getBoolean("HasRecipe");
		this.autoInsert = tag.getInteger("AutoInsert");
		this.autoExtract = tag.getInteger("AutoExtract");
		this.autoEject = tag.getBoolean("AutoEject");
		this.smartInsert = tag.getBoolean("SmartInsert");
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

	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}
	
	@Override
	public int getSizeInventory() {
		return this.inventory.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < this.inventory.getSlots(); i++) {
			if (!this.inventory.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventory.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return index >= 0 && index < this.inventory.getSlots() && !this.inventory.getStackInSlot(index).isEmpty() && count > 0 ? this.inventory.getStackInSlot(index).splitStack(count) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (index >= 0 && index < this.inventory.getSlots()) {
			this.inventory.setStackInSlot(index, ItemStack.EMPTY);
			return ItemStack.EMPTY;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = (ItemStack) this.inventory.getStackInSlot(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.inventory.setStackInSlot(index, stack);

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
		return this.isUseableByPlayer(player);
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
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
		return new int[] { 0, 1 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index == 1;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing side) {
		return this.getCapability(capability, side) != null;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) new SidedInvWrapper(this, side);
		} else if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		}
		
		return super.getCapability(capability, side);
	}
	
	public IItemHandlerModifiable getInventory() {
		return this.inventory;
	}
	
	public ItemStackHandler getRecipe() {
		return this.recipe;
	}
	
	public ItemStack getResult() {
		return this.result;
	}
	
	public EnergyStorageCustom getEnergy() {
		return this.energy;
	}
	
	public IExtendedTable getTable() {
		TileEntity tile = this.getWorld().getTileEntity(this.getPos().down());
		return tile != null && tile instanceof IExtendedTable ? (IExtendedTable) tile : null;
	}
	
	public boolean hasTable() {
		return this.getTable() != null;
	}
	
	public boolean isEnderCrafter() {
		return this.getTable() instanceof TileEnderCrafter;
	}
	
	public boolean isCraftingTable() {
		IExtendedTable table = this.getTable();
		return table instanceof TileBasicCraftingTable
			|| table instanceof TileAdvancedCraftingTable
			|| table instanceof TileEliteCraftingTable
			|| table instanceof TileUltimateCraftingTable;
	}
	
	public boolean hasRecipe() {
		return this.hasRecipe;
	}
	
	public void setHasRecipe(boolean hasRecipe) {
		this.hasRecipe = hasRecipe;
	}
	
	public void saveRecipe() {
		ItemStackHandler recipe = this.getRecipe();
		IExtendedTable table = this.getTable();
		NonNullList<ItemStack> matrix = table.getMatrix();
		recipe.setSize(matrix.size());
		for (int i = 0; i < matrix.size(); i++) {
			recipe.setStackInSlot(i, matrix.get(i).copy());
		}
		
		if (this.isEnderCrafter()) {
			this.result = EnderCrafterRecipeManager.getInstance().findMatchingRecipe(new TableCrafting(new EmptyContainer(), table), this.getWorld());
		} else {
			ItemStack result = table.getResult();
			if (result != null) {
				this.result = result;
			}
		}
		
		this.setHasRecipe(true);
		this.markDirty();
	}
	
	public void clearRecipe() {
		ItemStackHandler recipe = this.getRecipe();
		recipe.setSize(1);
		this.result = ItemStack.EMPTY;
		this.setHasRecipe(false);
		this.markDirty();
	}
	
	public EnumFacing getInserterFace() {
		return this.autoInsert > -1 && this.autoInsert < EnumFacing.values().length ? EnumFacing.values()[this.autoInsert] : null;
	}
	
	public EnumFacing getExtractorFace() {
		return this.autoExtract > -1 && this.autoExtract < EnumFacing.values().length ? EnumFacing.values()[this.autoExtract] : null;
	}
	
	public String getInserterFaceName() {
		return this.getInserterFace() != null ? this.getInserterFace().getName().toUpperCase(Locale.ROOT) : Utils.localize("ec.interface.none");
	}
	
	public String getExtractorFaceName() {
		return this.getExtractorFace() != null ? this.getExtractorFace().getName().toUpperCase(Locale.ROOT) : Utils.localize("ec.interface.none");
	}
	
	public void switchInserter() {
		if (this.autoInsert >= EnumFacing.values().length - 1) {
			this.autoInsert = -1;
		} else {
			this.autoInsert++;
			if (this.autoInsert == EnumFacing.DOWN.getIndex()) {
				this.autoInsert++;
			}
		}
		
		this.markDirty();
	}
	
	public void switchExtractor() {
		if (this.autoExtract >= EnumFacing.values().length - 1) {
			this.autoExtract = -1;
		} else {
			this.autoExtract++;
			if (this.autoExtract == EnumFacing.DOWN.getIndex()) {
				this.autoExtract++;
			}
		}
		
		this.markDirty();
	}
	
	public void disableInserter() {
		if (this.autoInsert != -1) {
			this.autoInsert = -1;
			this.markDirty();
		}
	}
	
	public void disableExtractor() {
		if (this.autoExtract != -1) {
			this.autoExtract = -1;
			this.markDirty();
		}
	}
	
	public boolean checkStackSmartly(ItemStack stack) {
		if (!this.getSmartInsert()) return true;
		if (!this.hasTable()) return false;
		if (!this.hasRecipe()) return false;
		
		NonNullList<ItemStack> matrix = this.getTable().getMatrix();
		for (int i = 0; i < matrix.size(); i++) {
			ItemStack slotStack = matrix.get(i);
			ItemStack recipeStack = this.getRecipe().getStackInSlot(i);
			if (StackHelper.areStacksEqual(stack, recipeStack) && (slotStack.isEmpty() || StackHelper.canCombineStacks(stack, slotStack))) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean getAutoEject() {
		return this.autoEject;
	}
	
	public void toggleAutoEject() {
		this.autoEject = !this.autoEject;
		this.markDirty();
	}
	
	public boolean getSmartInsert() {
		return this.smartInsert;
	}
	
	public void toggleSmartInsert() {
		this.smartInsert = !this.smartInsert;
		this.markDirty();
	}
	
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq(this.getPos().add(0.5, 0.5, 0.5)) <= 64;
	}
	
	private int insertItem(IInventory matrix, int slot, ItemStack stack) {
		ItemStack slotStack = matrix.getStackInSlot(slot);
		if (slotStack.isEmpty()) {
			matrix.setInventorySlotContents(slot, stack);
			return stack.getCount();
		} else {
			if (StackHelper.areStacksEqual(stack, slotStack) && slotStack.getCount() < slotStack.getMaxStackSize()) {
				ItemStack newStack = slotStack.copy();
				int newSize = Math.min(slotStack.getCount() + stack.getCount(), slotStack.getMaxStackSize());
				newStack.setCount(newSize);
				matrix.setInventorySlotContents(slot, newStack);
				return newSize - slotStack.getCount();
			}
		}
		
		return 0;
	}
	
	private int GCD(int a, int b) {
		if(b==0) return a;
		return GCD(b,a%b);
	}
	
	public void setRates(int item, int power) {
		this.itemTickRate = item;
		this.powerTickRate = power;
		this.tickRollover = Math.max(item, power) * ( ( item * power ) / GCD(item,power) ) - 1;
		this.ticks = 0;
	}
	
	class StackHandler extends ItemStackHandler {
		
		StackHandler(int size) {
			super(size);
		}

		@Override
		public void onContentsChanged(int slot) {
			TileAutomationInterface.this.markDirty();
		}
	}
}
