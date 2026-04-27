package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class EliteTableScreen extends BaseContainerScreen<EliteTableContainer> {
	public static final Identifier BACKGROUND = ExtendedCrafting.resource("textures/gui/elite_table.png");

	public EliteTableScreen(EliteTableContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 200, 242);
	}
	
	@Override
	protected void extractLabels(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		gfx.text(this.font, this.title, 8, 6, -12566464, false);
		gfx.text(this.font, this.playerInventoryTitle, 20, this.imageHeight - 94, -12566464, false);
	}
}