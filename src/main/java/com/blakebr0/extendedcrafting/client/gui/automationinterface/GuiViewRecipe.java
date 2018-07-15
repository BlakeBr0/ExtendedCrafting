package com.blakebr0.extendedcrafting.client.gui.automationinterface;

import java.io.IOException;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

public class GuiViewRecipe extends GuiContainer {
	
	private GuiAutomationInterface parent;
	private int xSize, ySize;
	
	protected static final ResourceLocation GRID_3x3 = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/grid_3x3.png");
	protected static final ResourceLocation GRID_5x5 = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/grid_5x5.png");
	protected static final ResourceLocation GRID_7x7 = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/grid_7x7.png");
	protected static final ResourceLocation GRID_9x9 = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/grid_9x9.png");
	protected static ResourceLocation grid;
		
	public GuiViewRecipe(GuiAutomationInterface parent, int xSize, int ySize, int size) {
		super(new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer player) {
				return false;
			}
		});
		this.parent = parent;
		this.xSize = xSize;
		this.ySize = ySize;
		switch (size) {
		case 3:
			this.grid = GRID_3x3;
			break;
		case 5:
			this.grid = GRID_5x5;
			break;
		case 7:
			this.grid = GRID_7x7;
			break;
		case 9:
			this.grid = GRID_9x9;
			break;
		default:
			this.grid = GRID_3x3;
			break;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new SmallButton(0, (this.width / 2) - 20, ((this.height - this.ySize) / 2) + 5, 40, 12, Utils.localize("ec.interface.back")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			Minecraft.getMinecraft().displayGuiScreen(parent);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(grid);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		
		if (parent.tile.hasRecipe()) {
			ItemStackHandler recipe = parent.tile.getRecipe();
			int s = (int) Math.sqrt(recipe.getSlots());
			for (int i = 0; i < s; i++) {
				for (int j = 0; j < s; j++) {
					int b = i * s + j;
					this.drawItemStack(recipe.getStackInSlot(b), ((this.width - this.xSize) / 2) + 13 + j * 18, ((this.height - this.ySize) / 2) + 22 + i * 18);
				}
			}
		}
	}
	
    private void drawItemStack(ItemStack stack, int x, int y) {
    	GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, "");
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GlStateManager.popMatrix();
    }
    
    public class SmallButton extends GuiButton {

		public SmallButton(int id, int x, int y, int width, int height, String text) {
			super(id, x, y, width, height, text);
		}
    	
		@Override
	    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
	        if (this.visible) {
	            FontRenderer fontrenderer = mc.fontRenderer;
	            mc.getTextureManager().bindTexture(parent.GUI);
	            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	            int i = this.getHoverState(this.hovered);
	            GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	            this.drawTexturedModalRect(this.x, this.y, 0, 185 + i * 12, this.width / 2, this.height);
	            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 185 + i * 12, this.width / 2, this.height);
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
