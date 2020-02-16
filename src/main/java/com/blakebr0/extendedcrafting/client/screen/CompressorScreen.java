package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.EjectModeSwitchMessage;
import com.blakebr0.extendedcrafting.network.message.InputLimitSwitchMessage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class CompressorScreen extends ContainerScreen<CompressorContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/compressor.png");

	public CompressorScreen(CompressorContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 176;
		this.ySize = 194;
	}

	@Override
	public void init() {
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		CompressorContainer container = this.getContainer();

		super.init();
		this.addButton(new Button(x + 69, y + 29, 11, 9, "", button -> {
			NetworkHandler.INSTANCE.sendToServer(new EjectModeSwitchMessage(container.getPos()));
		}) {
			@Override
			public void render(int mouseX, int mouseY, float partialTicks) {

			}
		});
		this.addButton(new Button(x + 91, y + 74, 7, 10, "", button -> {
			NetworkHandler.INSTANCE.sendToServer(new InputLimitSwitchMessage(container.getPos()));
		}) {
			@Override
			public void render(int mouseX, int mouseY, float partialTicks) {

			}
		});
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = this.getTitle().getFormattedText();
		this.font.drawString(title, (float) (this.xSize / 2 - this.font.getStringWidth(title) / 2), 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getFormattedText();
		this.font.drawString(inventory, 8.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int left = this.guiLeft;
		int top = this.guiTop;
		CompressorContainer container = this.getContainer();

		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		if (mouseX > left + 7 && mouseX < left + 20 && mouseY > top + 17 && mouseY < top + 94) {
			this.renderTooltip(Utils.asList(container.getEnergyStored() + " FE"), mouseX, mouseY);
		}

		if (mouseX > left + 60 && mouseX < left + 85 && mouseY > top + 74 && mouseY < top + 83) {
			List<String> tooltip = new ArrayList<>();
			if (container.getMaterialCount() < 1) {
				tooltip.add(ModTooltips.EMPTY.color(TextFormatting.WHITE).buildString());
			} else {
				// TODO: find a solution for this
//				if (container.hasMaterialStack()) {
//					tooltip.add(this.tile.getMaterialStack().getDisplayName());
//				}
				tooltip.add(container.getMaterialCount() + " / " + container.getMaterialsRequired());
			}

			this.renderTooltip(tooltip, mouseX, mouseY);
		}

		if (mouseX > left + 68 && mouseX < left + 79 && mouseY > top + 28 && mouseY < top + 39) {
			if (container.isEjecting()) {
				this.renderTooltip(ModTooltips.EJECTING.color(TextFormatting.WHITE).buildString(), mouseX, mouseY);
			} else {
				this.renderTooltip(ModTooltips.EJECT.color(TextFormatting.WHITE).buildString(), mouseX, mouseY);
			}
		}

		if (mouseX > left + 90 && mouseX < left + 98 && mouseY > top + 73 && mouseY < top + 84) {
			if (container.isLimitingInput()) {
				this.renderTooltip(ModTooltips.LIMITED_INPUT.color(TextFormatting.WHITE).buildString(), mouseX, mouseY);
			} else {
				this.renderTooltip(ModTooltips.UNLIMITED_INPUT.color(TextFormatting.WHITE).buildString(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		int left = this.guiLeft;
		int top = this.guiTop;
		CompressorContainer container = this.getContainer();

		this.blit(x, y, 0, 0, this.xSize, this.ySize);

		int i1 = container.getEnergyBarScaled(78);
		this.blit(x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);

		if (container.hasRecipe()) {
			if (container.getMaterialCount() > 0 && container.getMaterialsRequired() > 0) {
				int i2 = container.getMaterialBarScaled(26);
				this.blit(x + 60, y + 74, 194, 19, i2 + 1, 10);
			}

			if (container.getProgress() > 0 && container.getEnergyRequired() > 0) {
				int i2 = container.getProgressBarScaled(24);
				this.blit(x + 96, y + 47, 194, 0, i2 + 1, 16);
			}
		}

		if (mouseX > left + 68 && mouseX < left + 79 && mouseY > top + 28 && mouseY < top + 39) {
			this.blit(x + 68, y + 30, 194, 32, 11, 9);
		}
		
		
		if (mouseX > left + 90 && mouseX < left + 98 && mouseY > top + 73 && mouseY < top + 84) {
			if (container.isLimitingInput()) {
				this.blit(x + 90, y + 74, 194, 56, 9, 10);
			} else {
				this.blit(x + 90, y + 74, 194, 43, 9, 10);
			}
		} else {
			if (container.isLimitingInput()) {
				this.blit(x + 90, y + 74, 203, 56, 9, 10);
			}
		}
	}
}
