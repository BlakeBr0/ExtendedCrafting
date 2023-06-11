package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.extendedcrafting.tileentity.TheUltimateBlockTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TheUltimateBlock extends BaseTileEntityBlock {
    public TheUltimateBlock() {
        super(SoundType.METAL, 5.0F, 10.0F, true);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TheUltimateBlockTileEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
