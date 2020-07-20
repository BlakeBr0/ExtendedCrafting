package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.BasicTableContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BasicTableScreen extends BaseContainerScreen<BasicTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/basic_table.png");

	public BasicTableScreen(BasicTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title, BACKGROUND, 176, 170);
	}

	@Override
	protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.drawString(stack, title, 32.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getString();
		this.font.drawString(stack, inventory, 8.0F, this.ySize - 94.0F, 4210752);
	}
}