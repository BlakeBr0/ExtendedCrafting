package com.blakebr0.extendedcrafting.block.craftingtable;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.gui.GuiHandler;
import com.blakebr0.extendedcrafting.tile.TileBasicCraftingTable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBasicTable extends BlockBase implements ITileEntityProvider {

	public BlockBasicTable() {
		super("ec.table_basic", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileBasicCraftingTable();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else {
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileBasicCraftingTable) {
				player.openGui(ExtendedCrafting.instance, GuiHandler.BASIC_TABLE, world, pos.getX(), pos.getY(),
						pos.getZ());
			}
			return true;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileBasicCraftingTable tile = (TileBasicCraftingTable) world.getTileEntity(pos);
		if (tile instanceof TileBasicCraftingTable) {
			for (int i = 0; i < tile.matrix.getSlots(); i++) {
				ItemStack stack = tile.matrix.getStackInSlot(i);
				if (!StackHelper.isNull(stack)) {
					world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
				}
			}
		}
		super.breakBlock(world, pos, state);
	}
}
