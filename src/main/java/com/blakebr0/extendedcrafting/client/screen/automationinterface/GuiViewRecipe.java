package com.blakebr0.extendedcrafting.client.screen.automationinterface;

import java.io.IOException;

import com.blakebr0.cucumber.gui.button.GuiButtonArrow;
import com.blakebr0.cucumber.helper.RenderHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.container.automationinterface.ContainerViewRecipe;
import com.blakebr0.extendedcrafting.client.screen.GuiAdvancedTable;
import com.blakebr0.extendedcrafting.client.screen.GuiBasicTable;
import com.blakebr0.extendedcrafting.client.screen.GuiEliteTable;
import com.blakebr0.extendedcrafting.client.screen.GuiUltimateTable;
import com.blakebr0.extendedcrafting.lib.ViewRecipeInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

public class GuiViewRecipe extends GuiContainer {
	
	private GuiAutomationInterface parent;
	public ViewRecipeInfo info;
	public ResourceLocation grid;
	public GuiButton back;
		
	public GuiViewRecipe(GuiAutomationInterface parent, ViewRecipeInfo info, int width) {
		super(new ContainerViewRecipe(parent.player, parent.tile, info));
		this.parent = parent;
		this.info = info;
		this.xSize = info.width;
		this.ySize = info.height;
		
		switch (width) {
		case 3:
			this.grid = GuiBasicTable.GUI; break;
		case 5:
			this.grid = GuiAdvancedTable.GUI; break;
		case 7:
			this.grid = GuiEliteTable.GUI; break;
		case 9:
			this.grid = GuiUltimateTable.GUI; break;
		default:
			this.grid = GuiBasicTable.GUI; break;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		
		if (this.parent.tile.hasRecipe()) {
			ItemStackHandler recipe = this.parent.tile.getRecipe();
			int s = (int) Math.sqrt(recipe.getSlots());
			ItemStack hovered = ItemStack.EMPTY;
			for (int i = 0; i < s; i++) {
				for (int j = 0; j < s; j++) {
					int b = i * s + j;
					ItemStack stack = recipe.getStackInSlot(b);
					
					int x1 = x + 13 + (j * 18) + this.info.gridStartX;
					int y1 = y + 22 + (i * 18) + this.info.gridStartY;
					this.drawItemStack(stack, x1, y1, "");
					
					int xOffset = x1 - x;
					int yOffset = y1 - y;
					if (this.isMouseOver(xOffset, yOffset, mouseX, mouseY)) {
						hovered = stack;
						this.drawHoverSquare(xOffset, yOffset);
					}
				}
			}
			
			ItemStack result = this.parent.tile.getResult();
			this.drawItemStack(result, x + this.info.outputX, y + this.info.outputY, null);
			
			this.drawFakeItemStackTooltip(hovered, mouseX, mouseY);
			
			if (this.isMouseOver(this.info.outputX, this.info.outputY, mouseX, mouseY)) {
				this.drawHoverSquare(this.info.outputX, this.info.outputY);
				this.drawFakeItemStackTooltip(result, mouseX, mouseY);
			}
		}
		
	}	
	
	@Override
	public void initGui() {
		super.initGui();
		
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		
		this.back = this.addButton(new GuiButtonArrow(0, x + this.info.width - 35, y + 6));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.back) {
			Minecraft.getMinecraft().displayGuiScreen(this.parent);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = Utils.localize("ec.interface.view");
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(Utils.localize("container.inventory"), 8 + this.info.invOffsetX, this.ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.grid);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		
		if (this.grid == GuiUltimateTable.GUI) {
			RenderHelper.drawTexturedModelRect(x, y, 0, 0, this.xSize, this.ySize, 512, 512);
		} else {
			this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		}
	}
	
	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY) {
		super.renderHoveredToolTip(mouseX, mouseY);
		
		if (this.back.isMouseOver()) {
			this.drawHoveringText(this.back.displayString, mouseX, mouseY);
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
    
	private void drawFakeItemStackTooltip(ItemStack stack, int mouseX, int mouseY) {
		if (!stack.isEmpty()) {
			this.renderToolTip(stack, mouseX, mouseY);
		}
	}
	
	private void drawHoverSquare(int xOffset, int yOffset) {
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.colorMask(true, true, true, false);
		this.drawGradientRect(this.guiLeft + xOffset, this.guiTop + yOffset, this.guiLeft + xOffset + 16, this.guiTop + yOffset + 16, -2130706433, -2130706433);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}
	
	private boolean isMouseOver(int xOffset, int yOffset, int mouseX, int mouseY) {
		return mouseX > this.guiLeft + xOffset - 1 && mouseX < this.guiLeft + xOffset + 16 && mouseY > this.guiTop + yOffset - 1 && mouseY < this.guiTop + yOffset + 16;
	}
}
