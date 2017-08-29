package com.blakebr0.extendedcrafting.render;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.tile.TilePedestal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPedestal extends TileEntitySpecialRenderer<TilePedestal> {
	
	@Override
	public void renderTileEntityAt(TilePedestal tile, double x, double y, double z, float partialTick, int destroyStage) {
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
	
		if(state == null || state.getBlock() != ModBlocks.blockPedestal){
			return;
		}
		
		if(tile.getInventory().getStackInSlot(0) != null){
            GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, y + 1.4D, z + 0.5D);
			float scale = (float)(tile.getInventory().getStackInSlot(0).getItem() instanceof ItemBlock ? 0.85F : 0.65);
			GlStateManager.scale(scale, scale, scale);
            double tick = Minecraft.getSystemTime() / 800.0D;
            GlStateManager.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            GlStateManager.rotate((float)(((tick * 40.0D) % 360)), 0, 1, 0);
            GlStateManager.disableLighting();
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getRenderItem().renderItem(tile.getInventory().getStackInSlot(0), TransformType.FIXED);            
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
		}
	}
}
