package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

public class EnderCrafterBlock extends BaseTileEntityBlock implements IEnableable {
	public EnderCrafterBlock() {
		super(Material.METAL, SoundType.METAL, 6.0F, 12.0F, ToolType.PICKAXE);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new EnderCrafterTileEntity();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		if (!world.isClientSide()) {
			TileEntity tile = world.getBlockEntity(pos);

			if (tile instanceof EnderCrafterTileEntity){
				NetworkHooks.openGui((ServerPlayerEntity) player, (EnderCrafterTileEntity) tile, pos);
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		TileEntity tile = world.getBlockEntity(pos);

		if (tile instanceof EnderCrafterTileEntity) {
			EnderCrafterTileEntity table = (EnderCrafterTileEntity) tile;
			InventoryHelper.dropContents(world, pos, table.getInventory().getStacks());
		}

		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_ENDER_CRAFTER.get();
	}
}
