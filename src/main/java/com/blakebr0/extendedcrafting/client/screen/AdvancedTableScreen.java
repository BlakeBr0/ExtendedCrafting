package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.AdvancedTableContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AdvancedTableScreen extends BaseContainerScreen<AdvancedTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/advanced_table.png");

	public AdvancedTableScreen(AdvancedTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title, BACKGROUND, 176, 206);
	}
	
	@Override
	protected void func_230459_a_(MatrixStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.drawString(stack, title, 14.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getString();
		this.font.drawString(stack, inventory, 8.0F, this.ySize - 94.0F, 4210752);
	}
}