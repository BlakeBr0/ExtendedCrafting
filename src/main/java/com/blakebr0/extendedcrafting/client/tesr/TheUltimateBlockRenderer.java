package com.blakebr0.extendedcrafting.client.tesr;

import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.tileentity.TheUltimateBlockTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.client.model.data.ModelData;

public class TheUltimateBlockRenderer implements BlockEntityRenderer<TheUltimateBlockTileEntity> {
    public TheUltimateBlockRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(TheUltimateBlockTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int i, int i1) {
        var minecraft = Minecraft.getInstance();

        var level = tile.getLevel();
        if (level == null)
            return;

        var pos = tile.getBlockPos();
        var vertex = buffer.getBuffer(RenderType.solid());
        var state = ModBlocks.THE_ULTIMATE_BLOCK.get().defaultBlockState();

        matrix.pushPose();
        matrix.scale(1.0125f, 1.0125f, 1.0125f);
        matrix.translate(-0.005, -0.005, -0.005);
        minecraft.getBlockRenderer().renderBatched(state, pos, level, matrix, vertex, false, level.getRandom(), ModelData.EMPTY, RenderType.solid());
        matrix.popPose();
    }
}
