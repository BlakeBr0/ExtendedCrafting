package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.GuiHandler;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.tileentity.TileEnderCrafter;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEnderCrafter extends BlockBase implements ITileEntityProvider, IEnableable {

	public BlockEnderCrafter() {
		super("ec.ender_crafter", Material.IRON, SoundType.METAL, 6.0F, 12.0F);
		this.setCreativeTab(ExtendedCrafting.ITEM_GROUP);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.openGui(ExtendedCrafting.instance, GuiHandler.ENDER_CRAFTER, world, pos.getX(), pos.getY(), pos.getZ());
		}
		
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEnderCrafter();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEnderCrafter tile = (TileEnderCrafter) world.getTileEntity(pos);
		if (tile != null) {
			NonNullList<ItemStack> matrix = tile.getMatrix();
			for (int i = 0; i < matrix.size(); i++) {
				ItemStack stack = matrix.get(i);
				this.spawnAsEntity(world, pos, stack);
			}
			this.spawnAsEntity(world, pos, tile.getResult());
		}
		
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean isEnabled() {
		return ModConfig.confEnderEnabled;
	}
}
