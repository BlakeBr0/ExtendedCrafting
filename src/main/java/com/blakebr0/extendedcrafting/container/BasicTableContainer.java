package com.blakebr0.extendedcrafting.container;

import com.blakebr0.cucumber.container.BaseContainerMenu;
import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.inventory.ExtendedCraftingInventory;
import com.blakebr0.extendedcrafting.container.slot.TableOutputSlot;
import com.blakebr0.extendedcrafting.init.ModMenuTypes;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.tileentity.BasicTableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class BasicTableContainer extends BaseContainerMenu {
	private final Level level;
	private final Container result;
	private final ExtendedCraftingInventory matrix;
	private boolean isVanillaRecipe = false;

	public BasicTableContainer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
		this(id, playerInventory, BasicTableTileEntity.createInventoryHandler(), buffer.readBlockPos());
	}

	public BasicTableContainer(int id, Inventory playerInventory, CItemStacksHandler inventory, BlockPos pos) {
		super(ModMenuTypes.BASIC_TABLE.get(), id, pos);
		this.level = playerInventory.player.level();
		this.result = new ResultContainer();
		this.matrix = new ExtendedCraftingInventory(this, inventory, 3);

		this.addSlot(new TableOutputSlot(this, this.matrix, this.result, 0, 124, 36));
		
		int i, j;
		for (i = 0; i < 3; i++) {
			for (j = 0; j < 3; j++) {
				this.addSlot(new Slot(this.matrix, j + i * 3, 32 + j * 18, 18 + i * 18));
			}
		}

		for (i = 0; i < 3; i++) {
			for (j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 88 + i * 18));
			}
		}

		for (j = 0; j < 9; j++) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 146));
		}

		this.slotsChanged(matrix);
	}

	@Override
	public void slotsChanged(Container matrix) {
        if (this.level.isClientSide()) {
            return;
        }

		var recipeAccess = ((ServerLevel) this.level).recipeAccess();
		var inventory = this.matrix.asCraftInput();
		var recipe = recipeAccess.getRecipeFor(ModRecipeTypes.TABLE.get(), inventory, this.level);

		this.isVanillaRecipe = false;

		if (recipe.isPresent()) {
			var result = recipe.get().value().assemble(inventory);

			this.result.setItem(0, result);
		} else if (ModConfigs.TABLE_USE_VANILLA_RECIPES.get()) {
			var vanilla = recipeAccess.getRecipeFor(RecipeType.CRAFTING, inventory, this.level);

			if (vanilla.isPresent()) {
				var result = vanilla.get().value().assemble(inventory);

				this.isVanillaRecipe = true;
				this.result.setItem(0, result);
			} else {
				this.result.setItem(0, ItemStack.EMPTY);
			}
		} else {
			this.result.setItem(0, ItemStack.EMPTY);
		}

		super.slotsChanged(matrix);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotNumber) {
		var itemstack = ItemStack.EMPTY;
		var slot = this.slots.get(slotNumber);

		if (slot.hasItem()) {
			var itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotNumber == 0) {
				if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotNumber >= 10 && slotNumber < 46) {
				if (!this.moveItemStackTo(itemstack1, 1, 10, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
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

	public boolean isVanillaRecipe() {
		return this.isVanillaRecipe;
	}
}