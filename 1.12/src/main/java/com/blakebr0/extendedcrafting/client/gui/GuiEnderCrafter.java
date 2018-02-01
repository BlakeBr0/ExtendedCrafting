package com.blakebr0.extendedcrafting.client.gui;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.container.ContainerBasicTable;
import com.blakebr0.extendedcrafting.client.container.ContainerEnderCrafter;
import com.blakebr0.extendedcrafting.tile.TileBasicCraftingTable;
import com.blakebr0.extendedcrafting.tile.TileEnderCrafter;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiEnderCrafter extends GuiContainer {

	private static final ResourceLocation GUI = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/ender_crafter.png");
	private TileEnderCrafter tile;

	public GuiEnderCrafter(TileEnderCrafter tile, ContainerEnderCrafter container) {
		super(container);
		this.tile = tile;
		this.xSize = 176;
		this.ySize = 166;
	}
	
	private int getProgressBarScaled(int pixels) {
		int i = this.tile.getProgress();
		int j = this.tile.getProgressRequired();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		if (this.tile != null) {
			if (this.tile.getProgress() > 0) {
				int i2 = getProgressBarScaled(24);
				this.drawTexturedModalRect(x + 89, y + 35, 194, 0, i2 + 1, 16);
			}
		}
	}
}