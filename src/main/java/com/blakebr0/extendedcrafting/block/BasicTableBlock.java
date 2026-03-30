package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.iface.IHoverTextProvider;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.BasicTableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Consumer;

public class BasicTableBlock extends BaseTileEntityBlock implements IHoverTextProvider {
	public static final VoxelShape BASIC_TABLE_SHAPE = VoxelShapeBuilder.builder()
			.cuboid(2, 0, 2, 14, 2, 14)
			.cuboid(3, 2, 3, 5, 10, 5)
			.cuboid(11, 2, 11, 13, 10, 13)
			.cuboid(11, 2, 3, 13, 10, 5)
			.cuboid(3, 2, 11, 5, 10, 13)
			.cuboid(0, 10, 0, 16, 16, 16)
			.build();

	public BasicTableBlock(Identifier id) {
		super(id, SoundType.METAL, 5.0F, 10.0F, true);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BasicTableTileEntity(pos, state);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!level.isClientSide()) {
			var tile = level.getBlockEntity(pos);

			if (tile instanceof BasicTableTileEntity table) {
				player.openMenu(table, pos);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return BASIC_TABLE_SHAPE;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		builder.accept(ModTooltips.TIER.args(1).toComponent());
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return BlockHelper.getRedstoneSignalFromInventory(level.getBlockEntity(pos));
	}
}
