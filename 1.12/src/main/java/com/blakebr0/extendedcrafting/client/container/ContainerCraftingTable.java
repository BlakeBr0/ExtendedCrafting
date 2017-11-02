package com.blakebr0.extendedcrafting.client.container;

import com.blakebr0.extendedcrafting.crafting.CraftingTableCraftResult;
import com.blakebr0.extendedcrafting.crafting.CraftingTableCrafting;
import com.blakebr0.extendedcrafting.crafting.CraftingTableResultHandler;
import com.blakebr0.extendedcrafting.crafting.CraftingTableStackHandler;
import com.blakebr0.extendedcrafting.tile.TileCraftingTable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCraftingTable extends Container {

	public InventoryCrafting matrix;
	public InventoryCraftResult result;
	public TileCraftingTable tile;
	private IItemHandler handler;
	private final BlockPos pos;
	private final World world;
	private final InventoryPlayer player;

	public ContainerCraftingTable(InventoryPlayer player, TileCraftingTable tile, World world, BlockPos pos) {
		this.tile = tile;
		this.handler = tile.matrix;
		this.matrix = new CraftingTableCrafting(this, tile);
		this.result = new CraftingTableCraftResult(tile);
		this.pos = pos;
		this.world = world;
		this.player = player;

		this.addSlotToContainer(new CraftingTableResultHandler(player.player, this.matrix, this.result, 0, 124, 35));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlotToContainer(new SlotItemHandler(this.handler, j + i * 3, 30 + j * 18, 17 + i * 18));
			}
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlotToContainer(new Slot(player, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(player, l, 8 + l * 18, 142));
		}

		this.onCraftMatrixChanged(this.matrix);
		((CraftingTableStackHandler) handler).crafting = matrix;
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		ItemStack recipe = CraftingManager.findMatchingResult(this.matrix, this.world);
		this.result.setInventorySlotContents(0, recipe);
		this.slotChangedCraftingGrid(this.world, this.player.player, this.matrix, this.result);
		this.tile.markDirty();
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0) {
				itemstack1.getItem().onCreated(itemstack1, this.world, player);

				if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37) {
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 37 && index < 46) {
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(player, itemstack1);
			this.onCraftMatrixChanged(matrix);

			if (index == 0) {
				player.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slot) {
		return slot.inventory != this.result && super.canMergeSlot(stack, slot);
	}

	@Override
	protected void slotChangedCraftingGrid(World world, EntityPlayer player, InventoryCrafting inventory, InventoryCraftResult result) {
		if (!world.isRemote) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
			ItemStack itemstack = ItemStack.EMPTY;
			IRecipe irecipe = CraftingManager.findMatchingRecipe(inventory, world);

			if (irecipe != null && (irecipe.isHidden() || !world.getGameRules().getBoolean("doLimitedCrafting")
					|| entityplayermp.getRecipeBook().containsRecipe(irecipe))) {
				result.setRecipeUsed(irecipe);
				itemstack = irecipe.getCraftingResult(inventory);
			}

			entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
		}
	}
}