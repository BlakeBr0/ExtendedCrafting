package com.blakebr0.extendedcrafting.client.tesr;

import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;

public class PedestalRenderer implements BlockEntityRenderer<PedestalTileEntity> {
	public PedestalRenderer(BlockEntityRendererProvider.Context context) { }

	@Override
	public void render(PedestalTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int i, int i1) {
		var minecraft = Minecraft.getInstance();
		var stack = tile.getInventory().getStackInSlot(0);

		if (!stack.isEmpty()) {
			matrix.pushPose();
			matrix.translate(0.5D, 1.2D, 0.5D);
			float scale = stack.getItem() instanceof BlockItem ? 0.9F : 0.65F;
			matrix.scale(scale, scale, scale);
			double tick = System.currentTimeMillis() / 800.0D;
			matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
			matrix.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
			minecraft.getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, i, i1, matrix, buffer, 0);
			matrix.popPose();
		}
	}
}
