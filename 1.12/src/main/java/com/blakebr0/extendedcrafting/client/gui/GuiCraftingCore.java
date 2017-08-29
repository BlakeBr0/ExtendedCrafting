package com.blakebr0.extendedcrafting.client.gui;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.client.container.ContainerCraftingCore;
import com.blakebr0.extendedcrafting.tile.TileCraftingCore;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiCraftingCore extends GuiContainer {

	private static final ResourceLocation GUI = new ResourceLocation(ExtendedCrafting.MOD_ID,
			"textures/gui/crafting_core.png");

	private TileCraftingCore tile;

	public GuiCraftingCore(TileCraftingCore tile, ContainerCraftingCore container) {
		super(container);
		this.tile = tile;
		this.xSize = 176;
		this.ySize = 184;
	}

	private int getEnergyBarScaled(int pixels) {
		int i = this.tile.getEnergy().getEnergyStored();
		int j = this.tile.getEnergy().getMaxEnergyStored();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	private int getProgressBarScaled(int pixels) {
		int i = this.tile.getProgress();
		int j = this.tile.getRecipe().getCost();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	private void drawItemStack(ItemStack stack, int x, int y, String altText) {
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		this.zLevel = 50.0F;
		this.itemRender.zLevel = 50.0F;
		FontRenderer font = null;
		if (!stack.isEmpty())
			font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = fontRenderer;
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, altText);
		this.zLevel = 0.0F;
		this.itemRender.zLevel = 0.0F;
	}

	private void drawFakeItemStack(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY) {
		this.drawItemStack(stack, this.guiLeft + xOffset, this.guiTop + yOffset, (String) null);
	}

	private void drawFakeItemStackTooltip(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY) {
		if (mouseX > this.guiLeft + xOffset - 1 && mouseX < guiLeft + xOffset + 16 && mouseY > this.guiTop + yOffset - 1
				&& mouseY < this.guiTop + yOffset + 16) {
			if (!StackHelper.isNull(stack)) {
				// this.drawRect(this.guiLeft + xOffset, this.guiTop + yOffset,
				// this.guiLeft + xOffset + 16, this.guiTop + yOffset + 16,
				// 2130706433);
				this.renderToolTip(stack, mouseX, mouseY);
			}
		}
	}

	private ItemStack getStack(Object obj) {
		if (obj instanceof ItemStack) {
			return ((ItemStack) obj).copy();
		} else if (obj instanceof List) {
			return ((List<ItemStack>) obj).get(0);
		} else {
			return ItemStack.EMPTY;
		}
	}

	private ItemStack getPedestalStackFromIndex(int index) {
		List<Object> list = this.tile.getRecipe().getPedestalItems();
		if (index < list.size()) {
			return getStack(list.get(index));
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, this.ySize - 94, 4210752);
		this.fontRenderer.drawString("x " + tile.getPedestalCount(), 40, 4, 4210752);
		this.fontRenderer.drawString("Power Cost: ", 40, 20, 4210752);
		this.fontRenderer.drawString("Power Rate: ", 40, 32, 4210752);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int left = this.guiLeft;
		int top = this.guiTop;

		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		this.drawFakeItemStack(StackHelper.to(ModBlocks.blockPedestal), 22, 4, mouseX, mouseY);

		if (tile.getRecipe() != null) {
			ItemStack output = this.tile.getRecipe().getOutput();
			this.drawFakeItemStack(output, 148, 37, mouseX, mouseY);
			this.drawFakeItemStackTooltip(output, 148, 37, mouseX, mouseY);
		}

		if (mouseX > left + 7 && mouseX < guiLeft + 20 && mouseY > this.guiTop + 7 && mouseY < this.guiTop + 84) {
			this.drawHoveringText(
					Collections.singletonList(
							NumberFormat.getInstance().format(this.tile.getEnergy().getEnergyStored()) + " RF"),
					mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GUI);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		int i1 = this.getEnergyBarScaled(78);
		this.drawTexturedModalRect(x + 7, y + 85 - i1, 178, 78 - i1, 15, i1 + 1);

		if (this.tile != null && this.tile.getRecipe() != null) {
			if (this.tile.getProgress() > 0 && this.tile.getRecipe().getCost() > 0) {
				int i2 = getProgressBarScaled(24);
				this.drawTexturedModalRect(x + 116, y + 37, 194, 0, i2 + 1, 16);
			}
		}
	}
}
