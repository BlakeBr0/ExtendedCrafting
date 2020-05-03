package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.helper.RenderHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.RunningSwitchMessage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class UltimateAutoTableScreen extends ContainerScreen<UltimateAutoTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/ultimate_auto_table.png");

	public UltimateAutoTableScreen(UltimateAutoTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 254;
		this.ySize = 278;
	}

	@Override
	public void init() {
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		UltimateAutoTableContainer container = this.getContainer();

		super.init();
		this.addButton(new Button(x + 226, y + 113, 13, 16, "", button -> {
			NetworkHandler.INSTANCE.sendToServer(new RunningSwitchMessage(container.getPos()));
		}) {
			@Override
			public void render(int mouseX, int mouseY, float partialTicks) {

			}
		});
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

		if (mouseX > left + 226 && mouseX < left + 239 && mouseY > top + 113 && mouseY < top + 129) {
			this.renderTooltip(ModTooltips.TOGGLE_AUTO_CRAFTING.color(TextFormatting.WHITE).buildString(), mouseX, mouseY);
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
		RenderHelper.drawTexturedModalRect(x + 7, y + 137 - i1, 256, 78 - i1, 15, i1 + 1, 512, 512);

		if (container.isRunning()) {
			int i2 = container.getProgressBarScaled(16);
			RenderHelper.drawTexturedModalRect(x + 225, y + 113, 272, 0, 13, i2, 512, 512);
		} else {
			RenderHelper.drawTexturedModalRect(x + 226, y + 115, 272, 18, 13, 13, 512, 512);
		}
	}
}