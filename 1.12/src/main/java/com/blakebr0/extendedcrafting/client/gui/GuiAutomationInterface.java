package com.blakebr0.extendedcrafting.client.gui;

import java.io.IOException;
import java.util.Collections;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.container.ContainerAutomationInterface;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import com.blakebr0.extendedcrafting.util.InterfaceRecipeChangePacket;
import com.blakebr0.extendedcrafting.util.NetworkThingy;

import net.minecraft.client.Minecraft;
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

	private static final ResourceLocation GUI = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/automation_interface.png");
	private TileAutomationInterface tile;
	
	public GuiAutomationInterface(ContainerAutomationInterface container) {
		super(container);
		this.tile = container.tile;
		this.xSize = 176;
		this.ySize = 184;
	}
	
	private int getEnergyBarScaled(int pixels) {
		int i = this.tile.getEnergy().getEnergyStored();
		int j = this.tile.getEnergy().getMaxEnergyStored();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		
		if (mouseX > this.guiLeft + 7 && mouseX < this.guiLeft + 20 && mouseY > this.guiTop + 7 && mouseY < this.guiTop + 84) {
			this.drawHoveringText(Collections.singletonList(Utils.format(this.tile.getEnergy().getEnergyStored()) + " RF"), mouseX, mouseY);
		}
		
		ItemStackHandler recipe = this.tile.getRecipe();
		for (int i = 0; i < recipe.getSlots(); i++) {
			this.drawItemStack(recipe.getStackInSlot(i), Utils.randInt(100, 200), Utils.randInt(100, 200));
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(0, (this.width - this.xSize) / 2 + 30, (this.height - this.ySize) / 2, 100, 10, "Save Recipe"));
		this.buttonList.add(new GuiButton(1, (this.width - this.xSize) / 2 + 30, (this.height - this.ySize) / 2 + 15, 100, 10, "Clear Recipe"));
		
		//this.buttonList.add(new GuiButton(10, this.guiLeft + 40, this.guiTop + 10, 80, 20, "Auto-Insert: None"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			NetworkThingy.THINGY.sendToServer(new InterfaceRecipeChangePacket(this.tile.getPos().toLong(), 0));
		} else if (button.id == 1) {
			NetworkThingy.THINGY.sendToServer(new InterfaceRecipeChangePacket(this.tile.getPos().toLong(), 1));
		}
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		//this.buttonList.get(0).enabled = !this.tile.hasRecipe(); // TODO: requires gui reopenining for some reason
		//this.buttonList.get(0).displayString = !this.tile.hasRecipe() ? "Save Recipe" : "Clear Recipe"; // TODO: localize
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.fontRenderer.drawString(Utils.localize("container.inventory"), 8, this.ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GUI);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		
		int i1 = this.getEnergyBarScaled(78);
		this.drawTexturedModalRect(x + 7, y + 85 - i1, 178, 78 - i1, 15, i1 + 1);
	}
    
    private void drawItemStack(ItemStack stack, int x, int y) {
    	GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, (String) null);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GlStateManager.popMatrix();
    }
}
