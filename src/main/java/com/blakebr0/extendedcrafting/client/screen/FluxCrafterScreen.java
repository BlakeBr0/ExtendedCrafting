package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.FluxCrafterContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FluxCrafterScreen extends BaseContainerScreen<FluxCrafterContainer> {
	private static final Identifier BACKGROUND = ExtendedCrafting.resource("textures/gui/flux_crafter.png");

	public FluxCrafterScreen(FluxCrafterContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 170);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		gfx.text(this.font, this.title, 30, 6, 4210752, false);
		gfx.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float a) {
		super.extractBackground(gfx, mouseX, mouseY, a);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.menu.getProgress() > 0) {
			int i2 = this.getProgressBarScaled();
			gfx.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x + 89, y + 36, 194, 0, i2 + 1, 16, 256, 256);
		}
	}

	private int getProgressBarScaled() {
		int i = this.menu.getProgress();
		int j = Math.max(this.menu.getProgressRequired(), i);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}
}