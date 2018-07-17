package com.blakebr0.extendedcrafting.client.gui.automationinterface;

import java.awt.Point;
import java.io.IOException;

import com.blakebr0.cucumber.gui.GuiIcons;
import com.blakebr0.cucumber.gui.button.IconButton;
import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.container.ContainerAutomationInterface;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import com.blakebr0.extendedcrafting.util.InterfaceRecipeChangePacket;
import com.blakebr0.extendedcrafting.util.NetworkThingy;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiAutomationInterface extends GuiContainer {

	protected static final ResourceLocation GUI = ResourceHelper.getResource(ExtendedCrafting.MOD_ID, "textures/gui/automation_interface.png");
	protected TileAutomationInterface tile;
	private GuiButton save, clear, view, config;
	
	public GuiAutomationInterface(ContainerAutomationInterface container) {
		super(container);
		this.tile = container.tile;
		this.xSize = 176;
		this.ySize = 193;
	}
	
	protected int getEnergyBarScaled(int pixels) {
		int i = this.tile.getEnergy().getEnergyStored();
		int j = this.tile.getEnergy().getMaxEnergyStored();
		return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		
		if (mouseX > this.guiLeft + 7 && mouseX < this.guiLeft + 20 && mouseY > this.guiTop + 16 && mouseY < this.guiTop + 93) {
			this.drawHoveringText(Utils.format(this.tile.getEnergy().getEnergyStored()) + " FE", mouseX, mouseY);
		}
		
		for (GuiButton button : this.buttonList) {
			if (button instanceof IconButton && button.isMouseOver()) {
				this.drawHoveringText(button.displayString, mouseX, mouseY);
			}
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		
		this.save = this.addButton(new IconButton(0, x + 63, y + 77, 14, 14, 1, 198, Utils.localize("ec.interface.save"), GUI));
		this.clear = this.addButton(new IconButton(1, x + 79, y + 77, 14, 14, 17, 198, Utils.localize("ec.interface.clear"), GUI));
		this.view = this.addButton(new IconButton(5, x + 95, y + 77, 14, 14, 33, 198, Utils.localize("ec.interface.view"), GUI));
		
		this.config = this.addButton(new IconButton(10, x + 115, y + 75, 16, 16, 53, 196, Utils.localize("ec.interface.config"), GUI));
		
		this.updateButtons();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.save) {
			NetworkThingy.THINGY.sendToServer(new InterfaceRecipeChangePacket(this.tile.getPos().toLong(), 0));
			this.save.enabled = false;
			this.clear.enabled = true;
			this.view.enabled = true;
		} else if (button == this.clear) {
			NetworkThingy.THINGY.sendToServer(new InterfaceRecipeChangePacket(this.tile.getPos().toLong(), 1));
			this.save.enabled = true;
			this.clear.enabled = false;
			this.view.enabled = false;
		} else if (button == this.view) {
			Point size = this.getSizeForGrid();
			this.mc.displayGuiScreen(new GuiViewRecipe(this, size.x, size.y, (int) Math.sqrt(this.tile.getRecipe().getSlots())));
		} else if (button == this.config) {
			this.mc.displayGuiScreen(new GuiInterfaceConfig(this));
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = Utils.localize("container.ec.interface");
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(Utils.localize("container.inventory"), 8, this.ySize - 94, 4210752);
		GuiIcons.drawCheckOrX(63, 20, this.tile.hasTable());
		this.fontRenderer.drawString(Utils.localize("ec.interface.table"), 79, 23, -1);
		GuiIcons.drawCheckOrX(63, 36, this.tile.hasRecipe());
		this.fontRenderer.drawString(Utils.localize("ec.interface.recipe"), 79, 39, -1);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GUI);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		
		int i1 = this.getEnergyBarScaled(78);
		this.drawTexturedModalRect(x + 7, y + 94 - i1, 178, 78 - i1, 15, i1 + 1);
	}
    
    private void drawItemStack(ItemStack stack, int x, int y) {
    	GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = this.fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, (String) null);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GlStateManager.popMatrix();
    }
    
    public void updateButtons() {
    	this.save.enabled = !this.tile.hasRecipe() && this.tile.hasTable();
    	this.clear.enabled = this.tile.hasRecipe();
    	this.view.enabled = this.tile.hasRecipe();
    }
    
    private Point getSizeForGrid() {
    	int size = (int) Math.sqrt(this.tile.getRecipe().getSlots());
    	switch (size) {
    	case 3:
    		return new Point(78, 87);
    	case 5:
    		return new Point(114, 123);
    	case 7:
    		return new Point(150, 159);
    	case 9:
    		return new Point(186, 195);
    	default:
    		return new Point(186, 195);
    	}
    }
}
