package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class EnderCrafterScreen extends BaseContainerScreen<EnderCrafterContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/ender_crafter.png");
	private EnderCrafterTileEntity tile;

	public EnderCrafterScreen(EnderCrafterContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title, BACKGROUND, 176, 170);
	}

	@Override
	protected void init() {
		super.init();

		this.tile = this.getTileEntity();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.drawString(stack, title, 30.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getString();
		this.font.drawString(stack, inventory, 8.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(stack, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.getProgress() > 0) {
			int i2 = this.getProgressBarScaled();
			this.blit(stack, x + 89, y + 36, 194, 0, i2 + 1, 16);
		}
	}

	private EnderCrafterTileEntity getTileEntity() {
		ClientWorld world = this.getMinecraft().world;

		if (world != null) {
			TileEntity tile = world.getTileEntity(this.getContainer().getPos());

			if (tile instanceof EnderCrafterTileEntity) {
				return (EnderCrafterTileEntity) tile;
			}
		}

		return null;
	}

	private int getProgress() {
		if (this.tile == null)
			return 0;

		return this.tile.getProgress();
	}

	private int getProgressRequired() {
		if (this.tile == null)
			return 0;

		return this.tile.getProgressRequired();
	}

	private int getProgressBarScaled() {
		int i = this.getProgress();
		int j = Math.max(this.getProgressRequired(), i);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}
}