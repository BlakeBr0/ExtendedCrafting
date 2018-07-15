package com.blakebr0.extendedcrafting.block;

import javax.annotation.Nonnull;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.extendedcrafting.ExtendedCrafting;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFrame extends BlockBase {

	public BlockFrame() {
		super("ec.frame", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
		this.setCreativeTab(ExtendedCrafting.CREATIVE_TAB);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Nonnull
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}
