package com.blakebr0.extendedcrafting.block.craftingtable;

import java.util.List;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.gui.GuiHandler;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.tile.TileEliteCraftingTable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEliteTable extends BlockBase implements ITileEntityProvider, IEnableable {

	public BlockEliteTable() {
		super("ec.table_elite", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
		this.setCreativeTab(ExtendedCrafting.CREATIVE_TAB);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEliteCraftingTable();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else {
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileEliteCraftingTable) {
				player.openGui(ExtendedCrafting.instance, GuiHandler.ELITE_TABLE, world, pos.getX(), pos.getY(), pos.getZ());
			}
			
			return true;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEliteCraftingTable tile = (TileEliteCraftingTable) world.getTileEntity(pos);
		if (tile != null) {
			for (int i = 0; i < tile.matrix.getSlots(); i++) {
				ItemStack stack = tile.matrix.getStackInSlot(i);
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
		tooltip.add(Utils.localize("tooltip.ec.tier", 3));
	}

	@Override
	public boolean isEnabled() {
		return ModConfig.confTableEnabled;
	}
}
