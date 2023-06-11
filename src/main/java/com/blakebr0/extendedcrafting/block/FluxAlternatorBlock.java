package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.tileentity.FluxAlternatorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class FluxAlternatorBlock extends BaseTileEntityBlock {
	private static final VoxelShape FLUX_CRAFTER_SHAPE = VoxelShapeBuilder.builder()
			.cuboid(0, 0, 0, 16, 6, 16)
			.cuboid(4, 6, 4, 12, 15, 12)
			.cuboid(3, 15, 3, 13, 16, 13)
			.build();

	public FluxAlternatorBlock() {
		super(SoundType.METAL, 5.0F, 10.0F, true);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FluxAlternatorTileEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		if (!level.isClientSide()) {
			var tile = level.getBlockEntity(pos);

			if (tile instanceof FluxAlternatorTileEntity alternator) {
				NetworkHooks.openScreen((ServerPlayer) player, alternator, pos);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return FLUX_CRAFTER_SHAPE;
	}
}
