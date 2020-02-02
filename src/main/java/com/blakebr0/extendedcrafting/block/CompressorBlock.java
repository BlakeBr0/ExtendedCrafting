package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CompressorBlock extends BaseTileEntityBlock implements IEnableable {
	private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final VoxelShape COMPRESSOR_SHAPE = new VoxelShapeBuilder()
			.cuboid(16.0, 15.0, 15.0, 0.0, 0.0, 0.0)
			.cuboid(3.0, 8.0, 16.0, 0.0, 0.0, 15.0)
			.cuboid(16.0, 8.0, 16.0, 13.0, 0.0, 15.0)
			.cuboid(13.0, 1.0, 16.0, 3.0, 0.0, 15.0)
			.cuboid(16.0, 15.0, 16.0, 0.0, 8.0, 15.0)
			.cuboid(4.0, 16.0, 16.0, 0.0, 15.0, 0.0)
			.cuboid(16.0, 16.0, 16.0, 12.0, 15.0, 0.0)
			.cuboid(12.0, 16.0, 4.0, 4.0, 15.0, 0.0)
			.cuboid(12.0, 16.0, 16.0, 4.0, 15.0, 12.0)
			.cuboid(13.0, 8.0, 15.1, 3.0, 1.0, 15.1)
			.build();

	public CompressorBlock() {
		super(Material.IRON, SoundType.METAL, 5.0F, 10.0F);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CompressorTileEntity();
	}

	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		if (!world.isRemote()) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof CompressorTileEntity)
				player.openContainer((CompressorTileEntity) tile);
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof CompressorTileEntity) {
				CompressorTileEntity compressor = (CompressorTileEntity) tile;
				InventoryHelper.dropItems(world, pos, compressor.getInventory().getStacks());
			}
		}

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return Container.calcRedstone(world.getTileEntity(pos));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.toRotation(state.get(FACING)));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return COMPRESSOR_SHAPE;
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.confCompressorEnabled;
	}
}
