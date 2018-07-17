package com.blakebr0.extendedcrafting.client.gui.automationinterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.blakebr0.cucumber.gui.button.GuiButtonArrow;
import com.blakebr0.cucumber.gui.button.IconButton;
import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import com.blakebr0.extendedcrafting.util.InterfaceAutoChangePacket;
import com.blakebr0.extendedcrafting.util.NetworkThingy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;

public class GuiInterfaceConfig extends GuiContainer {

	private GuiAutomationInterface parent;
	private TileAutomationInterface tile;
	public DirButton insert, extract;
	public CheckboxButton eject, smart;
	public GuiButton back;
	
	public GuiInterfaceConfig(GuiAutomationInterface parent) {
		super(parent.inventorySlots);
		this.parent = parent;
		this.tile = parent.tile;
		this.xSize = parent.getXSize();
		this.ySize = parent.getYSize();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		
		if (mouseX > this.guiLeft + 7 && mouseX < this.guiLeft + 20 && mouseY > this.guiTop + 16 && mouseY < this.guiTop + 93) {
			this.drawHoveringText(Utils.format(this.tile.getEnergy().getEnergyStored()) + " FE", mouseX, mouseY);
		}
		
		if (this.back.isMouseOver()) {
			this.drawHoveringText(this.back.displayString, mouseX, mouseY);
		}
		
		for (GuiButton button : this.buttonList) {
			if (button.isMouseOver()) {
				if (button instanceof DirButton) {
					this.drawHoveringText(((DirButton) button).tooltip, mouseX, mouseY);
				} else if (button instanceof CheckboxButton) {
					this.drawHoveringText(((CheckboxButton) button).tooltip, mouseX, mouseY);
				}
			}
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		
		this.insert = this.addButton(new DirButton(0, x + 63, y + 20, Utils.localize("ec.interface.auto_insert")));
		this.extract = this.addButton(new DirButton(1, x + 63, y + 33, Utils.localize("ec.interface.auto_extract")));
		this.eject = this.addButton(new CheckboxButton(2, x + 63, y + 46, Utils.localize("ec.interface.auto_eject")));
		this.smart = this.addButton(new CheckboxButton(3, x + 63, y + 59, Utils.localize("ec.interface.smart_insert")));
		
		this.back = this.addButton(new IconButton(10, x + 82, y + 74, 29, 15, 71, 210, Utils.localize("ec.interface.back"), this.parent.GUI));
		
		this.updateButtons();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.back) {
			Minecraft.getMinecraft().displayGuiScreen(this.parent);
		} else if (button == this.insert) {
			NetworkThingy.THINGY.sendToServer(new InterfaceAutoChangePacket(this.tile.getPos(), 0));
			this.insert.facing = this.incrementFace(this.insert.facing);
			this.insert.tooltip = this.getButtonTooltip(this.insert);
		} else if (button == this.extract) {
			NetworkThingy.THINGY.sendToServer(new InterfaceAutoChangePacket(this.tile.getPos(), 1));
			this.extract.facing = this.incrementFace(this.extract.facing);
			this.extract.tooltip = this.getButtonTooltip(this.extract);
		} else if (button == this.eject) {
			NetworkThingy.THINGY.sendToServer(new InterfaceAutoChangePacket(this.tile.getPos(), 2));
			this.eject.on = !this.eject.on;
			this.eject.tooltip = this.getButtonTooltip(this.eject);
		} else if (button == this.smart) {
			this.smart.on = !this.smart.on;
			this.smart.tooltip = this.getButtonTooltip(this.smart);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = Utils.localize("container.ec.interface");
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(Utils.localize("container.inventory"), 8, this.ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.parent.GUI);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		
		int i1 = this.parent.getEnergyBarScaled(78);
		this.drawTexturedModalRect(x + 7, y + 94 - i1, 178, 78 - i1, 15, i1 + 1);
	}
	
	public void updateButtons() {
		this.insert.facing = this.tile.getInserterFace();
		this.extract.facing = this.tile.getExtractorFace();
		this.eject.on = this.tile.getAutoEject();
		this.smart.on = this.tile.getSmartInsert();
		
		this.insert.tooltip = this.getButtonTooltip(this.insert);
		this.extract.tooltip = this.getButtonTooltip(this.extract);
		this.eject.tooltip = this.getButtonTooltip(this.eject);
		this.smart.tooltip = this.getButtonTooltip(this.smart);
	}
	
	private List<String> getButtonTooltip(GuiButton button) {
		List<String> list = new ArrayList<>();
		
		if (button instanceof DirButton) {
			EnumFacing facing = ((DirButton) button).facing;
			if (facing == null || facing == EnumFacing.DOWN) {
				list.add(button.displayString + " -> " + Colors.RED + Utils.localize("ec.interface.none"));
			} else {
				list.add(button.displayString + " -> " + Colors.YELLOW + facing.getName().toUpperCase(Locale.ROOT));
			}
			
			if (button == this.insert) list.add(Colors.GRAY + Utils.localize("ec.interface.insert_desc"));
			if (button == this.extract) list.add(Colors.GRAY + Utils.localize("ec.interface.extract_desc"));
		} else if (button instanceof CheckboxButton) {
			if (((CheckboxButton) button).on) {
				list.add(button.displayString + " -> " + (Colors.GREEN + Utils.localize("ec.interface.on")));
			} else {
				list.add(button.displayString + " -> " + (Colors.RED + Utils.localize("ec.interface.off")));
			}
			
			if (button == this.eject) list.add(Colors.GRAY + Utils.localize("ec.interface.eject_desc"));
			if (button == this.smart) list.add(Colors.GRAY + Utils.localize("ec.interface.smart_desc"));
		}
		
		return list;
	}
	
	private EnumFacing incrementFace(EnumFacing facing) {
		if (facing == null) return EnumFacing.UP;
		int index = facing.getIndex() + 1;
		if (index > EnumFacing.values().length - 1) return null;
		return EnumFacing.values()[index];
	}

    public class CheckboxButton extends GuiButton {
    	
    	public List<String> tooltip;
    	public boolean on;
    	
		public CheckboxButton(int id, int x, int y, String text) {
			super(id, x, y, 68, 11, text);
			this.on = true;
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
	            this.drawTexturedModalRect(this.x, this.y, 72, 197, this.width, this.height);
	            
	            if (this.on) {
	            	this.drawTexturedModalRect(this.x, this.y, 142, 197, 11, 11);
	            }
	            
	            this.mouseDragged(mc, mouseX, mouseY);

	            GlStateManager.pushMatrix();
	            GlStateManager.scale(0.85, 0.85, 1.0);
	            fontrenderer.drawString(this.displayString, (int) ((this.x + 12) / 0.85), (int) (((this.y + (this.height - 9) / 2) + 2) / 0.85), 4210752);
	            GlStateManager.popMatrix();
	        }
	    }
    }
    
    public class DirButton extends GuiButton {
    	
    	public EnumFacing facing;
    	public List<String> tooltip;
    	
		public DirButton(int id, int x, int y, String text) {
			super(id, x, y, 68, 11, text);
			this.facing = null;
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
	            this.drawTexturedModalRect(this.x, this.y, 72, 197, this.width, this.height);
	            this.drawTexturedModalRect(this.x, this.y, 155 + (11 * this.getIndex(this.facing)), 197, 11, 11);
	            
	            this.mouseDragged(mc, mouseX, mouseY);

	            GlStateManager.pushMatrix();
	            GlStateManager.scale(0.85, 0.85, 1.0);
	            fontrenderer.drawString(this.displayString, (int) ((this.x + 12) / 0.85), (int) (((this.y + (this.height - 9) / 2) + 2) / 0.85), 4210752);
	            GlStateManager.popMatrix();	        }
	    }
		
		private int getIndex(EnumFacing facing) {
			if (facing == null) return 0;
			return facing.getIndex();
		}
    }
}
