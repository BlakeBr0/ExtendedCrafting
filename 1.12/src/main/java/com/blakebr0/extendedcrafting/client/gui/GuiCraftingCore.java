package com.blakebr0.extendedcrafting.client.gui;

import java.util.List;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.container.ContainerCraftingCore;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.tile.TileCraftingCore;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCraftingCore extends GuiContainer {

	private static final ResourceLocation GUI = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/crafting_core.png");

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
		if (!stack.isEmpty()) {
			font = stack.getItem().getFontRenderer(stack);
		}
		if (font == null) {
			font = fontRenderer;
		}
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, altText);
		this.zLevel = 0.0F;
		this.itemRender.zLevel = 0.0F;
	}

	private void drawFakeItemStack(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY) {
		this.drawItemStack(stack, this.guiLeft + xOffset, this.guiTop + yOffset, (String) null);
	}

	private void drawFakeItemStackTooltip(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY) {
		if (mouseX > this.guiLeft + xOffset - 1 && mouseX < guiLeft + xOffset + 16 && mouseY > this.guiTop + yOffset - 1 && mouseY < this.guiTop + yOffset + 16) {
			if (!StackHelper.isNull(stack)) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(this.guiLeft + xOffset, this.guiTop + yOffset, this.guiLeft + xOffset + 16, this.guiTop + yOffset + 16, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
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
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		GlStateManager.pushMatrix();
		this.fontRenderer.drawString(Utils.localize("container.inventory"), 8, this.ySize - 94, 4210752);
		GlStateManager.scale(0.75F, 0.75F, 0.75F);
		this.fontRenderer.drawString(Utils.localize("ec.ccore.pedestals") + " " + tile.getPedestalCount(), 36, 23, -1);
		CombinationRecipe recipe = this.tile.getRecipe();
		if (recipe == null) {
			this.fontRenderer.drawString(Utils.localize("ec.ccore.no_recipe"), 36, 43, -1);
		} else {
			this.fontRenderer.drawString(Utils.localize("ec.ccore.rf_cost") + " " + Utils.format(recipe.getCost()) + " RF", 36, 43, -1);
			this.fontRenderer.drawString(Utils.localize("ec.ccore.rf_rate") + " " + Utils.format(recipe.getPerTick()) + " RF/t", 36, 53, -1);
			this.fontRenderer.drawString(Utils.localize("ec.ccore.progress") + " " + this.tile.getProgress() / recipe.getCost() + "%", 36, 63, -1);
			if (this.tile.getEnergy().getEnergyStored() < recipe.getPerTick()) {
				this.fontRenderer.drawString(Utils.localize("ec.ccore.no_power"), 36, 83, -1);
			}
		}
		GlStateManager.popMatrix();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int left = this.guiLeft;
		int top = this.guiTop;

		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		
		if (this.tile.getRecipe() != null) {
			ItemStack output = this.tile.getRecipe().getOutput();
			this.drawFakeItemStack(output, 148, 37, mouseX, mouseY);
			this.drawFakeItemStackTooltip(output, 148, 37, mouseX, mouseY);
		}

		if (mouseX > left + 7 && mouseX < guiLeft + 20 && mouseY > this.guiTop + 7 && mouseY < this.guiTop + 84) {
			this.drawHoveringText(Utils.asList(Utils.format(this.tile.getEnergy().getEnergyStored()) + " RF"), mouseX, mouseY);
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
