package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.AdvancedTableContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedTableScreen extends BaseContainerScreen<AdvancedTableContainer> {
	public static final Identifier BACKGROUND = ExtendedCrafting.resource("textures/gui/advanced_table.png");

	public AdvancedTableScreen(AdvancedTableContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 206);
	}
	
	@Override
	protected void extractLabels(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		gfx.text(this.font, this.title, 14, 6, -12566464, false);
		gfx.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, -12566464, false);
	}
}