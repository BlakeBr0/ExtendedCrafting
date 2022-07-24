package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class CraftingCoreScreen extends BaseContainerScreen<CraftingCoreContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/crafting_core.png");
	private CraftingCoreTileEntity tile;

	public CraftingCoreScreen(CraftingCoreContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 194);
	}

	@Override
	protected void init() {
		super.init();

		this.tile = this.getTileEntity();
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		super.render(matrix, mouseX, mouseY, partialTicks);

		if (this.hasRecipe()) {
			var output = this.getRecipeOutput();

			this.drawItemStack(output, x + 148, y + 47);

			if (isHoveringSlot(x + 148, y + 47, mouseX, mouseY)) {
				this.renderTooltip(matrix, output, mouseX, mouseY);
			}
		}

		if (mouseX > x + 7 && mouseX < x + 20 && mouseY > y + 17 && mouseY < y + 94) {
			var text = Component.literal(number(this.getEnergyStored()) + " / " + number(this.getMaxEnergyStored()) + " FE");
			this.renderTooltip(matrix, text, mouseX, mouseY);
		}
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		this.font.draw(stack, title, (float) (this.imageWidth / 2 - this.font.width(title) / 2), 6.0F, 4210752);
		this.font.draw(stack, this.playerInventoryTitle, 8.0F, this.imageHeight - 94.0F, 4210752);

		stack.pushPose();
		stack.scale(0.75F, 0.75F, 0.75F);

		this.font.draw(stack, text("screen.extendedcrafting.crafting_core.pedestals", this.getPedestalCount()), 36, 36, -1);

		if (!this.hasRecipe()) {
			this.font.draw(stack, text("screen.extendedcrafting.crafting_core.no_recipe"), 36, 56, -1);
		} else {
			this.font.draw(stack, text("screen.extendedcrafting.crafting_core.power_cost", number(this.getEnergyRequired())) + " FE", 36, 56, -1);
			this.font.draw(stack, text("screen.extendedcrafting.crafting_core.power_rate", number(this.getEnergyRate())) + " FE/t", 36, 66, -1);
			if (this.getEnergyStored() < this.getEnergyRate()) {
				this.font.draw(stack, text("screen.extendedcrafting.crafting_core.no_power"), 36, 86, -1);
			}
		}

		stack.popPose();
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		super.renderDefaultBg(stack, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		int i1 = this.getEnergyBarScaled();

		this.blit(stack, x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);

		if (this.hasRecipe()) {
			if (this.getProgress() > 0 && this.getEnergyRate() > 0) {
				int i2 = this.getProgressBarScaled();
				this.blit(stack, x + 116, y + 47, 194, 0, i2 + 1, 16);
			}

			var output = this.getRecipeOutput();

			this.drawItemStack(output, x + 148, y + 47);

			if (isHoveringSlot(x + 148, y + 47, mouseX, mouseY)) {
				this.drawItemHoverOverlay(stack, x + 148, y + 47);
			}
		}
	}
	
	private void drawItemStack(ItemStack stack, int x, int y) {
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.translate(0.0D, 0.0D, 32.0D);
		RenderSystem.applyModelViewMatrix();
		this.setBlitOffset(200);
		this.itemRenderer.blitOffset = 200.0F;
		net.minecraft.client.gui.Font font = IClientItemExtensions.of(stack).getFont(stack, IClientItemExtensions.FontContext.ITEM_COUNT);

		if (font == null) font = this.font;
		this.itemRenderer.renderAndDecorateItem(stack, x, y);
//		this.itemRenderer.renderGuiItemDecorations(font, stack, x, y - (this.draggingItem.isEmpty() ? 0 : 8), "");
		this.setBlitOffset(0);
		this.itemRenderer.blitOffset = 0.0F;
	}

	private void drawItemHoverOverlay(PoseStack stack, int x, int y) {
//		RenderSystem.pushMatrix();
//		RenderSystem.disableLighting();
//		RenderSystem.disableDepthTest();
//		RenderSystem.colorMask(true, true, true, false);
//		this.fillGradient(stack, x, y, x + 16, y + 16, -2130706433, -2130706433);
//		RenderSystem.colorMask(true, true, true, true);
//		RenderSystem.enableLighting();
//		RenderSystem.enableDepthTest();
//		RenderSystem.popMatrix();
	}

	private CraftingCoreTileEntity getTileEntity() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var tile = level.getBlockEntity(this.getMenu().getPos());

			if (tile instanceof CraftingCoreTileEntity core)
				return core;
		}

		return null;
	}

	private boolean hasRecipe() {
		if (this.tile == null)
			return false;

		return this.tile.hasRecipe();
	}

	private ItemStack getRecipeOutput() {
		if (this.tile == null)
			return ItemStack.EMPTY;

		var recipe = this.tile.getActiveRecipe();

		if (recipe != null) {
			return recipe.getResultItem();
		}

		return ItemStack.EMPTY;
	}

	private int getEnergyStored() {
		if (this.tile == null)
			return 0;

		return this.tile.getEnergy().getEnergyStored();
	}

	private int getMaxEnergyStored() {
		if (this.tile == null)
			return 0;

		return this.tile.getEnergy().getMaxEnergyStored();
	}

	private int getEnergyRequired() {
		if (this.tile == null)
			return 0;

		return this.tile.getEnergyRequired();
	}

	private int getEnergyRate() {
		if (this.tile == null)
			return 0;

		return this.tile.getEnergyRate();
	}

	private int getProgress() {
		if (this.tile == null)
			return 0;

		return this.tile.getProgress();
	}

	private int getPedestalCount() {
		if (this.tile == null)
			return 0;

		return this.tile.getPedestalCount();
	}

	private int getEnergyBarScaled() {
		int i = this.getEnergyStored();
		int j = this.getMaxEnergyStored();
		return (int) (j != 0 && i != 0 ? i * (long) 78 / j : 0);
	}

	private int getProgressBarScaled() {
		int i = this.getProgress();
		long j = this.getEnergyRequired();
		return (int) (j != 0 && i != 0 ? (long) i * 24 / j : 0);
	}

	private static boolean isHoveringSlot(int x, int y, int mouseX, int mouseY) {
		return mouseX > x - 1 && mouseX < x + 16 && mouseY > y - 1 && mouseY < y + 16;
	}
}
