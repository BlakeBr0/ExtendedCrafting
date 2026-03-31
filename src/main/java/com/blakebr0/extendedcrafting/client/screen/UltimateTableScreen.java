package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.UltimateTableContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class UltimateTableScreen extends BaseContainerScreen<UltimateTableContainer> {
	public static final Identifier BACKGROUND = ExtendedCrafting.resource("textures/gui/ultimate_table.png");

	public UltimateTableScreen(UltimateTableContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 234, 278, 512, 512);
	}
	
	@Override
	protected void extractLabels(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		gfx.text(this.font, this.title, 8, 6, 4210752, false);
		gfx.text(this.font, this.playerInventoryTitle, 39, this.imageHeight - 94, 4210752, false);
	}
}