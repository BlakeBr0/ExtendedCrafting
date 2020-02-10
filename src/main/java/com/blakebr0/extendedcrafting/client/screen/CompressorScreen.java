package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.EjectModeSwitchMessage;
import com.blakebr0.extendedcrafting.network.message.InputLimitSwitchMessage;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class CompressorScreen extends ContainerScreen<CompressorContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/compressor.png");

	private CompressorTileEntity tile;

	public CompressorScreen(CompressorContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.tile = tile;
		this.xSize = 176;
		this.ySize = 194;
	}

//	private int getEnergyBarScaled(int pixels) {
//		int i = this.tile.getEnergy().getEnergyStored();
//		int j = this.tile.getEnergy().getMaxEnergyStored();
//		return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
//	}
//
//	private int getMaterialBarScaled(int pixels) {
//		int i = MathHelper.clamp(this.tile.getMaterialCount(), 0, this.tile.getRecipe().getInputCount());
//		int j = this.tile.getRecipe().getInputCount();
//		return j != 0 && i != 0 ? i * pixels / j : 0;
//	}
//
//	private int getProgressBarScaled(int pixels) {
//		int i = this.tile.getProgress();
//		int j = this.tile.getRecipe().getPowerCost();
//		return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
//	}

	private void drawItemStack(ItemStack stack, int x, int y, String altText) {
//		GlStateManager.translate(0.0F, 0.0F, 32.0F);
//		this.zLevel = 50.0F;
//		this.itemRender.zLevel = 50.0F;
//		FontRenderer font = null;
//		if (!stack.isEmpty())
//			font = stack.getItem().getFontRenderer(stack);
//		if (font == null)
//			font = fontRenderer;
//		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
//		this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, altText);
//		this.zLevel = 0.0F;
//		this.itemRender.zLevel = 0.0F;
	}

	private void drawFakeItemStack(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY) {
//		GlStateManager.pushMatrix();
//		RenderHelper.enableGUIStandardItemLighting();
//		this.drawItemStack(stack, this.guiLeft + xOffset, this.guiTop + yOffset, (String) null);
//		GlStateManager.popMatrix();
	}

	private void drawFakeItemStackTooltip(ItemStack stack, int xOffset, int yOffset, int mouseX, int mouseY) {
		if (mouseX > this.guiLeft + xOffset - 1 && mouseX < guiLeft + xOffset + 16 && mouseY > this.guiTop + yOffset - 1 && mouseY < this.guiTop + yOffset + 16) {
			if (!stack.isEmpty()) {
//				this.renderToolTip(stack, mouseX, mouseY);
			}
		}
	}

	private ItemStack getStack(Object obj) {
		if (obj instanceof ItemStack) {
			return ((ItemStack) obj).copy();
		} else if (obj instanceof List) {
			return ((List<ItemStack>) obj).get(0);
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = this.getTitle().getFormattedText();
		this.font.drawString(title, (float) (this.xSize / 2 - this.font.getStringWidth(title) / 2), 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getFormattedText();
		this.font.drawString(inventory, 20.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(this.guiLeft + 69, this.guiTop + 29, 11, 9, "", button -> {
			NetworkHandler.INSTANCE.sendToServer(new EjectModeSwitchMessage(this.tile.getPos().toLong()));
		}) {
			@Override
			public void render(int mouseX, int mouseY, float partialTicks) {

			}
		});
		this.addButton(new Button(this.guiLeft + 91, this.guiTop + 74, 7, 10, "", button -> {
			NetworkHandler.INSTANCE.sendToServer(new InputLimitSwitchMessage(this.tile.getPos().toLong()));
		}) {
			@Override
			public void render(int mouseX, int mouseY, float partialTicks) {

			}
		});
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int left = this.guiLeft;
		int top = this.guiTop;

		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
//
//		if (mouseX > left + 7 && mouseX < guiLeft + 20 && mouseY > this.guiTop + 17 && mouseY < this.guiTop + 94) {
//			this.drawHoveringText(Collections.singletonList(Utils.format(this.tile.getEnergy().getEnergyStored()) + " FE"), mouseX, mouseY);
//		}
//
//		if (mouseX > left + 60 && mouseX < guiLeft + 85 && mouseY > this.guiTop + 74 && mouseY < this.guiTop + 83) {
//			List<String> l = new ArrayList<String>();
//			if (this.tile.getMaterialCount() < 1) {
//				l.add(Utils.localize("tooltip.ec.empty"));
//			} else {
//				if (!this.tile.getMaterialStack().isEmpty()) {
//					l.add(this.tile.getMaterialStack().getDisplayName());
//				}
//				l.add(Utils.format(this.tile.getMaterialCount()) + " / " + Utils.format((this.tile.getRecipe() != null ? this.tile.getRecipe().getInputCount() : 0)));
//			}
//			this.drawHoveringText(l, mouseX, mouseY);
//		}
//
//		if (mouseX > guiLeft + 68 && mouseX < guiLeft + 79 && mouseY > guiTop + 28 && mouseY < guiTop + 39) {
//			if (this.tile.isEjecting()) {
//				this.drawHoveringText(Utils.localize("tooltip.ec.ejecting"), mouseX, mouseY);
//			} else {
//				this.drawHoveringText(Utils.localize("tooltip.ec.eject"), mouseX, mouseY);
//			}
//		}
//
//		if (mouseX > guiLeft + 90 && mouseX < guiLeft + 98 && mouseY > guiTop + 73 && mouseY < guiTop + 84) {
//			if (this.tile.isLimitingInput()) {
//				this.drawHoveringText(Utils.localize("tooltip.ec.limited_input"), mouseX, mouseY);
//			} else {
//				this.drawHoveringText(Utils.localize("tooltip.ec.unlimited_input"), mouseX, mouseY);
//			}
//		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.blit(x, y, 0, 0, this.xSize, this.ySize);

//		int i1 = this.getEnergyBarScaled(78);
//		this.blit(x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);

//		if (this.tile != null && this.tile.getRecipe() != null) {
//			if (this.tile.getMaterialCount() > 0 && this.tile.getRecipe().getInputCount() > 0) {
//				int i2 = getMaterialBarScaled(26);
//				this.blit(x + 60, y + 74, 194, 19, i2 + 1, 10);
//			}
//			if (this.tile.getProgress() > 0 && this.tile.getRecipe().getPowerCost() > 0) {
//				int i2 = getProgressBarScaled(24);
//				this.blit(x + 96, y + 47, 194, 0, i2 + 1, 16);
//			}
//		}

		if (mouseX > guiLeft + 68 && mouseX < guiLeft + 79 && mouseY > guiTop + 28 && mouseY < guiTop + 39) {
			this.blit(x + 68, y + 30, 194, 32, 11, 9);
		}
		
		
		if (mouseX > guiLeft + 90 && mouseX < guiLeft + 98 && mouseY > guiTop + 73 && mouseY < guiTop + 84) {
			if (this.tile.isLimitingInput()) {
				this.blit(x + 90, y + 74, 194, 56, 9, 10);
			} else {
				this.blit(x + 90, y + 74, 194, 43, 9, 10);
			}
		} else {
			if (this.tile.isLimitingInput()) {
				this.blit(x + 90, y + 74, 203, 56, 9, 10);
			}
		}
	}
}
