package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.client.screen.widget.EnergyBarWidget;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.FluxAlternatorContainer;
import com.blakebr0.extendedcrafting.tileentity.FluxAlternatorTileEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FluxAlternatorScreen extends BaseContainerScreen<FluxAlternatorContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/flux_alternator.png");

	public FluxAlternatorScreen(FluxAlternatorContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 194);
	}

	@Override
	protected void init() {
		super.init();
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		var tile = this.getTileEntity();

		if (tile != null) {
			this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 17, tile.getEnergy()));
		}
	}

	@Override
	protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		gfx.drawString(this.font, title, (this.imageWidth / 2 - this.font.width(title) / 2), 6, 4210752, false);
		gfx.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
	}

	@Override
	protected void renderBg(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
		this.renderDefaultBg(gfx, partialTicks, mouseX, mouseY);
	}

	private FluxAlternatorTileEntity getTileEntity() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var tile = level.getBlockEntity(this.getMenu().getBlockPos());

			if (tile instanceof FluxAlternatorTileEntity alternator)
				return alternator;
		}

		return null;
	}
}