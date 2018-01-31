package com.blakebr0.extendedcrafting.tile;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.cucumber.tile.TileEntityBase;
import com.blakebr0.cucumber.util.VanillaPacketDispatcher;
import com.blakebr0.extendedcrafting.block.BlockEnderAlternator;
import com.blakebr0.extendedcrafting.block.BlockPedestal;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.basic.BasicStackHandler;
import com.blakebr0.extendedcrafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.ItemStackHandler;

public class TileEnderCrafter extends TileEntityBase implements IExtendedTable, ITickable {

	public ItemStackHandler matrix = new ItemStackHandler(9);
	public InventoryCraftResult result = new InventoryCraftResult();
	private int progress;
	
	@Override
	public void update() {
		if (!this.getWorld().isRemote) {
			ItemStack result = EnderCrafterRecipeManager.getInstance().findMatchingRecipe(matrix);
			if (!result.isEmpty()) {
				List<BlockPos> alternators = this.getAlternatorPositions();

				this.progress(alternators.size());
				
				for (BlockPos pos : alternators) {
					if (this.getWorld().isAirBlock(pos.up())) {
						((WorldServer) this.getWorld()).spawnParticle(EnumParticleTypes.PORTAL, false, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 1, 0, 0, 0, 0.1D);
					}
				}
				
				if (this.progress >= 100) {
					for (int i = 0; i < matrix.getSlots(); i++) {
						this.matrix.extractItem(i, 1, false);
					}
					this.updateResult(result);
					this.progress = 0;
				}
				
				markDirty();
			} else {
				if (this.progress > 0) {
					this.progress = 0;
					markDirty();
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.merge(matrix.serializeNBT());
		if (!result.getStackInSlot(0).isEmpty()) {
			tag.setTag("Result", result.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
		} else {
			tag.removeTag("Result");
		}
		tag.setInteger("Progress", progress);
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		matrix.deserializeNBT(tag);
		if (tag.hasKey("Result")) {
			result.setInventorySlotContents(0, new ItemStack(tag.getCompoundTag("Result")));
		}
		progress = tag.getInteger("Progress");
	}
	
	@Override
	public ItemStack getResult() {
		return result.getStackInSlot(0);
	}
	
	@Override
	public ItemStackHandler getMatrix() {
		return this.matrix;
	}

	public void setResult(ItemStack result) {
		this.result.setInventorySlotContents(0, result);
	}
	
	public void updateResult(ItemStack result) {
		if (this.result.getStackInSlot(0).isEmpty()) {
			this.setResult(result);
		} else {
			this.result.getStackInSlot(0).grow(result.getCount());
		}
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		matrix.setStackInSlot(slot, stack);
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
	}
	
	public int getProgress() {
		return this.progress;
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
	
	private void progress(int alternators) {
		this.progress++;
	}
}
