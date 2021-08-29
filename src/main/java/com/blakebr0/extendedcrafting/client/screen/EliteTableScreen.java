package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EliteTableScreen extends BaseContainerScreen<EliteTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/elite_table.png");

	public EliteTableScreen(EliteTableContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 200, 242);
	}
	
	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		this.font.draw(stack, title, 8.0F, 6.0F, 4210752);
		this.font.draw(stack, this.playerInventoryTitle, 20.0F, this.imageHeight - 94.0F, 4210752);
	}
}