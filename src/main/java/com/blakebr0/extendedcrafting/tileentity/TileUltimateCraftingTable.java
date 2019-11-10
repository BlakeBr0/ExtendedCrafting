package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.extendedcrafting.lib.IExtendedTable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class TileUltimateCraftingTable extends TileEntity implements IInventory, IExtendedTable {

	private NonNullList<ItemStack> matrix = NonNullList.withSize(81, ItemStack.EMPTY);
	private ItemStack result = ItemStack.EMPTY;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		
		if (!this.result.isEmpty()) {
			tag.setTag("Result", this.result.serializeNBT());
		} else {
			tag.removeTag("Result");
		}
		
		tag.merge(ItemStackHelper.saveAllItems(tag, this.matrix));
		
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		this.result = new ItemStack(tag.getCompoundTag("Result"));
		ItemStackHelper.loadAllItems(tag, this.matrix);
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
	public ItemStack getResult() {
		return this.result;
	}

	@Override
	public void setResult(ItemStack result) {
		this.result = result;
		this.markDirty();
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.matrix.set(slot, stack);
	}

	@Override
	public NonNullList<ItemStack> getMatrix() {
		return this.matrix;
	}

	@Override
	public int getLineSize() {
		return 9;
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
		return 81;
	}

	@Override
	public boolean isEmpty() {
		return !this.matrix.stream().anyMatch(s -> !s.isEmpty()) && this.result.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.matrix.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int amount) {
		return ItemStackHelper.getAndSplit(this.matrix, index, amount);
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
		this.matrix = NonNullList.withSize(81, ItemStack.EMPTY);
		this.setResult(ItemStack.EMPTY);
	}
}
