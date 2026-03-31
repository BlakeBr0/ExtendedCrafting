package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.BasicTableContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class BasicTableScreen extends BaseContainerScreen<BasicTableContainer> {
	public static final Identifier BACKGROUND = ExtendedCrafting.resource("textures/gui/basic_table.png");

	public BasicTableScreen(BasicTableContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 170);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		gfx.text(this.font, this.title, 32, 6, 4210752, false);
		gfx.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
	}
}