package com.blakebr0.extendedcrafting.container.automationinterface;

import com.blakebr0.extendedcrafting.tileentity.TileAutomationInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAutomationInterface extends Container {

	public TileAutomationInterface tile;
	public InventoryPlayer player;

	public ContainerAutomationInterface(InventoryPlayer player, TileAutomationInterface tile) {
		this.tile = tile;
		this.player = player;
		
		this.addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 34, 50));
		this.addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 144, 50) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		});

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 111 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 169));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNumber == 1) {
                if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                
                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotNumber != 0) {
            	ItemStack inputStack = this.inventorySlots.get(0).getStack();
                if (inputStack.isEmpty() || (inputStack.isItemEqual(itemstack1) && inputStack.getCount() < inputStack.getMaxStackSize())) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(slotNumber >= 2 && slotNumber < 29) {
                    if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotNumber >= 29 && slotNumber < 38) {
                	if (!this.mergeItemStack(itemstack1, 2, 29, false)) {
                        return ItemStack.EMPTY;
                	}
                }
            } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
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

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tile.isUseableByPlayer(player);
	}
}
