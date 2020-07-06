package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class CraftingCoreBlock extends BaseTileEntityBlock implements IEnableable {
	public static final VoxelShape CRAFTING_CORE_SHAPE = new VoxelShapeBuilder()
			.cuboid(14.0, 2.0, 14.0, 2.0, 0.0, 2.0)
			.cuboid(16.0, 5.0, 16.0, 0.0, 2.0, 0.0)
			.cuboid(15.0, 6.0, 15.0, 1.0, 5.0, 1.0)
			.cuboid(16.0, 12.0, 16.0, 0.0, 6.0, 0.0)
			.cuboid(15.0, 13.0, 15.0, 1.0, 12.0, 1.0)
			.cuboid(16.0, 16.0, 16.0, 0.0, 13.0, 0.0)
			.build();

	public CraftingCoreBlock() {
		super(Material.IRON, SoundType.METAL, 5.0F, 10.0F);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CraftingCoreTileEntity();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		ItemStack held = player.getHeldItem(hand);
		if (!world.isRemote()) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof CraftingCoreTileEntity) {
				CraftingCoreTileEntity core = (CraftingCoreTileEntity) tile;
				if (trace.getFace() == Direction.UP) {
					BaseItemStackHandler inventory = core.getInventory();
					ItemStack stack = inventory.getStackInSlot(0);
					if (stack.isEmpty()) {
						if (!held.isEmpty()) {
							inventory.setStackInSlot(0, StackHelper.withSize(held, 1, false));
							player.setHeldItem(hand, StackHelper.shrink(held, 1, false));
							world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					} else {
						ItemEntity item = new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), stack);
						item.setNoPickupDelay();
						world.addEntity(item);
						inventory.setStackInSlot(0, ItemStack.EMPTY);
					}
				} else {
					NetworkHooks.openGui((ServerPlayerEntity) player, core, pos);
				}
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof CraftingCoreTileEntity) {
				CraftingCoreTileEntity core = (CraftingCoreTileEntity) tile;
				InventoryHelper.dropItems(world, pos, core.getInventory().getStacks());
			}
		}

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return CRAFTING_CORE_SHAPE;
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_CRAFTING_CORE.get();
	}
}
