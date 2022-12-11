package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.item.BaseItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class UltimateSingularityItem extends BaseItem {
	public UltimateSingularityItem() {
		super(p -> p.stacksTo(16).rarity(Rarity.EPIC));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
}
