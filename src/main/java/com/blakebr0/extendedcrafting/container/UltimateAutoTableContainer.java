package com.blakebr0.extendedcrafting.container;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.slot.OutputSlot;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.container.inventory.ExtendedCraftingInventory;
import com.blakebr0.extendedcrafting.container.slot.TableOutputSlot;
import com.blakebr0.extendedcrafting.init.ModContainerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Function;

public class UltimateAutoTableContainer extends AbstractContainerMenu {
	private final Function<Player, Boolean> isUsableByPlayer;
	private final ContainerData data;
	private final BlockPos pos;
	private final Level world;
	private final Container result;

	private UltimateAutoTableContainer(MenuType<?> type, int id, Inventory playerInventory, FriendlyByteBuf buffer) {
		this(type, id, playerInventory, p -> false, new BaseItemStackHandler(82), new SimpleContainerData(6), buffer.readBlockPos());
	}

	private UltimateAutoTableContainer(MenuType<?> type, int id, Inventory playerInventory, Function<Player, Boolean> isUsableByPlayer, BaseItemStackHandler inventory, ContainerData data, BlockPos pos) {
		super(type, id);
		this.isUsableByPlayer = isUsableByPlayer;
		this.data = data;
		this.pos = pos;
		this.world = playerInventory.player.level;
		this.result = new SimpleContainer(1);

		Container matrix = new ExtendedCraftingInventory(this, inventory, 9, true);

		this.addSlot(new TableOutputSlot(this, matrix, this.result, 0, 225, 89));
		
		int i, j;
		for (i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				this.addSlot(new Slot(matrix, j + i * 9, 27 + j * 18, 18 + i * 18));
			}
		}

		this.addSlot(new OutputSlot(inventory, 81, 225, 133));

		for (i = 0; i < 3; i++) {
			for (j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 47 + j * 18, 196 + i * 18));
			}
		}

		for (j = 0; j < 9; j++) {
			this.addSlot(new Slot(playerInventory, j, 47 + j * 18, 254));
		}

		this.slotsChanged(matrix);
		this.addDataSlots(data);
	}

	@Override
	public void slotsChanged(Container matrix) {
		Optional<ITableRecipe> recipe = this.world.getRecipeManager().getRecipeFor(RecipeTypes.TABLE, matrix, this.world);
		if (recipe.isPresent()) {
			ItemStack result = recipe.get().assemble(matrix);
			this.result.setItem(0, result);
		} else {
			this.result.setItem(0, ItemStack.EMPTY);
		}

		super.slotsChanged(matrix);
	}

	@Override
	public boolean stillValid(Player player) {
		return this.isUsableByPlayer.apply(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotNumber) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotNumber == 0 || slotNumber == 82) {
				if (!this.moveItemStackTo(itemstack1, 83, 119, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotNumber >= 83 && slotNumber < 119) {
				if (!this.moveItemStackTo(itemstack1, 1, 82, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 83, 119, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public static UltimateAutoTableContainer create(int windowId, Inventory playerInventory, FriendlyByteBuf buffer) {
		return new UltimateAutoTableContainer(ModContainerTypes.ULTIMATE_AUTO_TABLE.get(), windowId, playerInventory, buffer);
	}

	public static UltimateAutoTableContainer create(int windowId, Inventory playerInventory, Function<Player, Boolean> isUsableByPlayer, BaseItemStackHandler inventory, ContainerData data, BlockPos pos) {
		return new UltimateAutoTableContainer(ModContainerTypes.ULTIMATE_AUTO_TABLE.get(), windowId, playerInventory, isUsableByPlayer, inventory, data, pos);
	}
}