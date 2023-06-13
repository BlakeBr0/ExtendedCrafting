package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.client.screen.widget.EnergyBarWidget;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CraftingCoreScreen extends BaseContainerScreen<CraftingCoreContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/crafting_core.png");
	private CraftingCoreTileEntity tile;

	public CraftingCoreScreen(CraftingCoreContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 194);
	}

	@Override
	protected void init() {
		super.init();
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		this.tile = this.getTileEntity();

		if (this.tile != null) {
			this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 17, tile.getEnergy()));
		}
	}

	@Override
	public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		super.render(gfx, mouseX, mouseY, partialTicks);

		var isHoldingItem = !this.menu.getCarried().isEmpty() || this.isDragging();

		if (!isHoldingItem && isHoveringSlot(x + 148, y + 47, mouseX, mouseY)) {
			var output = this.getRecipeOutput();

			gfx.renderTooltip(this.font, output, mouseX, mouseY);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		gfx.drawString(this.font, title, (this.imageWidth / 2 - this.font.width(title) / 2), 6, 4210752, false);
		gfx.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);

		var matrix = gfx.pose();

		matrix.pushPose();
		matrix.scale(0.75F, 0.75F, 0.75F);

		gfx.drawString(this.font, text("screen.extendedcrafting.crafting_core.pedestals", this.getPedestalCount()), 36, 36, -1);

		if (!this.hasRecipe()) {
			gfx.drawString(this.font, text("screen.extendedcrafting.crafting_core.no_recipe"), 36, 56, -1);
		} else {
			gfx.drawString(this.font, text("screen.extendedcrafting.crafting_core.power_cost", number(this.getEnergyRequired())) + " FE", 36, 56, -1);
			gfx.drawString(this.font, text("screen.extendedcrafting.crafting_core.power_rate", number(this.getEnergyRate())) + " FE/t", 36, 66, -1);

			if (this.getEnergyStored() < this.getEnergyRate()) {
				gfx.drawString(this.font, text("screen.extendedcrafting.crafting_core.no_power"), 36, 86, -1);
			}
		}

		matrix.popPose();
	}

	@Override
	protected void renderBg(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
		super.renderDefaultBg(gfx, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.hasRecipe()) {
			if (this.getProgress() > 0 && this.getEnergyRate() > 0) {
				int i2 = this.getProgressBarScaled();
				gfx.blit(BACKGROUND, x + 116, y + 47, 194, 0, i2 + 1, 16);
			}

			var output = this.getRecipeOutput();

			gfx.renderItem(output, x + 148, y + 47);

			if (isHoveringSlot(x + 148, y + 47, mouseX, mouseY)) {
				renderSlotHighlight(gfx, x + 148, y + 47, 100);
			}
		}
	}

	private CraftingCoreTileEntity getTileEntity() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var tile = level.getBlockEntity(this.getMenu().getBlockPos());

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

		var level = this.tile.getLevel();
		if (level == null)
			return ItemStack.EMPTY;

		var recipe = this.tile.getActiveRecipe();

		if (recipe != null) {
			return recipe.getResultItem(level.registryAccess());
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

	private int getProgressBarScaled() {
		int i = this.getProgress();
		long j = this.getEnergyRequired();
		return (int) (j != 0 && i != 0 ? (long) i * 24 / j : 0);
	}

	private static boolean isHoveringSlot(int x, int y, int mouseX, int mouseY) {
		return mouseX > x - 1 && mouseX < x + 16 && mouseY > y - 1 && mouseY < y + 16;
	}
}
