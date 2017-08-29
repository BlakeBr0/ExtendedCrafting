package com.blakebr0.extendedcrafting.block;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.tile.TileCraftingCore;
import com.blakebr0.extendedcrafting.util.StackHelper;

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

	public BlockCraftingCore(){
		super("crafting_core", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote){
			TileCraftingCore tile = (TileCraftingCore)world.getTileEntity(pos);
			if(tile != null){
				if(side == EnumFacing.UP){
					if(StackHelper.isNull(tile.getInventory().getStackInSlot(0))){
						if(!StackHelper.isNull(held)){
							tile.getInventory().setStackInSlot(0, StackHelper.withSize(held.copy(), 1, false));
							player.setHeldItem(hand, StackHelper.decrease(held, 1, false));
							world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					} else {
						EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, tile.getInventory().getStackInSlot(0));
						item.setNoPickupDelay();
						world.spawnEntityInWorld(item);
						tile.getInventory().setStackInSlot(0, StackHelper.getNull());
					}
				} else {
					player.openGui(ExtendedCrafting.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
				}
				// TODO: DEBUG, REMOVE OR DIE TY
				if(player.isSneaking()){
					tile.getEnergy().receiveEnergy(100000, false);
				}
			}
		}
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta){
		return new TileCraftingCore();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
}
