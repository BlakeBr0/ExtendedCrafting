package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class EnderCrafterScreen extends BaseContainerScreen<EnderCrafterContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/ender_crafter.png");

	public EnderCrafterScreen(EnderCrafterContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title, BACKGROUND, 176, 170);
	}

	@Override
	protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.drawString(stack, title, 30.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getString();
		this.font.drawString(stack, inventory, 8.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		super.func_230450_a_(stack, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		EnderCrafterContainer container = this.getContainer();

		if (container.getProgress() > 0) {
			int i2 = container.getProgressBarScaled(24);
			this.blit(stack, x + 89, y + 36, 194, 0, i2 + 1, 16);
		}
	}
}