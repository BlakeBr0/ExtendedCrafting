package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.client.screen.widget.EnergyBarWidget;
import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.RecipeSelectButton;
import com.blakebr0.extendedcrafting.client.screen.button.ToggleTableRunningButton;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class EliteAutoTableScreen extends BaseContainerScreen<EliteAutoTableContainer> {
	public static final Identifier BACKGROUND = ExtendedCrafting.resource("textures/gui/elite_auto_table.png");
	private AutoTableTileEntity tile;

	public EliteAutoTableScreen(EliteAutoTableContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 220, 242);
	}

	@Override
	public void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		var pos = this.getMenu().getBlockPos();

		this.addRenderableWidget(new ToggleTableRunningButton(x + 192, y + 96, pos, this.menu::isRunning));

		this.tile = this.getTileEntity();

		this.addRenderableWidget(new RecipeSelectButton(x + 176, y + 7, pos, 0, this.menu::getSelectedRecipeIndex, this::onSelectButtonTooltip));
		this.addRenderableWidget(new RecipeSelectButton(x + 189, y + 7, pos, 1, this.menu::getSelectedRecipeIndex, this::onSelectButtonTooltip));
		this.addRenderableWidget(new RecipeSelectButton(x + 202, y + 7, pos, 2, this.menu::getSelectedRecipeIndex, this::onSelectButtonTooltip));

		this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 41, this.menu::getEnergyStored, this.menu::getMaxEnergyCapacity));
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		gfx.text(this.font, this.title, 26, 6, 4210752, false);
		gfx.text(this.font, this.playerInventoryTitle, 30, this.imageHeight - 94, 4210752, false);
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		super.extractTooltip(gfx, mouseX, mouseY);

		if (mouseX > x + 192 && mouseX < x + 205 && mouseY > y + 95 && mouseY < y + 111) {
			gfx.setTooltipForNextFrame(this.font, ModTooltips.TOGGLE_AUTO_CRAFTING.color(ChatFormatting.WHITE).toComponent(), mouseX, mouseY);
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float a) {
		super.extractBackground(gfx, mouseX, mouseY, a);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.menu.isRunning()) {
			int i2 = this.getProgressBarScaled();
			gfx.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x + 191, y + 95, 238, 0, 13, i2, 256, 256);
		}

		var recipe = this.getSelectedRecipe();

		if (recipe != null) {
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					int index = (i * 7) + j;
					var stack = recipe.getResource(index).toStack();

					this.extractGhostItem(gfx, x + 27 + (j * 18), y + 18 + (i * 18), stack);
				}
			}

			var output = recipe.getResource(recipe.size() - 1).toStack();

			this.extractGhostItem(gfx, x + 191, y + 71, output);
		}
	}

	private void onSelectButtonTooltip(Button button, GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		var index = ((RecipeSelectButton) button).getIndex();
		var isSelected = ((RecipeSelectButton) button).isSelected();
		var recipe = this.getRecipeInfo(index);

		if (recipe != null) {
			List<Component> tooltip;
			var hasRecipe = !recipe.getStacks().stream().allMatch(ItemStack::isEmpty);

			if (hasRecipe) {
				var output = recipe.getResource(recipe.size() - 1);

				tooltip = Lists.newArrayList(
						Component.literal(recipe.getAmountAsInt(recipe.size() - 1) + "x " + output.getHoverName().getString()),
						Component.literal(""),
						ModTooltips.AUTO_TABLE_DELETE_RECIPE.color(ChatFormatting.WHITE).toComponent()
				);

				if (isSelected) {
					tooltip.add(1, ModTooltips.SELECTED.color(ChatFormatting.GREEN).toComponent());
				}
			} else {
				tooltip = Lists.newArrayList(
						ModTooltips.AUTO_TABLE_SAVE_RECIPE.color(ChatFormatting.WHITE).toComponent()
				);

				if (isSelected) {
					tooltip.add(0, ModTooltips.SELECTED.color(ChatFormatting.GREEN).toComponent());
					tooltip.add(1, Component.literal(""));
				}
			}

			gfx.setTooltipForNextFrame(this.font, tooltip, Optional.empty(), mouseX, mouseY);
		}
	}

	private AutoTableTileEntity getTileEntity() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var tile = level.getBlockEntity(this.getMenu().getBlockPos());

			if (tile instanceof AutoTableTileEntity table)
				return table;
		}

		return null;
	}

	private CItemStacksHandler getRecipeInfo(int selected) {
		if (this.tile == null)
			return null;

		return this.tile.getRecipeStorage().getRecipe(selected);
	}

	private CItemStacksHandler getSelectedRecipe() {
		if (this.tile == null)
			return null;

		return this.tile.getRecipeStorage().getSelectedRecipe();
	}

	private int getProgressBarScaled() {
		int i = this.menu.getProgress();
		int j = this.menu.getProgressRequired();
		return j != 0 && i != 0 ? i * 16 / j : 0;
	}
}