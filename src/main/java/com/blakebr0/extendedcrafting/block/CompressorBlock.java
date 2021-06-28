package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

public class CompressorBlock extends BaseTileEntityBlock implements IEnableable {
	private static final DirectionProperty FACING = HorizontalBlock.FACING;

	public CompressorBlock() {
		super(Material.METAL, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CompressorTileEntity();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		if (!world.isClientSide()) {
			TileEntity tile = world.getBlockEntity(pos);
			if (tile instanceof CompressorTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile, pos);
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = world.getBlockEntity(pos);
			if (tile instanceof CompressorTileEntity) {
				CompressorTileEntity compressor = (CompressorTileEntity) tile;
				InventoryHelper.dropContents(world, pos, compressor.getInventory().getStacks());
			}
		}

		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, World world, BlockPos pos) {
		return Container.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
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
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_COMPRESSOR.get();
	}
}
