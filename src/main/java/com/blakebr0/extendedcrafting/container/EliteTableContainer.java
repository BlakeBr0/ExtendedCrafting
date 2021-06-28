package com.blakebr0.extendedcrafting.container;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.container.inventory.ExtendedCraftingInventory;
import com.blakebr0.extendedcrafting.container.slot.TableOutputSlot;
import com.blakebr0.extendedcrafting.init.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;
import java.util.function.Function;

public class EliteTableContainer extends Container {
	private final Function<PlayerEntity, Boolean> isUsableByPlayer;
	private final World world;
	private final IInventory result;

	private EliteTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory) {
		this(type, id, playerInventory, p -> false, new BaseItemStackHandler(49));
	}

	private EliteTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, BaseItemStackHandler inventory) {
		super(type, id);
		this.isUsableByPlayer = isUsableByPlayer;
		this.world = playerInventory.player.level;
		this.result = new Inventory(1);

		IInventory matrix = new ExtendedCraftingInventory(this, inventory, 7);

		this.addSlot(new TableOutputSlot(this, matrix, this.result, 0, 172, 71));
		
		int i, j;
		for (i = 0; i < 7; i++) {
			for (j = 0; j < 7; j++) {
				this.addSlot(new Slot(matrix, j + i * 7, 8 + j * 18, 18 + i * 18));
			}
		}

		for (i = 0; i < 3; i++) {
			for (j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 20 + j * 18, 160 + i * 18));
			}
		}

		for (j = 0; j < 9; j++) {
			this.addSlot(new Slot(playerInventory, j, 20 + j * 18, 218));
		}

		this.slotsChanged(matrix);
	}

	@Override
	public void slotsChanged(IInventory matrix) {
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
	public boolean stillValid(PlayerEntity player) {
		return this.isUsableByPlayer.apply(player);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int slotNumber) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotNumber == 0) {
				if (!this.moveItemStackTo(itemstack1, 50, 86, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotNumber >= 50 && slotNumber < 86) {
				if (!this.moveItemStackTo(itemstack1, 1, 50, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 50, 86, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
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

	public static EliteTableContainer create(int windowId, PlayerInventory playerInventory) {
		return new EliteTableContainer(ModContainerTypes.ELITE_TABLE.get(), windowId, playerInventory);
	}

	public static EliteTableContainer create(int windowId, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, BaseItemStackHandler inventory) {
		return new EliteTableContainer(ModContainerTypes.ELITE_TABLE.get(), windowId, playerInventory, isUsableByPlayer, inventory);
	}
}