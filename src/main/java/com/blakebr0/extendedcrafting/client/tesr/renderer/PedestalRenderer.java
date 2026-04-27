package com.blakebr0.extendedcrafting.client.tesr.renderer;

import com.blakebr0.extendedcrafting.client.tesr.state.PedestalRenderState;
import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PedestalRenderer implements BlockEntityRenderer<PedestalTileEntity, PedestalRenderState> {
	private final ItemModelResolver itemModelResolver;

	public PedestalRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public PedestalRenderState createRenderState() {
		return new PedestalRenderState();
	}

	@Override
	public void extractRenderState(PedestalTileEntity tile, PedestalRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);

		state.itemResource = tile.getInventory().getResource(0);

		int seed = HashCommon.long2int(state.blockPos.asLong());

		this.itemModelResolver.updateForTopItem(state.itemRenderState,  state.itemResource.toStack(), ItemDisplayContext.FIXED, tile.getLevel(), null, seed);
	}

	@Override
	public void submit(PedestalRenderState state, PoseStack matrix, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (!state.itemResource.isEmpty()) {
			matrix.pushPose();
			matrix.translate(0.5D, 1.2D, 0.5D);
			float scale = state.itemResource.getItem() instanceof BlockItem blockItem && !(blockItem.getBlock() instanceof CropBlock) ? 0.55F : 0.35F;
			matrix.scale(scale, scale, scale);
			double tick = System.currentTimeMillis() / 800.0D;
			matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
			matrix.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
			state.itemRenderState.submit(matrix, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			matrix.popPose();
		}
	}
}
