package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CraftingCoreBlock extends BaseTileEntityBlock implements ITickableTileEntity, IEnableable {
	public CraftingCoreBlock() {
		super(Material.IRON, SoundType.METAL, 5.0F, 10.0F);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CraftingCoreTileEntity();
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack held = player.getHeldItem(hand);
		if (!world.isRemote()) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof CraftingCoreTileEntity) {
				CraftingCoreTileEntity core = (CraftingCoreTileEntity) tile;
				if (hit.getFace() == Direction.UP) {
					ItemStack stack = tile.getInventory().getStackInSlot(0);
					if (stack.isEmpty()) {
						if (!held.isEmpty()) {
							tile.getInventory().setStackInSlot(0, StackHelper.withSize(held.copy(), 1, false));
							player.setHeldItem(hand, StackHelper.decrease(held, 1, false));
							world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					} else {
						ItemEntity item = new ItemEntity(world, player.posX, player.posY, player.posZ, stack);
						item.setNoPickupDelay();
						world.addEntity(item);
						core.getInventory().setStackInSlot(0, ItemStack.EMPTY);
					}
				} else {
					player.openContainer((INamedContainerProvider) core);
				}
			}
		}

		return true;
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof CraftingCoreTileEntity) {
				CraftingCoreTileEntity core = (CraftingCoreTileEntity) tile;
			}
		}

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean isEnabled() {
		return ModConfig.confCraftingCoreEnabled;
	}
}
