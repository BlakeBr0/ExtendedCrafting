package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

public class EnderCrafterBlock extends BaseTileEntityBlock implements IEnableable {
	public EnderCrafterBlock() {
		super(Material.METAL, SoundType.METAL, 6.0F, 12.0F, ToolType.PICKAXE);
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new EnderCrafterTileEntity();
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		if (!world.isClientSide()) {
			BlockEntity tile = world.getBlockEntity(pos);

			if (tile instanceof EnderCrafterTileEntity){
				NetworkHooks.openGui((ServerPlayer) player, (EnderCrafterTileEntity) tile, pos);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		BlockEntity tile = world.getBlockEntity(pos);

		if (tile instanceof EnderCrafterTileEntity) {
			EnderCrafterTileEntity table = (EnderCrafterTileEntity) tile;
			Containers.dropContents(world, pos, table.getInventory().getStacks());
		}

		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_ENDER_CRAFTER.get();
	}
}
