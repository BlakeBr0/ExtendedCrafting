package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class PedestalBlock extends BaseTileEntityBlock {
	public static final VoxelShape PEDESTAL_SHAPE = VoxelShapeBuilder.builder()
			.cuboid(3, 2, 3, 13, 14, 13)
			.cuboid(1, 0, 1, 15, 2, 15)
			.cuboid(2, 14, 2, 14, 16, 14)
			.build();

	public PedestalBlock(Identifier id) {
		super(id, SoundType.METAL, 5.0F, 10.0F, true);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PedestalTileEntity(pos, state);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		var tile = level.getBlockEntity(pos);

		if (tile instanceof PedestalTileEntity pedestal) {
			var inventory = pedestal.getInventory();
			var input = inventory.getResource(0);
			var held = player.getItemInHand(hand);

			if (input.isEmpty() && !held.isEmpty()) {
				inventory.set(0, ItemResource.of(held), 1);
				player.setItemInHand(hand, held.copyWithCount(held.count() - 1));
				level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
			} else if (!input.isEmpty()) {
				var item = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), input.toStack());

				item.setNoPickUpDelay();
				level.addFreshEntity(item);
				inventory.set(0, ItemResource.EMPTY, 0);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return PEDESTAL_SHAPE;
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return BlockHelper.getRedstoneSignalFromInventory(level.getBlockEntity(pos));
	}
}
