package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class EnderCrafterBlock extends BaseTileEntityBlock implements IEnableable {
	public EnderCrafterBlock() {
		super(Material.METAL, SoundType.METAL, 6.0F, 12.0F, true);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnderCrafterTileEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		if (!level.isClientSide()) {
			var tile = level.getBlockEntity(pos);

			if (tile instanceof EnderCrafterTileEntity table) {
				NetworkHooks.openGui((ServerPlayer) player, table, pos);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			var tile = level.getBlockEntity(pos);

			if (tile instanceof EnderCrafterTileEntity table) {
				Containers.dropContents(level, pos, table.getInventory().getStacks());
			}
		}

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_ENDER_CRAFTER.get();
	}

	@Override
	protected <T extends BlockEntity> BlockEntityTicker<T> getServerTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTicker(type, ModTileEntities.ENDER_CRAFTER.get(), EnderCrafterTileEntity::tick);
	}

	@Override
	protected <T extends BlockEntity> BlockEntityTicker<T> getClientTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTicker(type, ModTileEntities.ENDER_CRAFTER.get(), EnderCrafterTileEntity::tick);
	}
}
