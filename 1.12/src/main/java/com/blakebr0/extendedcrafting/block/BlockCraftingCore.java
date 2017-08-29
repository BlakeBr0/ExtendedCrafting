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
import net.minecraft.util.text.TextComponentTranslation;
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
					if (StackHelper.isNull(tile.getInventory().getStackInSlot(0))) {
						if (!StackHelper.isNull(held)) {
							tile.getInventory().setStackInSlot(0, StackHelper.withSize(held.copy(), 1, false));
							player.setHeldItem(hand, StackHelper.decrease(held, 1, false));
							world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					} else {
						EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, tile.getInventory().getStackInSlot(0));
						item.setNoPickupDelay();
						world.spawnEntity(item);
						tile.getInventory().setStackInSlot(0, StackHelper.getNull());
					}
				} else {
					player.openGui(ExtendedCrafting.instance, GuiHandler.CRAFTING_CORE, world, pos.getX(), pos.getY(), pos.getZ());
				}
				// TODO: DEBUG, REMOVE OR DIE TY
			/*	if (player.isSneaking()) {
					tile.getEnergy().receiveEnergy(100000, false);
					player.sendMessage(new TextComponentTranslation("Cheaty cheaty Darkosto, back at it again... I'll let it slide though since it's your birthday."));
				} */
			} 
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileCraftingCore tile = (TileCraftingCore) world.getTileEntity(pos);
		if (tile instanceof TileCraftingCore) {
			ItemStack stack = tile.getInventory().getStackInSlot(0);
			if (!StackHelper.isNull(stack)) {
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.spawnEntity(item);
			}
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
}
