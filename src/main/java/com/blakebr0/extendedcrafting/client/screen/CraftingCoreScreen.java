package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CraftingCoreScreen extends ContainerScreen<CraftingCoreContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/crafting_core.png");

	public CraftingCoreScreen(CraftingCoreContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 176;
		this.ySize = 194;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = this.getTitle().getFormattedText();
		this.font.drawString(title, (float) (this.xSize / 2 - this.font.getStringWidth(title) / 2), 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getFormattedText();
		this.font.drawString(inventory, 8.0F, this.ySize - 94.0F, 4210752);

		CraftingCoreContainer container = this.getContainer();
		RenderSystem.pushMatrix();
		RenderSystem.scalef(0.75F, 0.75F, 0.75F);
		this.font.drawString(this.text("screen.extendedcrafting.crafting_core.pedestals", container.getPedestalCount()), 36, 36, -1);

		if (!container.hasRecipe()) {
			this.font.drawString(this.text("screen.extendedcrafting.crafting_core.no_recipe"), 36, 56, -1);
		} else {
			this.font.drawString(this.text("screen.extendedcrafting.crafting_core.power_cost", container.getEnergyRequired()) + " FE", 36, 56, -1);
			this.font.drawString(this.text("screen.extendedcrafting.crafting_core.power_rate", container.getEnergyRate()) + " FE/t", 36, 66, -1);
			if (container.getEnergyStored() < container.getEnergyRate()) {
				this.font.drawString(this.text("screen.extendedcrafting.crafting_core.no_power"), 36, 86, -1);
			}
		}
		
		RenderSystem.popMatrix();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int left = this.guiLeft;
		int top = this.guiTop;
		CraftingCoreContainer container = this.getContainer();

		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		
//		if (container.hasRecipe()) {
//			ItemStack output = this.tile.getActiveRecipe().getOutput();
//			this.drawFakeItemStack(output, 148, 47, mouseX, mouseY);
//			this.drawFakeItemStackTooltip(output, 148, 47, mouseX, mouseY);
//		}

		if (mouseX > left + 7 && mouseX < left + 20 && mouseY > top + 17 && mouseY < top + 94) {
			this.renderTooltip(container.getEnergyStored() + " FE", mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		CraftingCoreContainer container = this.getContainer();

		this.blit(x, y, 0, 0, this.xSize, this.ySize);

		int i1 = container.getEnergyBarScaled(78);
		this.blit(x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);

		if (container.hasRecipe()) {
			if (container.getProgress() > 0 && container.getEnergyRate() > 0) {
				int i2 = container.getProgressBarScaled(24);
				this.blit(x + 116, y + 47, 194, 0, i2 + 1, 16);
			}
		}
	}
	
	private void drawItemStack(ItemStack stack, int x, int y, String altText) {
//    	GlStateManager.pushMatrix();
//    	net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
//        GlStateManager.translate(0.0F, 0.0F, -32.0F);
//        this.zLevel = 200.0F;
//        this.itemRender.zLevel = 200.0F;
//        FontRenderer font = stack.getItem().getFontRenderer(stack);
//        if (font == null) font = this.fontRenderer;
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
//        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, altText);
//        this.zLevel = 0.0F;
//        this.itemRender.zLevel = 0.0F;
//        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
//        GlStateManager.popMatrix();
	}

	private void drawFakeItemStack(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY) {
		this.drawItemStack(stack, this.guiLeft + xOffset, this.guiTop + yOffset, null);
	}

	private void drawFakeItemStackTooltip(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY) {
//		if (mouseX > this.guiLeft + xOffset - 1 && mouseX < guiLeft + xOffset + 16 && mouseY > this.guiTop + yOffset - 1 && mouseY < this.guiTop + yOffset + 16) {
//			if (!stack.isEmpty()) {
//                GlStateManager.disableLighting();
//                GlStateManager.disableDepth();
//                GlStateManager.colorMask(true, true, true, false);
//                this.drawGradientRect(this.guiLeft + xOffset, this.guiTop + yOffset, this.guiLeft + xOffset + 16, this.guiTop + yOffset + 16, -2130706433, -2130706433);
//                GlStateManager.colorMask(true, true, true, true);
//                GlStateManager.enableLighting();
//                GlStateManager.enableDepth();
//				this.renderToolTip(stack, mouseX, mouseY);
//			}
//		}
	}

	private String text(String key, Object... args) {
		return Localizable.of(key).args(args).buildString();
	}
}
