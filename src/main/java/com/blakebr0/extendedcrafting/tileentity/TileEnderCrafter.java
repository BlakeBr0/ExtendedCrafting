package com.blakebr0.extendedcrafting.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.tile.TileEntityBase;
import com.blakebr0.extendedcrafting.block.BlockEnderAlternator;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.endercrafter.IEnderCraftingRecipe;
import com.blakebr0.extendedcrafting.crafting.table.TableCrafting;
import com.blakebr0.extendedcrafting.lib.EmptyContainer;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class TileEnderCrafter extends TileEntityBase implements IInventory, ITickable, IExtendedTable {

	private NonNullList<ItemStack> matrix = NonNullList.withSize(9, ItemStack.EMPTY);
	private ItemStack result = ItemStack.EMPTY;
	private int progress;
	private int progressReq;
	
	@Override
	public void update() {
		if (!this.getWorld().isRemote) {
			TableCrafting crafting = new TableCrafting(new EmptyContainer(), this);
			IEnderCraftingRecipe recipe = EnderCrafterRecipeManager.getInstance().findMatchingRecipe(crafting, this.getWorld());
			ItemStack result = recipe == null ? ItemStack.EMPTY : ((IRecipe) recipe).getCraftingResult(crafting);
			ItemStack output = this.getResult();
			if (!result.isEmpty() && (output.isEmpty() || StackHelper.canCombineStacks(output, result))) {
				List<BlockPos> alternators = this.getAlternatorPositions();
				int alternatorCount = alternators.size();

				if (alternatorCount > 0) {
					this.progress(alternatorCount, recipe.getEnderCrafterTimeSeconds());
					
					for (BlockPos pos : alternators) {
						if (this.getWorld().isAirBlock(pos.up())) {
							((WorldServer) this.getWorld()).spawnParticle(EnumParticleTypes.PORTAL, false, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 1, 0, 0, 0, 0.1D);
						}
					}
					
					if (this.progress >= this.progressReq) {
						for (int i = 0; i < this.matrix.size(); i++) {
							this.decrStackSize(i, 1);
						}
						
						this.updateResult(result);
						this.progress = 0;
					}
					
					this.markDirty();
				}
			} else {
				if (this.progress > 0 || this.progressReq > 0) {
					this.progress = 0;
					this.progressReq = 0;
					this.markDirty();
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.merge(ItemStackHelper.saveAllItems(tag, this.matrix));
		
		if (!this.result.isEmpty()) {
			tag.setTag("Result", this.result.serializeNBT());
		} else {
			tag.removeTag("Result");
		}
		
		tag.setInteger("Progress", this.progress);
		tag.setInteger("ProgressReq", this.progressReq);
		
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		ItemStackHelper.loadAllItems(tag, this.matrix);
		this.result = new ItemStack(tag.getCompoundTag("Result"));
		
		this.progress = tag.getInteger("Progress");
		this.progressReq = tag.getInteger("ProgressReq");
	}
	
	@Override
	public ItemStack getResult() {
		return this.result;
	}
	
	@Override
	public NonNullList<ItemStack> getMatrix() {
		return this.matrix;
	}

	@Override
	public void setResult(ItemStack result) {
		this.result = result;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.matrix.set(slot, stack);
	}
	
	@Override
	public int getLineSize() {
		return 3;
	}
	
	public void updateResult(ItemStack result) {
		if (this.result.isEmpty()) {
			this.setResult(result);
		} else {
			this.result.grow(result.getCount());
		}
	}
	
	public List<BlockPos> getAlternatorPositions() {
		ArrayList<BlockPos> alternators = new ArrayList<BlockPos>();
		Iterable<BlockPos> blocks = this.getPos().getAllInBox(this.getPos().add(-3, -3, -3), this.getPos().add(3, 3, 3));
		for (BlockPos aoePos : blocks) {
			Block block = this.getWorld().getBlockState(aoePos).getBlock();
			if (block instanceof BlockEnderAlternator) {
				alternators.add(aoePos);
			}
		}
		
		return alternators;
	}
	
	public int getProgress() {
		return this.progress;
	}
	
	private void progress(int alternators, int timeRequired) {
		this.progress++;
		
		int timeReq = 20 * timeRequired;
		this.progressReq = (int) Math.max(timeReq - (timeReq * (ModConfig.confEnderAlternatorEff * alternators)), 20);
	}
	
	public int getProgressRequired() {
		return this.progressReq;
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
	public int getSizeInventory() {
		return 9;
	}

	@Override
	public boolean isEmpty() {
		return this.matrix.isEmpty() && this.result.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.matrix.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.matrix, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.matrix, index);
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
		return false;
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
		this.matrix = NonNullList.withSize(9, ItemStack.EMPTY);
		this.setResult(ItemStack.EMPTY);
	}
}
