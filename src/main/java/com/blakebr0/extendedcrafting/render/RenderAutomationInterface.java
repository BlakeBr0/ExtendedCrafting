package com.blakebr0.extendedcrafting.render;

import com.blakebr0.cucumber.render.GhostItemRenderer;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderAutomationInterface extends TileEntitySpecialRenderer<TileAutomationInterface> {

	@Override
	public void render(TileAutomationInterface tile, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());

		if (state == null || state.getBlock() != ModBlocks.blockAutomationInterface)
			return;

		ItemStack stack = tile.getResult();
		if (!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, y + 0.6D, z + 0.5D);
			float scale = (float) (stack.getItem() instanceof ItemBlock ? 0.30F : 0.35F);
			GlStateManager.scale(scale, scale, scale);
			double tick = Minecraft.getSystemTime() / 800.0D;
			GlStateManager.rotate((float) (((tick * 40.0D) % 360)), 0, 1, 0);
			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();
			GhostItemRenderer.renderItemModel(Minecraft.getMinecraft(), stack, 0.7F);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
}
