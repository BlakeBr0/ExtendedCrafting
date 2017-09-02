package com.blakebr0.extendedcrafting.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockLamp extends ItemBlock {

	public ItemBlockLamp(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "_" + BlockLamp.Type.byMetadata(stack.getMetadata()).getName();
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}
