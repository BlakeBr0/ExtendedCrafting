package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.RunningSwitchMessage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class EliteAutoTableScreen extends ContainerScreen<EliteAutoTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/elite_auto_table.png");

	public EliteAutoTableScreen(EliteAutoTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 220;
		this.ySize = 242;
	}

	@Override
	public void init() {
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		EliteAutoTableContainer container = this.getContainer();

		super.init();
		this.addButton(new Button(x + 192, y + 95, 13, 16, "", button -> {
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
		EliteAutoTableContainer container = this.getContainer();

		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		if (mouseX > left + 7 && mouseX < left + 20 && mouseY > top + 41 && mouseY < top + 118) {
			this.renderTooltip(container.getEnergyStored() + " FE", mouseX, mouseY);
		}

		if (mouseX > left + 192 && mouseX < left + 205 && mouseY > top + 95 && mouseY < top + 111) {
			this.renderTooltip(ModTooltips.TOGGLE_AUTO_CRAFTING.color(TextFormatting.WHITE).buildString(), mouseX, mouseY);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = this.getTitle().getFormattedText();
		this.font.drawString(title, 26.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getFormattedText();
		this.font.drawString(inventory, 30.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		EliteAutoTableContainer container = this.getContainer();

		this.blit(x, y, 0, 0, this.xSize, this.ySize);

		int i1 = container.getEnergyBarScaled(78);
		this.blit(x + 7, y + 119 - i1, 222, 78 - i1, 15, i1 + 1);

		if (container.isRunning()) {
			int i2 = container.getProgressBarScaled(16);
			this.blit(x + 191, y + 95, 238, 0, 13, i2);
		} else {
			this.blit(x + 192, y + 97, 238, 18, 13, 13);
		}
	}
}