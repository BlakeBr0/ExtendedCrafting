package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.gui.GuiHandler;
import com.blakebr0.extendedcrafting.tile.TileCraftingCore;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCraftingCore extends BlockBase implements ITileEntityProvider {

	public BlockCraftingCore() {
		super("ec.crafting_core", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack held = player.getHeldItem(hand);
		if (!world.isRemote) {
			TileCraftingCore tile = (TileCraftingCore) world.getTileEntity(pos);
			if (tile != null) {
				if (facing == EnumFacing.UP) {
					ItemStack stack = tile.getInventory().getStackInSlot(0);
					if (stack.isEmpty()) {
						if (!held.isEmpty()) {
							tile.getInventory().setStackInSlot(0, StackHelper.withSize(held.copy(), 1, false));
							player.setHeldItem(hand, StackHelper.decrease(held, 1, false));
							world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					} else {
						EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, stack);
						item.setNoPickupDelay();
						world.spawnEntity(item);
						tile.getInventory().setStackInSlot(0, ItemStack.EMPTY);
					}
				} else {
					player.openGui(ExtendedCrafting.instance, GuiHandler.CRAFTING_CORE, world, pos.getX(), pos.getY(), pos.getZ());
				}

				if (ExtendedCrafting.DEBUG && player.isSneaking()) {
					tile.getEnergy().receiveEnergy(10000000, false);
				} 
			} 
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileCraftingCore tile = (TileCraftingCore) world.getTileEntity(pos);
		if (tile != null) {
			ItemStack stack = tile.getInventory().getStackInSlot(0);
			this.spawnAsEntity(world, pos, stack);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileCraftingCore();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
}
