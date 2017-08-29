package com.blakebr0.extendedcrafting.block;

import com.blakebr0.extendedcrafting.ExtendedCrafting;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBase extends Block {

	public BlockBase(String name, Material material, SoundType sound, float hardness, float resistance){
		super(material);
		this.setUnlocalizedName("ec." + name);
		this.setRegistryName(name);
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
		this.setSoundType(sound);
		this.setHardness(hardness);
		this.setResistance(resistance);
	}

	public void init(){

	}
}
