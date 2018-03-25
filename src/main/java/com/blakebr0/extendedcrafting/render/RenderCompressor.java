package com.blakebr0.extendedcrafting.render;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL14;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.tile.TileCompressor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
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
			float scale = (float) (stack.getItem() instanceof ItemBlock ? 0.8F : 0.6F);
			GlStateManager.scale(scale, scale, scale);
			double tick = Minecraft.getSystemTime() / 800.0D;
			GlStateManager.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
			GlStateManager.rotate((float) (((tick * 40.0D) % 360)), 0, 1, 0);
			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();
			//GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			renderItemModel(Minecraft.getMinecraft(), Minecraft.getMinecraft().getRenderItem(), stack);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
	
    protected void renderItemModel(Minecraft mc, RenderItem render, ItemStack stack) {
        if (!stack.isEmpty()) {
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.pushMatrix();
            
            IBakedModel bakedmodel = render.getItemModelWithOverrides(stack, null, null);
            bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, TransformType.NONE, false);

            GlStateManager.enableBlend();
            GL14.glBlendColor(1, 1, 1, 0.7F);
            GlStateManager.blendFunc(SourceFactor.CONSTANT_ALPHA, DestFactor.ONE_MINUS_CONSTANT_ALPHA);

            render.renderItem(stack, bakedmodel);

            GL14.glBlendColor(1, 1, 1, 1);
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            
            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        }
    }
}
