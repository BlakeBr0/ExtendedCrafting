package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PedestalBlock extends BaseTileEntityBlock {
	public static final VoxelShape PEDESTAL_SHAPE = VoxelShapeBuilder.builder()
			.cuboid(3, 2, 3, 13, 14, 13)
			.cuboid(1, 0, 1, 15, 2, 15)
			.cuboid(2, 14, 2, 14, 16, 14)
			.build();

	public PedestalBlock() {
		super(SoundType.METAL, 5.0F, 10.0F, true);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PedestalTileEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		var tile = level.getBlockEntity(pos);

		if (tile instanceof PedestalTileEntity pedestal) {
			var inventory = pedestal.getInventory();
			var input = inventory.getStackInSlot(0);
			var held = player.getItemInHand(hand);

			if (input.isEmpty() && !held.isEmpty()) {
				inventory.setStackInSlot(0, StackHelper.withSize(held, 1, false));
				player.setItemInHand(hand, StackHelper.shrink(held, 1, false));
				level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
			} else if (!input.isEmpty()) {
				var item = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), input);

				item.setNoPickUpDelay();
				level.addFreshEntity(item);
				inventory.setStackInSlot(0, ItemStack.EMPTY);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			var tile = level.getBlockEntity(pos);

			if (tile instanceof PedestalTileEntity pedestal) {
				Containers.dropContents(level, pos, pedestal.getInventory().getStacks());
			}
		}

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return PEDESTAL_SHAPE;
	}
}
