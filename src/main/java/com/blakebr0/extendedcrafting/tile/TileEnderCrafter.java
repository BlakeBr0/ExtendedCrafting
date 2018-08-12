package com.blakebr0.extendedcrafting.tile;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.tile.TileEntityBase;
import com.blakebr0.extendedcrafting.block.BlockEnderAlternator;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.ItemStackHandler;

public class TileEnderCrafter extends TileEntityBase implements IExtendedTable, ITickable {

	private ItemStackHandler matrix = new ItemStackHandler(9);
	private ItemStack result = ItemStack.EMPTY;
	private int progress;
	private int progressReq;
	
	@Override
	public void update() {
		if (!this.getWorld().isRemote) {
			ItemStack result = EnderCrafterRecipeManager.getInstance().findMatchingRecipe(this.matrix);
			ItemStack output = this.getResult();
			if (!result.isEmpty() && (output.isEmpty() || StackHelper.canCombineStacks(output, result))) {
				List<BlockPos> alternators = this.getAlternatorPositions();
				int alternatorCount = alternators.size();

				if (alternatorCount > 0) {
					this.progress(alternatorCount);
					
					for (BlockPos pos : alternators) {
						if (this.getWorld().isAirBlock(pos.up())) {
							((WorldServer) this.getWorld()).spawnParticle(EnumParticleTypes.PORTAL, false, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 1, 0, 0, 0, 0.1D);
						}
					}
					
					if (this.progress >= this.progressReq) {
						for (int i = 0; i < this.matrix.getSlots(); i++) {
							this.matrix.extractItem(i, 1, false);
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
		tag.merge(this.matrix.serializeNBT());
		
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
		this.matrix.deserializeNBT(tag);
		this.result = new ItemStack(tag.getCompoundTag("Result"));
		
		this.progress = tag.getInteger("Progress");
		this.progressReq = tag.getInteger("ProgressReq");
	}
	
	@Override
	public ItemStack getResult() {
		return this.result;
	}
	
	@Override
	public ItemStackHandler getMatrix() {
		return this.matrix;
	}

	@Override
	public void setResult(ItemStack result) {
		this.result = result;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.matrix.setStackInSlot(slot, stack);
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
	
	private void progress(int alternators) {
		this.progress++;
		
		int timeReq = 20 * ModConfig.confEnderTimeRequired;
		this.progressReq = (int) Math.max(timeReq - (timeReq * (ModConfig.confEnderAlternatorEff * alternators)), 20);
	}
	
	public int getProgressRequired() {
		return this.progressReq;
	}
	
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq(this.getPos().add(0.5, 0.5, 0.5)) <= 64;
	}
}
