package com.blakebr0.extendedcrafting.client.gui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.container.ContainerCraftingCore;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.tile.TileCraftingCore;
import com.blakebr0.extendedcrafting.util.StackHelper;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiCraftingCore extends GuiContainer {

    private static final ResourceLocation GUI = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/crafting_core.png");
    
    private TileCraftingCore tile;
    private CombinationRecipe recipe;

    public GuiCraftingCore(TileCraftingCore tile, ContainerCraftingCore container){
        super(container);
        this.recipe = tile.getRecipe();
        this.tile = tile;
        this.xSize = 176;
        this.ySize = 184;
    }
    
    private int getEnergyBarScaled(int pixels){
        int i = this.tile.getEnergy().getEnergyStored();
        int j = this.tile.getEnergy().getMaxEnergyStored();
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
    
    private int getProgressBarScaled(int pixels){
        int i = this.tile.getProgress();
        int j = this.recipe.getCost();
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
    
    private void drawItemStack(ItemStack stack, int x, int y, String altText){
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 50.0F;
        this.itemRender.zLevel = 50.0F;
        net.minecraft.client.gui.FontRenderer font = null;
        if (stack != null) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRendererObj;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }
    
    private void drawFakeItemStack(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY){
    	this.drawItemStack(stack, this.guiLeft + xOffset, this.guiTop + yOffset, (String)null);
    }
    
    private void drawFakeItemStackTooltip(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY){
    	if(mouseX > this.guiLeft + xOffset - 1 && mouseX < guiLeft + xOffset + 16 && mouseY > this.guiTop + yOffset - 1 && mouseY < this.guiTop + yOffset + 16){
    		if(!StackHelper.isNull(stack)){
                this.drawRect(this.guiLeft + xOffset, this.guiTop + yOffset, this.guiLeft + xOffset + 16, this.guiTop + yOffset + 16, -2130706433);
        		this.renderToolTip(stack, mouseX, mouseY);
    		}
    	}
    }
    
    private ItemStack getStack(Object obj){
    	if(obj instanceof ItemStack){
    		return ((ItemStack)obj).copy();
    	} else if(obj instanceof List){
    		return ((List<ItemStack>)obj).get(0);
    	} else {
    		return null;
    	}
    }
    
    private ItemStack getPedestalStackFromIndex(int index){
    	List<Object> list = this.recipe.getPedestalItems();
    	if(index < list.size()){
    		return getStack(list.get(index));
    	} else {
    		return null;
    	}
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_){
        this.fontRendererObj.drawString(I18n.translateToLocal("container.inventory"), 8, this.ySize - 94, 4210752);
    }
    
    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
    	int left = this.guiLeft;
    	int top = this.guiTop;
    	
        super.drawScreen(mouseX, mouseY, partialTicks);
                
        if(tile.getRecipe() != null){
            this.drawFakeItemStack(this.getPedestalStackFromIndex(0), 37, 22, mouseX, mouseY);
            this.drawFakeItemStack(this.getPedestalStackFromIndex(1), 53, 22, mouseX, mouseY);
            this.drawFakeItemStack(this.getPedestalStackFromIndex(2), 69, 22, mouseX, mouseY);
            this.drawFakeItemStack(this.getPedestalStackFromIndex(3), 69, 38, mouseX, mouseY);
            this.drawFakeItemStack(this.getPedestalStackFromIndex(4), 69, 54, mouseX, mouseY);
            this.drawFakeItemStack(this.getPedestalStackFromIndex(5), 53, 54, mouseX, mouseY);
            this.drawFakeItemStack(this.getPedestalStackFromIndex(6), 37, 54, mouseX, mouseY);
            this.drawFakeItemStack(this.getPedestalStackFromIndex(7), 37, 38, mouseX, mouseY);

            this.drawFakeItemStack(this.recipe.getInput(), 53, 38, mouseX, mouseY);
            this.drawFakeItemStack(this.recipe.getOutput(), 138, 37, mouseX, mouseY);
            
            this.drawFakeItemStackTooltip(this.getPedestalStackFromIndex(0), 37, 22, mouseX, mouseY);
            this.drawFakeItemStackTooltip(this.getPedestalStackFromIndex(1), 53, 22, mouseX, mouseY);
            this.drawFakeItemStackTooltip(this.getPedestalStackFromIndex(2), 69, 22, mouseX, mouseY);
            this.drawFakeItemStackTooltip(this.getPedestalStackFromIndex(3), 69, 38, mouseX, mouseY);
            this.drawFakeItemStackTooltip(this.getPedestalStackFromIndex(4), 69, 54, mouseX, mouseY);
            this.drawFakeItemStackTooltip(this.getPedestalStackFromIndex(5), 53, 54, mouseX, mouseY);
            this.drawFakeItemStackTooltip(this.getPedestalStackFromIndex(6), 37, 54, mouseX, mouseY);
            this.drawFakeItemStackTooltip(this.getPedestalStackFromIndex(7), 37, 38, mouseX, mouseY);
            
            this.drawFakeItemStackTooltip(this.recipe.getInput(), 53, 38, mouseX, mouseY);
            this.drawFakeItemStackTooltip(this.recipe.getOutput(), 138, 37, mouseX, mouseY);
        }
        
    	if(mouseX > left + 7 && mouseX < guiLeft + 20 && mouseY > this.guiTop + 7 && mouseY < this.guiTop + 84){
            this.drawHoveringText(Collections.singletonList(NumberFormat.getInstance().format(this.tile.getEnergy().getEnergyStored()) + " RF"), mouseX, mouseY);
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
        this.drawTexturedModalRect(x + 7, y + 85 - i1, 178, 78 - i1, 15, i1 + 1);
                
        if(this.tile.getProgress() > 0 && this.recipe.getCost() > 0){
            int i2 = getProgressBarScaled(24);
            this.drawTexturedModalRect(x + 98, y + 37, 194, 0, i2 + 1, 16);
        }
    }
}
