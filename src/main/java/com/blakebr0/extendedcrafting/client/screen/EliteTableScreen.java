package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class EliteTableScreen extends ContainerScreen<EliteTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/elite_table.png");

	public EliteTableScreen(EliteTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 200;
		this.ySize = 242;
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		this.func_230459_a_(stack, mouseX, mouseY);
	}
	
	@Override
	protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.drawString(stack, title, 8.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getString();
		this.font.drawString(stack, inventory, 20.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.blit(stack, x, y, 0, 0, this.xSize, this.ySize);
	}
}