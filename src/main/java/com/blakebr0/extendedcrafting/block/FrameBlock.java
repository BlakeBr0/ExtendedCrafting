package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

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
        super(Material.STONE, SoundType.STONE, 5.0F, 10.0F, ToolType.PICKAXE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return FRAME_SHAPE;
    }
}
