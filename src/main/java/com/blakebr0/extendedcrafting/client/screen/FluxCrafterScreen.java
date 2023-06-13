package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.FluxCrafterContainer;
import com.blakebr0.extendedcrafting.tileentity.FluxCrafterTileEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FluxCrafterScreen extends BaseContainerScreen<FluxCrafterContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/flux_crafter.png");
	private FluxCrafterTileEntity tile;

	public FluxCrafterScreen(FluxCrafterContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 170);
	}

	@Override
	protected void init() {
		super.init();

		this.tile = this.getTileEntity();
	}

	@Override
	protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		gfx.drawString(this.font, title, 30, 6, 4210752, false);
		gfx.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
	}

	@Override
	protected void renderBg(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
		super.renderDefaultBg(gfx, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.getProgress() > 0) {
			int i2 = this.getProgressBarScaled();
			gfx.blit(BACKGROUND, x + 89, y + 36, 194, 0, i2 + 1, 16);
		}
	}

	private FluxCrafterTileEntity getTileEntity() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var tile = level.getBlockEntity(this.getMenu().getBlockPos());

			if (tile instanceof FluxCrafterTileEntity table)
				return table;
		}

		return null;
	}

	private int getProgress() {
		if (this.tile == null)
			return 0;

		return this.tile.getProgress();
	}

	private int getProgressRequired() {
		if (this.tile == null)
			return 0;

		return this.tile.getProgressRequired();
	}

	private int getProgressBarScaled() {
		int i = this.getProgress();
		int j = Math.max(this.getProgressRequired(), i);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}
}