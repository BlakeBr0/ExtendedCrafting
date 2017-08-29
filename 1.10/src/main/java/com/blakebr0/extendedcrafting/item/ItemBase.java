package com.blakebr0.extendedcrafting.item;

import com.blakebr0.extendedcrafting.ExtendedCrafting;

import net.minecraft.item.Item;

public class ItemBase extends Item {
	
	public ItemBase(String name){
		super();
		this.setUnlocalizedName("ec." + name);
		this.setRegistryName(name);
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
	}
}