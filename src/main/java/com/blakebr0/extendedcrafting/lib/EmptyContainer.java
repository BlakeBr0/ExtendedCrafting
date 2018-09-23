package com.blakebr0.extendedcrafting.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EmptyContainer extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
