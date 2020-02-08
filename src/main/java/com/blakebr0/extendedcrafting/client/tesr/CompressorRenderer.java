package com.blakebr0.extendedcrafting.client.tesr;

import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class CompressorRenderer extends TileEntityRenderer<CompressorTileEntity> {
	public CompressorRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(CompressorTileEntity tile, float v, MatrixStack matrix, IRenderTypeBuffer buffer, int i, int i1) {
		if (!ModConfigs.ENABLE_COMPRESSOR_RENDERER.get())
			return;

		CompressorRecipe recipe = null; // tile.getRecipe();
		if (recipe != null) {
			ItemStack stack = recipe.getRecipeOutput();
			if (!stack.isEmpty()) {
				matrix.push();
				matrix.translate(0.5D, 1.4D, 0.5D);
				float scale = stack.getItem() instanceof BlockItem ? 0.8F : 0.6F;
				matrix.scale(scale, scale, scale);
				double tick = System.currentTimeMillis() / 800.0D;
				matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
				matrix.rotate(Vector3f.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
//				GhostItemRenderer.renderItemModel(Minecraft.getMinecraft(), stack, 0.7F); // TODO: ghost model item renderer
				matrix.pop();
			}
		}
	}
}
