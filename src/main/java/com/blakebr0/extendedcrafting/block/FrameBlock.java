package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FrameBlock extends BaseBlock {
    private static final VoxelShape FRAME_SHAPE = VoxelShapeBuilder.builder()
            .cuboid(2, 2, 2, 14, 14, 14)
            .cuboid(0, 0, 0, 16, 3, 3)
            .cuboid(0, 13, 0, 16, 16, 3)
            .cuboid(0, 13, 13, 16, 16, 16)
            .cuboid(0, 0, 13, 16, 3, 16)
            .cuboid(0, 3, 0, 3, 13, 3)
            .cuboid(0, 3, 13, 3, 13, 16)
            .cuboid(13, 3, 13, 16, 13, 16)
            .cuboid(13, 3, 0, 16, 13, 3)
            .cuboid(0, 0, 3, 3, 3, 13)
            .cuboid(0, 13, 3, 3, 16, 13)
            .cuboid(13, 13, 3, 16, 16, 13)
            .cuboid(13, 0, 3, 16, 3, 13)
            .build();

    public FrameBlock() {
        super(SoundType.STONE, 5.0F, 10.0F, true);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FRAME_SHAPE;
    }
}
