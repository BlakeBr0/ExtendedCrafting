package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class EnderCrafterScreen extends ContainerScreen<EnderCrafterContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/ender_crafter.png");

	public EnderCrafterScreen(EnderCrafterContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 176;
		this.ySize = 170;
	}
	
//	private int getProgressBarScaled(int pixels) {
//		int i = this.tile.getProgress();
//		int j = Math.max(this.tile.getProgressRequired(), i);
//		return j != 0 && i != 0 ? i * pixels / j : 0;
//	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = this.getTitle().getFormattedText();
		this.font.drawString(title, 32.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getFormattedText();
		this.font.drawString(inventory, 8.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.blit(x, y, 0, 0, this.xSize, this.ySize);
//		if (this.tile != null) {
//			if (this.tile.getProgress() > 0) {
//				int i2 = getProgressBarScaled(24);
//				this.drawTexturedModalRect(x + 89, y + 36, 194, 0, i2 + 1, 16);
//			}
//		} // TODO: progress bar
	}
}