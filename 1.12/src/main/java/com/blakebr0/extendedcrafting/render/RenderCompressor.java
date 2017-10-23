package com.blakebr0.extendedcrafting.render;

import javax.annotation.Nullable;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.tile.TileCompressor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCompressor extends TileEntitySpecialRenderer<TileCompressor> {

	@Override
	public void render(TileCompressor tile, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());

		if (state == null || state.getBlock() != ModBlocks.blockCompressor) {
			return;
		}

		if (tile.getRecipe() != null) {
			ItemStack stack = tile.getRecipe().getOutput();
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, y + 1.4D, z + 0.5D);
			float scale = (float) (stack.getItem() instanceof ItemBlock ? 0.85F : 0.65F);
			GlStateManager.scale(scale, scale, scale);
			double tick = Minecraft.getSystemTime() / 800.0D;
			GlStateManager.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
			GlStateManager.rotate((float) (((tick * 40.0D) % 360)), 0, 1, 0);
			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.FIXED);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
}
