package com.blakebr0.extendedcrafting.block;

import java.util.List;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.gui.GuiHandler;
import com.blakebr0.extendedcrafting.tile.TileCraftingTable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCraftingTable extends BlockBase implements ITileEntityProvider {

	public BlockCraftingTable() {
		super("ec.crafting_table", Material.WOOD, SoundType.WOOD, 2.5F, 12.5F);
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileCraftingTable();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.openGui(ExtendedCrafting.instance, GuiHandler.CRAFTING_TABLE, world, pos.getX(), pos.getY(),
					pos.getZ());
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileCraftingTable tile = (TileCraftingTable) world.getTileEntity(pos);
		if (tile instanceof TileCraftingTable) {
			for (int i = 0; i < tile.matrix.getSlots(); i++) {
				ItemStack stack = tile.matrix.getStackInSlot(i);
				if (!StackHelper.isNull(stack)) {
					world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
				}
			}
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(Utils.localize("tooltip.ec.crafting_table"));
	}
}
