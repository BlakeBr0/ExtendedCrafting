package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class EliteAutoTableBlock extends BaseTileEntityBlock {
    public static final VoxelShape ELITE_AUTO_TABLE_SHAPE = VoxelShapeBuilder.builder()
            .cuboid(2, 0, 2, 14, 2, 14)
            .cuboid(3, 2, 3, 5, 10, 5)
            .cuboid(11, 2, 11, 13, 10, 13)
            .cuboid(11, 2, 3, 13, 10, 5)
            .cuboid(3, 2, 11, 5, 10, 13)
            .cuboid(0, 10, 0, 16, 16, 16)
            .cuboid(4, 2, 4, 12, 10, 12)
            .build();

    public EliteAutoTableBlock() {
        super(SoundType.METAL, 5.0F, 10.0F, true);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AutoTableTileEntity.Elite(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
        if (!level.isClientSide()) {
            var tile = level.getBlockEntity(pos);

            if (tile instanceof AutoTableTileEntity.Elite table) {
                NetworkHooks.openScreen((ServerPlayer) player, table, pos);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            var tile = level.getBlockEntity(pos);

            if (tile instanceof AutoTableTileEntity.Elite table) {
                Containers.dropContents(level, pos, table.getInventory().getStacks());
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (NBTHelper.hasKey(stack, "RecipeStorage")) {
            var tile = level.getBlockEntity(pos);

            if (tile instanceof AutoTableTileEntity.Elite table) {
                var storage = stack.getTag().getCompound("RecipeStorage");

                table.getRecipeStorage().deserializeNBT(storage);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ELITE_AUTO_TABLE_SHAPE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(ModTooltips.TIER.args(3).build());

        var recipes = NBTHelper.getInt(stack, "RecipeCount");

        if (recipes > 0) {
            tooltip.add(ModTooltips.RECIPE_COUNT.args(recipes).build());
        }
    }

    @Override
    protected <T extends BlockEntity> BlockEntityTicker<T> getServerTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTicker(type, ModTileEntities.ELITE_AUTO_TABLE.get(), AutoTableTileEntity::tick);
    }
}
