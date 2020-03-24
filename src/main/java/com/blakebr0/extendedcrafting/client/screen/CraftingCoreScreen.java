package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

		if (container.hasRecipe()) {
			ItemStack output = this.getRecipeOutput();
			this.drawItemStack(output, left + 148, top + 47);

			if (this.isHoveringSlot(left + 148, top + 47, mouseX, mouseY)) {
				this.renderTooltip(output, mouseX, mouseY);
			}
		}

		if (mouseX > left + 7 && mouseX < left + 20 && mouseY > top + 17 && mouseY < top + 94) {
			this.renderTooltip(container.getEnergyStored() + " FE", mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int left = this.guiLeft;
		int top = this.guiTop;
		CraftingCoreContainer container = this.getContainer();

		this.blit(left, top, 0, 0, this.xSize, this.ySize);

		int i1 = container.getEnergyBarScaled(78);
		this.blit(left + 7, top + 95 - i1, 178, 78 - i1, 15, i1 + 1);

		if (container.hasRecipe()) {
			if (container.getProgress() > 0 && container.getEnergyRate() > 0) {
				int i2 = container.getProgressBarScaled(24);
				this.blit(left + 116, top + 47, 194, 0, i2 + 1, 16);
			}

			ItemStack output = this.getRecipeOutput();
			this.drawItemStack(output, left + 148, top + 47);

			if (this.isHoveringSlot(left + 148, top + 47, mouseX, mouseY)) {
				this.drawItemHoverOverlay(left + 148, top + 47);
			}
		}
	}

	private boolean isHoveringSlot(int x, int y, int mouseX, int mouseY) {
		return mouseX > x - 1 && mouseX < x + 16 && mouseY > y - 1 && mouseY < y + 16;
	}
	
	private void drawItemStack(ItemStack stack, int x, int y) {
    	RenderSystem.pushMatrix();
    	RenderHelper.enableStandardItemLighting();
        RenderSystem.translatef(0.0F, 0.0F, -32.0F);
//        this.itemRenderer.zLevel = 200.0F;
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = this.font;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y, null);
//        this.itemRenderer.zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
        RenderSystem.popMatrix();
	}

	private void drawItemHoverOverlay(int x, int y) {
		RenderSystem.pushMatrix();
		RenderSystem.disableLighting();
		RenderSystem.disableDepthTest();
		RenderSystem.colorMask(true, true, true, false);
		this.fillGradient(x, y, x + 16, y + 16, -2130706433, -2130706433);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.enableLighting();
		RenderSystem.enableDepthTest();
		RenderSystem.popMatrix();
	}

	private String text(String key, Object... args) {
		return Localizable.of(key).args(args).buildString();
	}

	private ItemStack getRecipeOutput() {
		ClientWorld world = this.getMinecraft().world;
		if (world != null) {
			CraftingCoreContainer container = this.getContainer();
			TileEntity tile = world.getTileEntity(container.getPos());
			if (tile instanceof CraftingCoreTileEntity) {
				CraftingCoreTileEntity core = (CraftingCoreTileEntity) tile;
				CombinationRecipe recipe = core.getActiveRecipe();
				if (recipe != null) {
					return recipe.getRecipeOutput();
				}
			}
		}

		return ItemStack.EMPTY;
	}
}
