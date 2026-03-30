package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.neoforged.neoforge.transfer.item.ItemResource;

public class CraftingCoreBlock extends BaseTileEntityBlock {
	public static final VoxelShape CRAFTING_CORE_SHAPE = VoxelShapeBuilder.builder()
			.cuboid(2, 0, 2, 14, 2, 14)
			.cuboid(0, 2, 0, 16, 5, 16)
			.cuboid(1, 5, 1, 15, 6, 15)
			.cuboid(0, 6, 0, 16, 12, 16)
			.cuboid(1, 12, 1, 15, 13, 15)
			.cuboid(0, 13, 0, 16, 16, 16)
			.build();

	public CraftingCoreBlock(Identifier id) {
		super(id, SoundType.METAL, 5.0F, 10.0F, true);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CraftingCoreTileEntity(pos, state);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!level.isClientSide()) {
			var tile = level.getBlockEntity(pos);

			if (tile instanceof CraftingCoreTileEntity core) {
				if (hitResult.getDirection() == Direction.UP) {
					var inventory = core.getInventory();
					var input = inventory.getResource(0);
					var held = player.getItemInHand(hand);

					if (input.isEmpty()) {
						if (!held.isEmpty()) {
							inventory.set(0, ItemResource.of(held), 1);
							player.setItemInHand(hand, StackHelper.shrink(held, 1, false));
							level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
						}
					} else {
						var item = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), input.toStack());

						item.setNoPickUpDelay();
						level.addFreshEntity(item);
						inventory.set(0, ItemResource.EMPTY, 0);
					}
				} else {
					player.openMenu(core, pos);
				}
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return CRAFTING_CORE_SHAPE;
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return BlockHelper.getRedstoneSignalFromInventory(level.getBlockEntity(pos));
	}

	@Override
	protected <T extends BlockEntity> BlockEntityTicker<T> getServerTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTicker(type, ModTileEntities.CRAFTING_CORE.get(), CraftingCoreTileEntity::tick);
	}
}
