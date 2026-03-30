package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.iface.IHoverTextProvider;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.init.ModDataComponentTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Consumer;

public class BasicAutoTableBlock extends BaseTileEntityBlock implements IHoverTextProvider {
    public static final VoxelShape BASIC_AUTO_TABLE_SHAPE = VoxelShapeBuilder.builder()
            .cuboid(2, 0, 2, 14, 2, 14)
            .cuboid(3, 2, 3, 5, 10, 5)
            .cuboid(11, 2, 11, 13, 10, 13)
            .cuboid(11, 2, 3, 13, 10, 5)
            .cuboid(3, 2, 11, 5, 10, 13)
            .cuboid(0, 10, 0, 16, 16, 16)
            .cuboid(4, 2, 4, 12, 10, 12)
            .build();

    public BasicAutoTableBlock(Identifier id) {
        super(id, SoundType.METAL, 5.0F, 10.0F, true);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AutoTableTileEntity.Basic(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            var tile = level.getBlockEntity(pos);

            if (tile instanceof AutoTableTileEntity.Basic table) {
                player.openMenu(table, pos);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        var storage = stack.get(ModDataComponentTypes.TABLE_RECIPE_STORAGE);
        if (storage != null) {
            var tile = level.getBlockEntity(pos);

            if (tile instanceof AutoTableTileEntity.Basic table) {
                table.getRecipeStorage().deserialize(storage.data());
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BASIC_AUTO_TABLE_SHAPE;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(ModTooltips.TIER.args(1).toComponent());

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
        return createTicker(type, ModTileEntities.BASIC_AUTO_TABLE.get(), AutoTableTileEntity::tick);
    }
}
