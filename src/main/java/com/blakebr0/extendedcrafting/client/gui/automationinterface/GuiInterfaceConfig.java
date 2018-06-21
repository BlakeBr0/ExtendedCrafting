package com.blakebr0.extendedcrafting.client.gui.automationinterface;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.util.InterfaceAutoChangePacket;
import com.blakebr0.extendedcrafting.util.NetworkThingy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.ItemStackHandler;

public class GuiInterfaceConfig extends GuiContainer {

	private GuiAutomationInterface parent;
	
	public GuiInterfaceConfig(GuiAutomationInterface parent) {
		super(parent.inventorySlots);
		this.parent = parent;
		this.xSize = parent.getXSize();
		this.ySize = parent.getYSize();
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.buttonList.get(0).displayString = this.parent.tile.getInserterFaceName();
		this.buttonList.get(1).displayString = this.parent.tile.getExtractorFaceName();
		this.buttonList.get(2).displayString = this.parent.tile.getAutoEject() ? Utils.localize("ec.interface.on") : Utils.localize("ec.interface.off");
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		
		if (mouseX > this.guiLeft + 7 && mouseX < this.guiLeft + 20 && mouseY > this.guiTop + 16 && mouseY < this.guiTop + 93) {
			this.drawHoveringText(Utils.format(parent.tile.getEnergy().getEnergyStored()) + " FE", mouseX, mouseY);
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new SmallButton(0, (this.width - this.xSize) / 2 + 62, (this.height - this.ySize) / 2 + 30, 70, 12, this.parent.tile.getInserterFaceName()));
		this.buttonList.add(new SmallButton(1, (this.width - this.xSize) / 2 + 62, (this.height - this.ySize) / 2 + 55, 70, 12, this.parent.tile.getExtractorFaceName()));
		this.buttonList.add(new SmallButton(2, (this.width - this.xSize) / 2 + 62, (this.height - this.ySize) / 2 + 79, 70, 12, this.parent.tile.getAutoEject() ? Utils.localize("ec.interface.on") : Utils.localize("ec.interface.off")));
		this.buttonList.add(new SmallButton(10, (this.width - this.xSize) / 2 + 129, (this.height - this.ySize) / 2 + 96, 40, 12, Utils.localize("ec.interface.back")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 10) {
			Minecraft.getMinecraft().displayGuiScreen(parent);
		} else if (button.id == 0) {
			NetworkThingy.THINGY.sendToServer(new InterfaceAutoChangePacket(this.parent.tile.getPos().toLong(), 0));
		} else if (button.id == 1) {
			NetworkThingy.THINGY.sendToServer(new InterfaceAutoChangePacket(this.parent.tile.getPos().toLong(), 1));
		} else if (button.id == 2) {
			NetworkThingy.THINGY.sendToServer(new InterfaceAutoChangePacket(this.parent.tile.getPos().toLong(), 2));
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String s = Utils.localize("container.ec.interface");
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(Utils.localize("container.inventory"), 8, this.ySize - 94, 4210752);
		this.fontRenderer.drawString(Utils.localize("ec.interface.auto_insert"), this.xSize / 2 - 20, 21, -1);
		this.fontRenderer.drawString(Utils.localize("ec.interface.auto_extract"), this.xSize / 2 - 23, 45, -1);
		this.fontRenderer.drawString(Utils.localize("ec.interface.auto_eject"), this.xSize / 2 - 18, 70, -1);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(parent.GUI);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		
		int i1 = parent.getEnergyBarScaled(78);
		this.drawTexturedModalRect(x + 7, y + 94 - i1, 178, 78 - i1, 15, i1 + 1);
	}
		
    public class SmallButton extends GuiButton {

		public SmallButton(int id, int x, int y, int width, int height, String text) {
			super(id, x, y, width, height, text);
		}
    	
		@Override
	    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
	        if (this.visible) {
	            FontRenderer fontrenderer = mc.fontRenderer;
	            mc.getTextureManager().bindTexture(GuiAutomationInterface.GUI);
	            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	            int i = this.getHoverState(this.hovered);
	            GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	            this.drawTexturedModalRect(this.x, this.y, 0, 194 + i * 12, this.width / 2, this.height);
	            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 194 + i * 12, this.width / 2, this.height);
	            this.mouseDragged(mc, mouseX, mouseY);
	            int j = 14737632;

	            if (this.packedFGColour != 0) {
	                j = packedFGColour;
	            } else if (!this.enabled) {
	                j = 10526880;
	            } else if (this.hovered) {
	                j = 16777120;
	            }

	            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, (this.y + (this.height - 9) / 2) + 1, j);
	        }
	    }
    }
}
