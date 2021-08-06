package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolType;

public class PedestalBlock extends BaseTileEntityBlock implements IEnableable {
	public static final VoxelShape PEDESTAL_SHAPE = new VoxelShapeBuilder()
			.cuboid(13, 14, 13, 3, 2, 3)
			.cuboid(15, 2, 15, 1, 0, 1)
			.cuboid(14, 16, 14, 2, 14, 2)
			.build();

	public PedestalBlock() {
		super(Material.METAL, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE);
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new PedestalTileEntity();
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof PedestalTileEntity) {
			PedestalTileEntity pedestal = (PedestalTileEntity) tile;
			BaseItemStackHandler inventory = pedestal.getInventory();
			ItemStack input = inventory.getStackInSlot(0);
			ItemStack held = player.getItemInHand(hand);
			if (input.isEmpty() && !held.isEmpty()) {
				inventory.setStackInSlot(0, StackHelper.withSize(held, 1, false));
				player.setItemInHand(hand, StackHelper.shrink(held, 1, false));
				world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
			} else if (!input.isEmpty()) {
				ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), input);
				item.setNoPickUpDelay();
				world.addFreshEntity(item);
				inventory.setStackInSlot(0, ItemStack.EMPTY);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tile = world.getBlockEntity(pos);
			if (tile instanceof PedestalTileEntity) {
				PedestalTileEntity pedestal = (PedestalTileEntity) tile;
				Containers.dropContents(world, pos, pedestal.getInventory().getStacks());
			}
		}

		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return PEDESTAL_SHAPE;
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_CRAFTING_CORE.get();
	}
}
