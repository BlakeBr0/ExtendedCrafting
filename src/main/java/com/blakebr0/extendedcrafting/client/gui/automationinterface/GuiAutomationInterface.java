package com.blakebr0.extendedcrafting.client.gui.automationinterface;

import java.io.IOException;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.container.ContainerAutomationInterface;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import com.blakebr0.extendedcrafting.util.InterfaceRecipeChangePacket;
import com.blakebr0.extendedcrafting.util.NetworkThingy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class GuiAutomationInterface extends GuiContainer {

	protected static final ResourceLocation GUI = ResourceHelper.getResource(ExtendedCrafting.MOD_ID, "textures/gui/automation_interface.png");
	protected TileAutomationInterface tile;
	
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
			this.drawHoveringText(Utils.asList(Utils.format(this.tile.getEnergy().getEnergyStored()) + " FE"), mouseX, mouseY);
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new SmallButton(0, (this.width - this.xSize) / 2 + 62, (this.height - this.ySize) / 2 + 56, 71, 12, Utils.localize("ec.interface.save")));
		this.buttonList.add(new SmallButton(1, (this.width - this.xSize) / 2 + 62, (this.height - this.ySize) / 2 + 68, 71, 12, Utils.localize("ec.interface.clear")));
		this.buttonList.add(new SmallButton(5, (this.width - this.xSize) / 2 + 62, (this.height - this.ySize) / 2 + 80, 71, 12, Utils.localize("ec.interface.view")));
		
		this.buttonList.add(new SmallButton(10, (this.width - this.xSize) / 2 + 129, (this.height - this.ySize) / 2 + 96, 40, 12, Utils.localize("ec.interface.config")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			NetworkThingy.THINGY.sendToServer(new InterfaceRecipeChangePacket(this.tile.getPos().toLong(), 0));
			this.buttonList.get(0).enabled = false;
			this.buttonList.get(1).enabled = true;
			this.buttonList.get(2).enabled = true;
		} else if (button.id == 1) {
			NetworkThingy.THINGY.sendToServer(new InterfaceRecipeChangePacket(this.tile.getPos().toLong(), 1));
			this.buttonList.get(0).enabled = true;
			this.buttonList.get(1).enabled = false;
			this.buttonList.get(2).enabled = false;
		} else if (button.id == 5) {
			RecipeGuiSize size = this.getSizeForGrid();
			Minecraft.getMinecraft().displayGuiScreen(new GuiViewRecipe(this, size.x, size.y, (int) Math.sqrt(this.tile.getRecipe().getSlots())));
		} else if (button.id == 10) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiInterfaceConfig(this));
		}
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.buttonList.get(0).enabled = !this.tile.hasRecipe() && this.tile.hasTable();
		this.buttonList.get(1).enabled = this.tile.hasRecipe();
		this.buttonList.get(2).enabled = this.tile.hasRecipe();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = Utils.localize("container.ec.interface");
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(Utils.localize("container.inventory"), 8, this.ySize - 94, 4210752);
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.75F, 0.75F, 0.75F);
		this.fontRenderer.drawString(Utils.localize("ec.interface.table") + StringUtils.capitalize(String.valueOf(this.tile.hasTable())), 83, 26, -1);
		this.fontRenderer.drawString(Utils.localize("ec.interface.recipe") + StringUtils.capitalize(String.valueOf(this.tile.hasRecipe())), 83, 36, -1);
		GlStateManager.popMatrix();
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
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, (String) null);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GlStateManager.popMatrix();
    }
    
    private RecipeGuiSize getSizeForGrid() {
    	int size = (int) Math.sqrt(this.tile.getRecipe().getSlots());
    	switch (size) {
    	case 3:
    		return new RecipeGuiSize(78, 87);
    	case 5:
    		return new RecipeGuiSize(114, 123);
    	case 7:
    		return new RecipeGuiSize(150, 159);
    	case 9:
    		return new RecipeGuiSize(186, 195);
    	default:
    		return new RecipeGuiSize(186, 195);
    	}
    }
    
	public class RecipeGuiSize {
		
		public int x;
		public int y;
		
		public RecipeGuiSize(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
    
    public class SmallButton extends GuiButton {

		public SmallButton(int id, int x, int y, int width, int height, String text) {
			super(id, x, y, width, height, text);
		}
    	
		@Override
	    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
	        if (this.visible) {
	            FontRenderer fontrenderer = mc.fontRenderer;
	            mc.getTextureManager().bindTexture(GUI);
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
