package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.client.screen.widget.EnergyBarWidget;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

public class CraftingCoreScreen extends BaseContainerScreen<CraftingCoreContainer> {
	private static final Identifier BACKGROUND = ExtendedCrafting.resource("textures/gui/crafting_core.png");

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

		this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 17, this.menu::getEnergyStored, this.menu::getMaxEnergyCapacity));
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		gfx.text(this.font, this.title, (this.imageWidth / 2 - this.font.width(title) / 2), 6, -12566464, false);
		gfx.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, -12566464, false);

		var matrix = gfx.pose();

		matrix.pushMatrix();
		matrix.scale(0.75F, 0.75F);

		gfx.text(this.font, text("screen.extendedcrafting.crafting_core.pedestals", this.menu.getPedestalCount()), 36, 36, -1);

		if (!this.hasRecipe()) {
			gfx.text(this.font, text("screen.extendedcrafting.crafting_core.no_recipe"), 36, 56, -1);
		} else {
			gfx.text(this.font, text("screen.extendedcrafting.crafting_core.power_cost", number(this.menu.getEnergyRequired())) + " FE", 36, 56, -1);
			gfx.text(this.font, text("screen.extendedcrafting.crafting_core.power_rate", number(this.menu.getEnergyRate())) + " FE/t", 36, 66, -1);

			if (this.menu.getEnergyStored() < this.menu.getEnergyRate()) {
				gfx.text(this.font, text("screen.extendedcrafting.crafting_core.no_power"), 36, 86, -1);
			}
		}

		matrix.popMatrix();
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		var isHoldingItem = !this.menu.getCarried().isEmpty() || this.isDragging();

		if (!isHoldingItem && isHoveringSlot(x + 148, y + 47, mouseX, mouseY)) {
			var output = this.getRecipeOutput();
			if (!output.isEmpty()) {
				gfx.setTooltipForNextFrame(this.font, output, mouseX, mouseY);
			}
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float a) {
		super.extractBackground(gfx, mouseX, mouseY, a);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.hasRecipe()) {
			if (this.menu.getProgress() > 0 && this.menu.getEnergyRate() > 0) {
				int i2 = this.getProgressBarScaled();
				gfx.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x + 116, y + 47, 194, 0, i2 + 1, 16, 256, 256);
			}

			var output = this.getRecipeOutput();

			if (isHoveringSlot(x + 148, y + 47, mouseX, mouseY)) {
				extractSlotHighlightBack(gfx, x + 148, y + 47);
			}

			gfx.item(output, x + 148, y + 47);

			if (isHoveringSlot(x + 148, y + 47, mouseX, mouseY)) {
				extractSlotHighlightFront(gfx, x + 148, y + 47);
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

		var recipe = this.tile.getActiveClientRecipe();

		if (recipe != null) {
			return recipe.assemble(CraftingInput.EMPTY);
		}

		return ItemStack.EMPTY;
	}

	private int getProgressBarScaled() {
		int i = this.menu.getProgress();
		long j = this.menu.getEnergyRequired();
		return (int) (j != 0 && i != 0 ? (long) i * 24 / j : 0);
	}
}
