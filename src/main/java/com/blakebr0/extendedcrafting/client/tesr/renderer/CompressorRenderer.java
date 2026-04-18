package com.blakebr0.extendedcrafting.client.tesr.renderer;

import com.blakebr0.extendedcrafting.client.tesr.state.CompressorRenderState;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CompressorRenderer implements BlockEntityRenderer<CompressorTileEntity, CompressorRenderState> {
	public CompressorRenderer(BlockEntityRendererProvider.Context context) { }

//	TODO renderer
//	@Override
//	public void render(CompressorTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int i, int i1) {
//		if (!ModConfigs.ENABLE_COMPRESSOR_RENDERER.get())
//			return;
//
//		var minecraft = Minecraft.getInstance();
//		var level = minecraft.level;
//		if (level == null)
//			return;
//
//		var recipe = tile.getActiveRecipe();
//
//		if (recipe != null) {
//			var stack = recipe.getResultItem(level.registryAccess());
//
//			if (!stack.isEmpty()) {
//				matrix.pushPose();
//				matrix.translate(0.5D, 1.3D, 0.5D);
//				float scale = stack.getItem() instanceof BlockItem ? 0.9F : 0.75F;
//				matrix.scale(scale, scale, scale);
//				double tick = System.currentTimeMillis() / 800.0D;
//				matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
//				matrix.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
//				minecraft.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, 234, i1, matrix, buffer, level, 0);
//				matrix.popPose();
//			}
//		}
//	}

	@Override
	public CompressorRenderState createRenderState() {
		return new CompressorRenderState();
	}

	@Override
	public void extractRenderState(CompressorTileEntity blockEntity, CompressorRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
	}

	@Override
	public void submit(CompressorRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {

	}
}
