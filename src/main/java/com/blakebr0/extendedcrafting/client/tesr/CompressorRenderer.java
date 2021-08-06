package com.blakebr0.extendedcrafting.client.tesr;

import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class CompressorRenderer extends BlockEntityRenderer<CompressorTileEntity> {
	public CompressorRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(CompressorTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int i, int i1) {
		if (!ModConfigs.ENABLE_COMPRESSOR_RENDERER.get())
			return;

		Minecraft minecraft = Minecraft.getInstance();
		CompressorRecipe recipe = tile.getActiveRecipe();
		if (recipe != null) {
			ItemStack stack = recipe.getResultItem();
			if (!stack.isEmpty()) {
				matrix.pushPose();
				matrix.translate(0.5D, 1.3D, 0.5D);
				float scale = stack.getItem() instanceof BlockItem ? 0.9F : 0.75F;
				matrix.scale(scale, scale, scale);
				double tick = System.currentTimeMillis() / 800.0D;
				matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
				matrix.mulPose(Vector3f.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
				minecraft.getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, i, i1, matrix, buffer);
				matrix.popPose();
			}
		}
	}
}
