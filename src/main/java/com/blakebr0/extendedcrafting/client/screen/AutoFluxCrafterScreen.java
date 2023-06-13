package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.render.GhostItemRenderer;
import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.client.screen.widget.EnergyBarWidget;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.RecipeSelectButton;
import com.blakebr0.extendedcrafting.container.AutoFluxCrafterContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AutoFluxCrafterTileEntity;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AutoFluxCrafterScreen extends BaseContainerScreen<AutoFluxCrafterContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/auto_flux_crafter.png");
	private AutoFluxCrafterTileEntity tile;

	public AutoFluxCrafterScreen(AutoFluxCrafterContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 194);
	}

	@Override
	protected void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		var pos = this.getMenu().getBlockPos();

		this.tile = this.getTileEntity();

		if (this.tile != null) {
			this.addRenderableWidget(new RecipeSelectButton(x + 132, y + 7, pos, 0, 66, this.tile.getRecipeStorage(), this::onSelectButtonTooltip));
			this.addRenderableWidget(new RecipeSelectButton(x + 145, y + 7, pos, 1, 66, this.tile.getRecipeStorage(), this::onSelectButtonTooltip));
			this.addRenderableWidget(new RecipeSelectButton(x + 158, y + 7, pos, 2, 66, this.tile.getRecipeStorage(), this::onSelectButtonTooltip));

			this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 17, this.tile.getEnergy()));
		}
	}

	@Override
	protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		gfx.drawString(this.font, title, 32, 6, 4210752, false);
		gfx.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
	}

	@Override
	protected void renderBg(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
		super.renderDefaultBg(gfx, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.getProgress() > 0) {
			int i2 = this.getProgressBarScaled();
			gfx.blit(BACKGROUND, x + 92, y + 48, 194, 0, i2 + 1, 16);
		}

		var recipe = this.getSelectedRecipe();

		if (recipe != null) {
			var itemRenderer = Minecraft.getInstance().getItemRenderer();

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int index = (i * 3) + j;
					var item = recipe.getStackInSlot(index);

					GhostItemRenderer.renderItemIntoGui(item, x + 33 + (j * 18), y + 30 + (i * 18), itemRenderer);
				}
			}

			var output = recipe.getStackInSlot(recipe.getSlots() - 1);

			GhostItemRenderer.renderItemIntoGui(output, x + 127, y + 48, itemRenderer);
		}
	}

	private void onSelectButtonTooltip(Button button, GuiGraphics gfx, int mouseX, int mouseY) {
		var index = ((RecipeSelectButton) button).getIndex();
		var isSelected = ((RecipeSelectButton) button).isSelected();
		var recipe = this.getRecipeInfo(index);

		if (recipe != null) {
			List<Component> tooltip;
			var hasRecipe = !recipe.getStacks().stream().allMatch(ItemStack::isEmpty);

			if (hasRecipe) {
				var output = recipe.getStackInSlot(recipe.getSlots() - 1);

				tooltip = Lists.newArrayList(
						Component.literal(output.getCount() + "x " + output.getHoverName().getString()),
						Component.literal(""),
						ModTooltips.AUTO_TABLE_DELETE_RECIPE.color(ChatFormatting.WHITE).build()
				);

				if (isSelected) {
					tooltip.add(1, ModTooltips.SELECTED.color(ChatFormatting.GREEN).build());
				}
			} else {
				tooltip = Lists.newArrayList(
						ModTooltips.AUTO_TABLE_SAVE_RECIPE.color(ChatFormatting.WHITE).build()
				);

				if (isSelected) {
					tooltip.add(0, ModTooltips.SELECTED.color(ChatFormatting.GREEN).build());
					tooltip.add(1, Component.literal(""));
				}
			}

			gfx.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
		}
	}

	private AutoFluxCrafterTileEntity getTileEntity() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var tile = level.getBlockEntity(this.getMenu().getBlockPos());

			if (tile instanceof AutoFluxCrafterTileEntity crafter)
				return crafter;
		}

		return null;
	}

	private BaseItemStackHandler getRecipeInfo(int selected) {
		if (this.tile == null)
			return null;

		return this.tile.getRecipeStorage().getRecipe(selected);
	}

	private BaseItemStackHandler getSelectedRecipe() {
		if (this.tile == null)
			return null;

		return this.tile.getRecipeStorage().getSelectedRecipe();
	}

	private int getProgress() {
		if (this.tile == null)
			return 0;

		return this.tile.getProgress();
	}

	private int getProgressRequired() {
		if (this.tile == null)
			return 0;

		return this.tile.getProgressRequired();
	}

	private int getProgressBarScaled() {
		int i = this.getProgress();
		int j = Math.max(this.getProgressRequired(), i);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}
}