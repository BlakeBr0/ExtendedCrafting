package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.client.screen.widget.EnergyBarWidget;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.FluxAlternatorContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FluxAlternatorScreen extends BaseContainerScreen<FluxAlternatorContainer> {
	public static final Identifier BACKGROUND = ExtendedCrafting.resource("textures/gui/flux_alternator.png");

	public FluxAlternatorScreen(FluxAlternatorContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 194);
	}

	@Override
	protected void init() {
		super.init();
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 17, this.menu::getEnergyStored, this.menu::getMaxEnergyCapacity));
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		gfx.text(this.font, this.title, (this.imageWidth / 2 - this.font.width(title) / 2), 6, -12566464, false);
		gfx.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, -12566464, false);
	}
}