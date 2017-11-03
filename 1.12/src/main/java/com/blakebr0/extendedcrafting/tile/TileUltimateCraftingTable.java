package com.blakebr0.extendedcrafting.tile;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.ultimate.UltimateStackHandler;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TileUltimateCraftingTable extends TileEntity implements IExtendedTable {

	public UltimateStackHandler matrix = new UltimateStackHandler(81, this);
	private ItemStack result = ItemStack.EMPTY;

	@Override
	public ItemStack getResult() {
		if (result.isEmpty()) {
			result = TableRecipeManager.getInstance().findMatchingRecipe(matrix);
		}
		return result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		matrix.setStackInSlot(slot, stack);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.merge(matrix.serializeNBT());
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		matrix.deserializeNBT(tag);
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), -1, this.getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
	}

	@Override
	public ItemStackHandler getMatrix() {
		return matrix;
	}
}
