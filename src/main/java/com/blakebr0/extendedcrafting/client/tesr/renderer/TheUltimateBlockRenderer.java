package com.blakebr0.extendedcrafting.client.tesr.renderer;

import com.blakebr0.extendedcrafting.client.tesr.state.TheUltimateBlockRenderState;
import com.blakebr0.extendedcrafting.tileentity.TheUltimateBlockTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TheUltimateBlockRenderer implements BlockEntityRenderer<TheUltimateBlockTileEntity, TheUltimateBlockRenderState> {
    private final BlockModelResolver blockModelResolver;

    public TheUltimateBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
    }

    @Override
    public TheUltimateBlockRenderState createRenderState() {
        return new TheUltimateBlockRenderState();
    }

    @Override
    public void extractRenderState(TheUltimateBlockTileEntity tile, TheUltimateBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);

        this.blockModelResolver.update(state.blockModelRenderState, tile.getBlockState(), BlockDisplayContext.create());
    }

    @Override
    public void submit(TheUltimateBlockRenderState state, PoseStack matrix, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        matrix.pushPose();
        matrix.scale(1.0125f, 1.0125f, 1.0125f);
        matrix.translate(-0.005, -0.005, -0.005);
        state.blockModelRenderState.submit(matrix, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
        matrix.popPose();
    }
}
