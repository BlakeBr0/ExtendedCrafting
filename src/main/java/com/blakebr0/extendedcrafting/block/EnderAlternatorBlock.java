package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnderAlternatorBlock extends BaseBlock {
	private static final VoxelShape SHAPE_DOWN = VoxelShapeBuilder.builder().cuboid(0, 10, 0, 16, 16, 16).cuboid(4, 0, 4, 12, 10, 12).build();
	private static final VoxelShape SHAPE_UP = VoxelShapeBuilder.builder().cuboid(0, 0, 0, 16, 6, 16).cuboid(4, 6, 4, 12, 16, 12).build();
	private static final VoxelShape SHAPE_NORTH = VoxelShapeBuilder.builder().cuboid(0, 0, 10, 16, 16, 16).cuboid(4, 4, 0, 12, 12, 10).build();
	private static final VoxelShape SHAPE_SOUTH = VoxelShapeBuilder.builder().cuboid(0, 0, 0, 16, 16, 6).cuboid(4, 4, 6, 12, 12, 16).build();
	private static final VoxelShape SHAPE_EAST = VoxelShapeBuilder.builder().cuboid(10, 0, 0, 16, 16, 16).cuboid(0, 4, 4, 16, 12, 12).build();
	private static final VoxelShape SHAPE_WEST = VoxelShapeBuilder.builder().cuboid(0, 0, 0, 6, 16, 16).cuboid(6, 4, 4, 16, 12, 12).build();

	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public EnderAlternatorBlock() {
		super(SoundType.METAL, 5.0F, 10.0F, true);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getClickedFace());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case DOWN -> SHAPE_DOWN;
			case UP -> SHAPE_UP;
			case NORTH -> SHAPE_NORTH;
			case SOUTH -> SHAPE_SOUTH;
			case WEST -> SHAPE_EAST;
			case EAST -> SHAPE_WEST;
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
