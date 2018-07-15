package com.blakebr0.extendedcrafting.client.gui.automationinterface;

import java.io.IOException;

import com.blakebr0.cucumber.gui.button.GuiButtonArrow;
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
	public GuiButton back;
		
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
		
		this.back = this.addButton(new GuiButtonArrow(0, (this.width / 2) - 15, ((this.height - this.ySize) / 2) + 4));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			Minecraft.getMinecraft().displayGuiScreen(this.parent);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.grid);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		
		if (this.parent.tile.hasRecipe()) {
			ItemStackHandler recipe = this.parent.tile.getRecipe();
			int s = (int) Math.sqrt(recipe.getSlots());
			for (int i = 0; i < s; i++) {
				for (int j = 0; j < s; j++) {
					int b = i * s + j;
					this.drawItemStack(recipe.getStackInSlot(b), x + 13 + (j * 18), y + 22 + (i * 18));
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
        if (font == null) font = this.fontRenderer;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, "");
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GlStateManager.popMatrix();
    }
}
