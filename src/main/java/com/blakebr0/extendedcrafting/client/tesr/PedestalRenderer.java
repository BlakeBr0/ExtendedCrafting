package com.blakebr0.extendedcrafting.client.tesr;

import com.blakebr0.extendedcrafting.tileentity.PedestalTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class PedestalRenderer extends TileEntityRenderer<PedestalTileEntity> {
	public PedestalRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(PedestalTileEntity tile, float v, MatrixStack matrix, IRenderTypeBuffer buffer, int i, int i1) {
		ItemStack stack = tile.getInventory().getStackInSlot(0);
		if (!stack.isEmpty()) {
			matrix.push();
			matrix.translate(0.5D, 1.4D, 0.5D);
			float scale = stack.getItem() instanceof BlockItem ? 0.85F : 0.65F;
			matrix.scale(scale, scale, scale);
			double tick = System.currentTimeMillis() / 800.0D;
			matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
			matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float) ((tick * 40.0D) % 360)));
			Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, i, i1, matrix, buffer);
			matrix.pop();
		}
	}
}
