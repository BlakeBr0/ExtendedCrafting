package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class PedestalBlock extends BaseTileEntityBlock implements IEnableable {
	public static final VoxelShape PEDESTAL_SHAPE = new VoxelShapeBuilder()
			.cuboid(13, 14, 13, 3, 2, 3)
			.cuboid(15, 2, 15, 1, 0, 1)
			.cuboid(14, 16, 14, 2, 14, 2)
			.build();

	public PedestalBlock() {
		super(Material.IRON, SoundType.METAL, 5.0F, 10.0F);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new PedestalTileEntity();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof PedestalTileEntity) {
			PedestalTileEntity pedestal = (PedestalTileEntity) tile;
			BaseItemStackHandler inventory = pedestal.getInventory();
			ItemStack input = inventory.getStackInSlot(0);
			ItemStack held = player.getHeldItem(hand);
			if (input.isEmpty() && !held.isEmpty()) {
				inventory.setStackInSlot(0, StackHelper.withSize(held, 1, false));
				player.setHeldItem(hand, StackHelper.shrink(held, 1, false));
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			} else if (!input.isEmpty()) {
				ItemEntity item = new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), input);
				item.setNoPickupDelay();
				world.addEntity(item);
				inventory.setStackInSlot(0, ItemStack.EMPTY);
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof PedestalTileEntity) {
				PedestalTileEntity pedestal = (PedestalTileEntity) tile;
				InventoryHelper.dropItems(world, pos, pedestal.getInventory().getStacks());
			}
		}

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return PEDESTAL_SHAPE;
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_CRAFTING_CORE.get();
	}
}
