package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CraftingCoreScreen extends ContainerScreen<CraftingCoreContainer> {

	private static final ResourceLocation GUI = ResourceHelper.getResource(ExtendedCrafting.MOD_ID, "textures/gui/crafting_core.png");

	private CraftingCoreTileEntity tile;

	public CraftingCoreScreen(CraftingCoreContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.tile = tile;
		this.xSize = 176;
		this.ySize = 194;
	}

	private int getEnergyBarScaled(int pixels) {
		int i = this.tile.getEnergy().getEnergyStored();
		int j = this.tile.getEnergy().getMaxEnergyStored();
		return (int) (j != 0 && i != 0 ? i * (long) pixels / j : 0);
	}

	private int getProgressBarScaled(int pixels) {
		int i = this.tile.getProgress();
		long j = this.tile.getRecipe().getPowerCost();
		return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = Utils.localize("container.ec.crafting_core");
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(Utils.localize("container.inventory"), 8, this.ySize - 94, 4210752);
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.75F, 0.75F, 0.75F);
		this.fontRenderer.drawString(Utils.localize("ec.ccore.pedestals", this.tile.getPedestalCount()), 36, 36, -1);
		
		CombinationRecipe recipe = this.tile.getRecipe();
		if (recipe == null) {
			this.fontRenderer.drawString(Utils.localize("ec.ccore.no_recipe"), 36, 56, -1);
		} else {
			this.fontRenderer.drawString(Utils.localize("ec.ccore.rf_cost", Utils.format(recipe.getPowerCost())) + " FE", 36, 56, -1);
			this.fontRenderer.drawString(Utils.localize("ec.ccore.rf_rate", Utils.format(recipe.getPowerRate())) + " FE/t", 36, 66, -1);
			if (this.tile.getEnergy().getEnergyStored() < recipe.getPowerRate()) {
				this.fontRenderer.drawString(Utils.localize("ec.ccore.no_power"), 36, 86, -1);
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
			this.drawFakeItemStack(output, 148, 47, mouseX, mouseY);
			this.drawFakeItemStackTooltip(output, 148, 47, mouseX, mouseY);
		}

		if (mouseX > left + 7 && mouseX < this.guiLeft + 20 && mouseY > this.guiTop + 17 && mouseY < this.guiTop + 94) {
			this.drawHoveringText(Utils.format(this.tile.getEnergy().getEnergyStored()) + " FE", mouseX, mouseY);
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
		this.drawTexturedModalRect(x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);

		CombinationRecipe recipe = this.tile.getRecipe();
		if (this.tile != null && recipe != null) {
			if (this.tile.getProgress() > 0 && recipe.getPowerCost() > 0) {
				int i2 = getProgressBarScaled(24);
				this.drawTexturedModalRect(x + 116, y + 47, 194, 0, i2 + 1, 16);
			}
		}
	}
	
	private void drawItemStack(ItemStack stack, int x, int y, String altText) {
    	GlStateManager.pushMatrix();
    	net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.translate(0.0F, 0.0F, -32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = this.fontRenderer;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
	}

	private void drawFakeItemStack(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY) {
		this.drawItemStack(stack, this.guiLeft + xOffset, this.guiTop + yOffset, null);
	}

	private void drawFakeItemStackTooltip(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY) {
		if (mouseX > this.guiLeft + xOffset - 1 && mouseX < guiLeft + xOffset + 16 && mouseY > this.guiTop + yOffset - 1 && mouseY < this.guiTop + yOffset + 16) {
			if (!stack.isEmpty()) {
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
}
