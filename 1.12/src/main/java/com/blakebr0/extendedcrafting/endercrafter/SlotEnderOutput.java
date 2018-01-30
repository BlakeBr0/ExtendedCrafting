package com.blakebr0.extendedcrafting.endercrafter;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.client.container.ContainerEnderCrafter;
import com.blakebr0.extendedcrafting.tile.TileEnderCrafter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotEnderOutput extends Slot {
	
	private ItemStackHandler matrix;
	private ContainerEnderCrafter container;

	public SlotEnderOutput(TileEnderCrafter tile, ContainerEnderCrafter container, int index, int xPosition, int yPosition) {
		super(container.result, index, xPosition, yPosition);
	}
}
