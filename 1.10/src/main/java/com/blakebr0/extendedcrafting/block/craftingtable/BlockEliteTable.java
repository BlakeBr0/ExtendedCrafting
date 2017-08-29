package com.blakebr0.extendedcrafting.block.craftingtable;

import com.blakebr0.extendedcrafting.block.BlockBase;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEliteTable extends BlockBase implements ITileEntityProvider {

	public BlockEliteTable(){
		super("table_elite", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta){
		return null;
	}

}
