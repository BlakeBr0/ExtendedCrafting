package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CraftingCoreScreen extends BaseContainerScreen<CraftingCoreContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/crafting_core.png");
	private CraftingCoreTileEntity tile;

	public CraftingCoreScreen(CraftingCoreContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title, BACKGROUND, 176, 194);
	}

	@Override
	protected void init() {
		super.init();

		this.tile = this.getTileEntity();
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		super.render(stack, mouseX, mouseY, partialTicks);

		if (this.hasRecipe()) {
			ItemStack output = this.getRecipeOutput();
			this.drawItemStack(output, x + 148, y + 47);

			if (isHoveringSlot(x + 148, y + 47, mouseX, mouseY)) {
				this.renderTooltip(stack, output, mouseX, mouseY);
			}
		}

		if (mouseX > x + 7 && mouseX < x + 20 && mouseY > y + 17 && mouseY < y + 94) {
			StringTextComponent text = new StringTextComponent(number(this.getEnergyStored()) + " / " + number(this.getMaxEnergyStored()) + " FE");
			this.renderTooltip(stack, text, mouseX, mouseY);
		}
	}

	@Override
	protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.draw(stack, title, (float) (this.imageWidth / 2 - this.font.width(title) / 2), 6.0F, 4210752);
		String inventory = this.inventory.getDisplayName().getString();
		this.font.draw(stack, inventory, 8.0F, this.imageHeight - 94.0F, 4210752);

		RenderSystem.pushMatrix();
		RenderSystem.scalef(0.75F, 0.75F, 0.75F);
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

		RenderSystem.popMatrix();
	}

	@Override
	protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(stack, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		int i1 = this.getEnergyBarScaled();

		this.blit(stack, x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);

		if (this.hasRecipe()) {
			if (this.getProgress() > 0 && this.getEnergyRate() > 0) {
				int i2 = this.getProgressBarScaled();
				this.blit(stack, x + 116, y + 47, 194, 0, i2 + 1, 16);
			}

			ItemStack output = this.getRecipeOutput();
			this.drawItemStack(output, x + 148, y + 47);

			if (isHoveringSlot(x + 148, y + 47, mouseX, mouseY)) {
				this.drawItemHoverOverlay(stack, x + 148, y + 47);
			}
		}
	}
	
	private void drawItemStack(ItemStack stack, int x, int y) {
    	RenderSystem.pushMatrix();
    	RenderHelper.turnBackOn();
        RenderSystem.translatef(0.0F, 0.0F, -32.0F);
//        this.itemRenderer.zLevel = 200.0F;
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = this.font;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.itemRenderer.renderAndDecorateItem(stack, x, y);
        this.itemRenderer.renderGuiItemDecorations(font, stack, x, y, null);
//        this.itemRenderer.zLevel = 0.0F;
        RenderHelper.turnOff();
        RenderSystem.popMatrix();
	}

	private void drawItemHoverOverlay(MatrixStack stack, int x, int y) {
		RenderSystem.pushMatrix();
		RenderSystem.disableLighting();
		RenderSystem.disableDepthTest();
		RenderSystem.colorMask(true, true, true, false);
		this.fillGradient(stack, x, y, x + 16, y + 16, -2130706433, -2130706433);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.enableLighting();
		RenderSystem.enableDepthTest();
		RenderSystem.popMatrix();
	}

	private CraftingCoreTileEntity getTileEntity() {
		ClientWorld world = this.getMinecraft().level;

		if (world != null) {
			TileEntity tile = world.getBlockEntity(this.getMenu().getPos());

			if (tile instanceof CraftingCoreTileEntity) {
				return (CraftingCoreTileEntity) tile;
			}
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

		CombinationRecipe recipe = this.tile.getActiveRecipe();
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
