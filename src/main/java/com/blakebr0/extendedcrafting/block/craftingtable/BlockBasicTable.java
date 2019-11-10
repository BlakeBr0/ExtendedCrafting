package com.blakebr0.extendedcrafting.block.craftingtable;

import java.util.List;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.GuiHandler;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.tileentity.TileBasicCraftingTable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBasicTable extends BlockBase implements ITileEntityProvider, IEnableable {

	public BlockBasicTable() {
		super("ec.table_basic", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
		this.setCreativeTab(ExtendedCrafting.ITEM_GROUP);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileBasicCraftingTable();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else {
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileBasicCraftingTable) {
				player.openGui(ExtendedCrafting.instance, GuiHandler.BASIC_TABLE, world, pos.getX(), pos.getY(), pos.getZ());
			}
			
			return true;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileBasicCraftingTable tile = (TileBasicCraftingTable) world.getTileEntity(pos);
		if (tile != null) {
			NonNullList<ItemStack> matrix = tile.getMatrix();
			for (int i = 0; i < matrix.size(); i++) {
				ItemStack stack = matrix.get(i);
				this.spawnAsEntity(world, pos, stack);
			}
		}
		
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return facing == EnumFacing.UP ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(Utils.localize("tooltip.ec.tier", 1));
	}

	@Override
	public boolean isEnabled() {
		return ModConfig.confTableEnabled;
	}
}
