package com.blakebr0.extendedcrafting.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTrimmed extends ItemBlock {

	public ItemBlockTrimmed(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "_" + BlockTrimmed.Type.byMetadata(stack.getMetadata()).getName();
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getMetadata() == 5;
	}
}
