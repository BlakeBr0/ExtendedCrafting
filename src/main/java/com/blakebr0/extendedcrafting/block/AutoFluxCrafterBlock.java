package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.iface.IHoverTextProvider;
import com.blakebr0.extendedcrafting.init.ModDataComponentTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AutoFluxCrafterTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Consumer;

public class AutoFluxCrafterBlock extends BaseTileEntityBlock implements IHoverTextProvider {
	public AutoFluxCrafterBlock(Identifier id) {
		super(id, SoundType.METAL, 6.0F, 12.0F, true);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AutoFluxCrafterTileEntity(pos, state);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!level.isClientSide()) {
			var tile = level.getBlockEntity(pos);

			if (tile instanceof AutoFluxCrafterTileEntity crafter) {
				player.openMenu(crafter, pos);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		var storage = stack.get(ModDataComponentTypes.TABLE_RECIPE_STORAGE);
		if (storage != null) {
			var tile = level.getBlockEntity(pos);

			if (tile instanceof AutoFluxCrafterTileEntity crafter) {
				crafter.getRecipeStorage().deserialize(storage.data());
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		var storage = stack.get(ModDataComponentTypes.TABLE_RECIPE_STORAGE);
		if (storage != null && storage.recipeCount() > 0) {
			builder.accept(ModTooltips.RECIPE_COUNT.args(storage.recipeCount()).toComponent());
		}
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return BlockHelper.getRedstoneSignalFromInventory(level.getBlockEntity(pos));
	}

	@Override
	protected <T extends BlockEntity> BlockEntityTicker<T> getServerTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTicker(type, ModTileEntities.AUTO_FLUX_CRAFTER.get(), AutoFluxCrafterTileEntity::tick);
	}
}
