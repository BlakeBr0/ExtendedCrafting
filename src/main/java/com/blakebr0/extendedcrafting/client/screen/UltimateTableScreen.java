package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.UltimateTableContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class UltimateTableScreen extends BaseContainerScreen<UltimateTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/ultimate_table.png");

	public UltimateTableScreen(UltimateTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title, BACKGROUND, 234, 278, 512, 512);
	}
	
	@Override
	protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.draw(stack, title, 8.0F, 6.0F, 4210752);
		String inventory = this.inventory.getDisplayName().getString();
		this.font.draw(stack, inventory, 39.0F, this.imageHeight - 94.0F, 4210752);
	}
}