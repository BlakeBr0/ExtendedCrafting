package com.blakebr0.extendedcrafting.tile;

import com.blakebr0.cucumber.tile.TileEntityBase;
import com.blakebr0.cucumber.util.VanillaPacketDispatcher;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.basic.BasicStackHandler;
import com.blakebr0.extendedcrafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;

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
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

public class TileEnderCrafter extends TileEntityBase implements IExtendedTable, ITickable {

	public ItemStackHandler matrix = new ItemStackHandler(9);
	public InventoryCraftResult result = new InventoryCraftResult();
	private int progress;

	@Override
	public ItemStack getResult() {
		return result.getStackInSlot(0);
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
	
	@Override
	public void update() {
		if (!getWorld().isRemote) {
			ItemStack result = EnderCrafterRecipeManager.getInstance().findMatchingRecipe(matrix);
			if (!result.isEmpty()) {
				this.progress++;
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

	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
	}
	
	public int getProgress() {
		return this.progress;
	}

	@Override
	public ItemStackHandler getMatrix() {
		return this.matrix;
	}
}
