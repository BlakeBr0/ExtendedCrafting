package com.blakebr0.extendedcrafting.container;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.slot.OutputSlot;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.container.inventory.ExtendedCraftingInventory;
import com.blakebr0.extendedcrafting.container.slot.TableOutputSlot;
import com.blakebr0.extendedcrafting.init.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;
import java.util.function.Function;

public class EliteAutoTableContainer extends Container {
	private final Function<PlayerEntity, Boolean> isUsableByPlayer;
	private final IIntArray data;
	private final BlockPos pos;
	private final World world;
	private final IItemHandlerModifiable result;

	private EliteAutoTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		this(type, id, playerInventory, p -> false, new BaseItemStackHandler(50), new IntArray(6), buffer.readBlockPos());
	}

	private EliteAutoTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, BaseItemStackHandler inventory, IIntArray data, BlockPos pos) {
		super(type, id);
		this.isUsableByPlayer = isUsableByPlayer;
		this.data = data;
		this.pos = pos;
		this.world = playerInventory.player.world;
		this.result = new ItemStackHandler();

		IInventory matrix = new ExtendedCraftingInventory(this, inventory, 7, true);

		this.addSlot(new TableOutputSlot(this, matrix, this.result, 0, 191, 71));
		
		int i, j;
		for (i = 0; i < 7; i++) {
			for (j = 0; j < 7; j++) {
				this.addSlot(new Slot(matrix, j + i * 7, 27 + j * 18, 18 + i * 18));
			}
		}

		this.addSlot(new OutputSlot(inventory, 49, 191, 115));

		for (i = 0; i < 3; i++) {
			for (j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 30 + j * 18, 160 + i * 18));
			}
		}

		for (j = 0; j < 9; j++) {
			this.addSlot(new Slot(playerInventory, j, 30 + j * 18, 218));
		}

		this.onCraftMatrixChanged(matrix);
		this.trackIntArray(data);
	}

	@Override
	public void onCraftMatrixChanged(IInventory matrix) {
		Optional<ITableRecipe> recipe = this.world.getRecipeManager().getRecipe(RecipeTypes.TABLE, matrix, this.world);
		if (recipe.isPresent()) {
			ItemStack result = recipe.get().getCraftingResult(matrix);
			this.result.setStackInSlot(0, result);
		} else {
			this.result.setStackInSlot(0, ItemStack.EMPTY);
		}

		super.onCraftMatrixChanged(matrix);
	}

	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return this.isUsableByPlayer.apply(player);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotNumber) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotNumber == 0 || slotNumber == 50) {
				if (!this.mergeItemStack(itemstack1, 51, 87, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (slotNumber >= 51 && slotNumber < 87) {
				if (!this.mergeItemStack(itemstack1, 1, 50, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 51, 87, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	public static EliteAutoTableContainer create(int windowId, PlayerInventory playerInventory, PacketBuffer buffer) {
		return new EliteAutoTableContainer(ModContainerTypes.ELITE_AUTO_TABLE.get(), windowId, playerInventory, buffer);
	}

	public static EliteAutoTableContainer create(int windowId, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, BaseItemStackHandler inventory, IIntArray data, BlockPos pos) {
		return new EliteAutoTableContainer(ModContainerTypes.ELITE_AUTO_TABLE.get(), windowId, playerInventory, isUsableByPlayer, inventory, data, pos);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public int getEnergyBarScaled(int pixels) {
		int i = this.getEnergyStored();
		int j = this.getMaxEnergyStored();
		return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
	}

	public int getProgressBarScaled(int pixels) {
		int i = this.getProgress();
		int j = this.getProgressRequired();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	public boolean isRunning() {
		return this.data.get(4) > 0;
	}

	public int getEnergyStored() {
		return this.data.get(0);
	}

	public int getMaxEnergyStored() {
		return this.data.get(1);
	}

	public int getProgress() {
		return this.data.get(2);
	}

	public int getProgressRequired() {
		return this.data.get(3);
	}

	public int getSelected() {
		return this.data.get(5);
	}
}