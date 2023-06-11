package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class CraftingCoreBlock extends BaseTileEntityBlock {
	public static final VoxelShape CRAFTING_CORE_SHAPE = VoxelShapeBuilder.builder()
			.cuboid(2, 0, 2, 14, 2, 14)
			.cuboid(0, 2, 0, 16, 5, 16)
			.cuboid(1, 5, 1, 15, 6, 15)
			.cuboid(0, 6, 0, 16, 12, 16)
			.cuboid(1, 12, 1, 15, 13, 15)
			.cuboid(0, 13, 0, 16, 16, 16)
			.build();

	public CraftingCoreBlock() {
		super(SoundType.METAL, 5.0F, 10.0F, true);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CraftingCoreTileEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		var held = player.getItemInHand(hand);

		if (!level.isClientSide()) {
			var tile = level.getBlockEntity(pos);

			if (tile instanceof CraftingCoreTileEntity core) {
				if (trace.getDirection() == Direction.UP) {
					var inventory = core.getInventory();
					var stack = inventory.getStackInSlot(0);

					if (stack.isEmpty()) {
						if (!held.isEmpty()) {
							inventory.setStackInSlot(0, StackHelper.withSize(held, 1, false));
							player.setItemInHand(hand, StackHelper.shrink(held, 1, false));
							level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
						}
					} else {
						var item = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), stack);

						item.setNoPickUpDelay();
						level.addFreshEntity(item);
						inventory.setStackInSlot(0, ItemStack.EMPTY);
					}
				} else {
					NetworkHooks.openScreen((ServerPlayer) player, core, pos);
				}
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			var tile = level.getBlockEntity(pos);

			if (tile instanceof CraftingCoreTileEntity core) {
				Containers.dropContents(level, pos, core.getInventory().getStacks());
			}
		}

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return CRAFTING_CORE_SHAPE;
	}

	@Override
	protected <T extends BlockEntity> BlockEntityTicker<T> getServerTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTicker(type, ModTileEntities.CRAFTING_CORE.get(), CraftingCoreTileEntity::tick);
	}
}
