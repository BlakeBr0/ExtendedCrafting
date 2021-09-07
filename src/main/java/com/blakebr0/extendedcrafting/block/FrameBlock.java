package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FrameBlock extends BaseBlock {
    private static final VoxelShape FRAME_SHAPE = new VoxelShapeBuilder()
            .cuboid(14.0, 14.0, 14.0, 2.0, 2.0, 2.0)
            .cuboid(16.0, 3.0, 3.0, 0.0, 0.0, 0.0)
            .cuboid(16.0, 16.0, 3.0, 0.0, 13.0, 0.0)
            .cuboid(16.0, 16.0, 16.0, 0.0, 13.0, 13.0)
            .cuboid(16.0, 3.0, 16.0, 0.0, 0.0, 13.0)
            .cuboid(3.0, 13.0, 3.0, 0.0, 3.0, 0.0)
            .cuboid(3.0, 13.0, 16.0, 0.0, 3.0, 13.0)
            .cuboid(16.0, 13.0, 16.0, 13.0, 3.0, 13.0)
            .cuboid(16.0, 13.0, 3.0, 13.0, 3.0, 0.0)
            .cuboid(3.0, 3.0, 13.0, 0.0, 0.0, 3.0)
            .cuboid(3.0, 16.0, 13.0, 0.0, 13.0, 3.0)
            .cuboid(16.0, 16.0, 13.0, 13.0, 13.0, 3.0)
            .cuboid(16.0, 3.0, 13.0, 13.0, 0.0, 3.0)
            .build();

    public FrameBlock() {
        super(Material.STONE, SoundType.STONE, 5.0F, 10.0F, true);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FRAME_SHAPE;
    }
}
