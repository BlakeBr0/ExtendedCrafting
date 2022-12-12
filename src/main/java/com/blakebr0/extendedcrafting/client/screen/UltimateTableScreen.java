package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.UltimateTableContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class UltimateTableScreen extends BaseContainerScreen<UltimateTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/screen/ultimate_table.png");

	public UltimateTableScreen(UltimateTableContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 234, 278, 512, 512);
	}
	
	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		this.font.draw(stack, title, 8.0F, 6.0F, 4210752);
		this.font.draw(stack, this.playerInventoryTitle, 39.0F, this.imageHeight - 94.0F, 4210752);
	}

	@Override
	protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
		this.renderDefaultBg(matrix, partialTicks, mouseX, mouseY);
	}
}