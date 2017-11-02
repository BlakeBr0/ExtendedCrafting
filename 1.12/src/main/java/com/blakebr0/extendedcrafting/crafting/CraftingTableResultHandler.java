package com.blakebr0.extendedcrafting.crafting;

import javax.annotation.Nullable;

import com.blakebr0.cucumber.helper.StackHelper;
import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CraftingTableResultHandler extends Slot {

	private final CraftingTableCrafting crafting;
	private final EntityPlayer player;
	private final ItemStackHandler matrix;
	private int amountCrafted;

	public CraftingTableResultHandler(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventory, int slot, int x, int y) {
		super(inventory, slot, x, y);
		this.crafting = (CraftingTableCrafting) craftingInventory;
		this.player = player;
		this.matrix = ((CraftingTableCrafting) craftingInventory).tile.matrix;
	}

	@Override
	public boolean isItemValid(@Nullable ItemStack stack) {
		return false;
	}

	@Override
	protected void onCrafting(ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCrafting(this.player.world, this.player, this.amountCrafted);
            FMLCommonHandler.instance().firePlayerCraftingEvent(this.player, stack, this.crafting);
        }

        this.amountCrafted = 0;
        InventoryCraftResult inventorycraftresult = (InventoryCraftResult)this.inventory;
        IRecipe irecipe = inventorycraftresult.getRecipeUsed();

        if (irecipe != null && !irecipe.isHidden()) {
            this.player.unlockRecipes(Lists.newArrayList(irecipe));
            inventorycraftresult.setRecipeUsed((IRecipe)null);
        }
	}
	
	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        this.onCrafting(stack);
        ForgeHooks.setCraftingPlayer(player);
        NonNullList<ItemStack> nonnulllist = CraftingManager.getRemainingItems(this.crafting, player.getEntityWorld());
        ForgeHooks.setCraftingPlayer(null);
		for (int i = 0; i < this.matrix.getSlots(); i++) {
			ItemStack slotStack = this.matrix.getStackInSlot(i);
			if (!StackHelper.isNull(slotStack)) {
				if (slotStack.getItem().hasContainerItem(slotStack) && slotStack.getCount() == 1) {
					this.matrix.setStackInSlot(i, slotStack.getItem().getContainerItem(slotStack));
				} else {
					this.matrix.setStackInSlot(i, StackHelper.decrease(slotStack.copy(), 1, false));
				}
			}
		}
		this.crafting.container.onCraftMatrixChanged(this.crafting);
		return stack;
	}
}
