package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.helper.RenderHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class UltimateAutoTableScreen extends ContainerScreen<UltimateAutoTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/ultimate_auto_table.png");

	public UltimateAutoTableScreen(UltimateAutoTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 254;
		this.ySize = 278;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int left = this.guiLeft;
		int top = this.guiTop;
		UltimateAutoTableContainer container = this.getContainer();

		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		if (mouseX > left + 7 && mouseX < left + 20 && mouseY > top + 59 && mouseY < top + 136) {
			this.renderTooltip(container.getEnergyStored() + " FE", mouseX, mouseY);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = this.getTitle().getFormattedText();
		this.font.drawString(title, 26.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getFormattedText();
		this.font.drawString(inventory, 47.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		UltimateAutoTableContainer container = this.getContainer();

		RenderHelper.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize, 512, 512);

		int i1 = container.getEnergyBarScaled(78);
		this.blit(x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);
	}
}