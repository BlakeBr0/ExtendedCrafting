package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.render.GhostItemRenderer;
import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.client.screen.widget.EnergyBarWidget;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.RecipeSelectButton;
import com.blakebr0.extendedcrafting.client.screen.button.ToggleTableRunningButton;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AdvancedAutoTableScreen extends BaseContainerScreen<AdvancedAutoTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/advanced_auto_table.png");
	private AutoTableTileEntity tile;

	public AdvancedAutoTableScreen(AdvancedAutoTableContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 186, 206);
	}

	@Override
	public void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		var pos = this.getMenu().getPos();

		this.addRenderableWidget(new ToggleTableRunningButton(x + 155, y + 62, pos, this::isRunning));

		this.tile = this.getTileEntity();

		if (this.tile != null) {
			this.addRenderableWidget(new RecipeSelectButton(x + 142, y + 7, pos, 0, this.tile.getRecipeStorage(), this::onSelectButtonTooltip));
			this.addRenderableWidget(new RecipeSelectButton(x + 155, y + 7, pos, 1, this.tile.getRecipeStorage(), this::onSelectButtonTooltip));
			this.addRenderableWidget(new RecipeSelectButton(x + 168, y + 7, pos, 2, this.tile.getRecipeStorage(), this::onSelectButtonTooltip));

			this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 23, this.tile.getEnergy(), this));
		}
	}

	@Override
	protected void renderTooltip(PoseStack stack, int mouseX, int mouseY) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		super.renderTooltip(stack, mouseX, mouseY);

		if (mouseX > x + 155 && mouseX < x + 168 && mouseY > y + 62 && mouseY < y + 78) {
			this.renderTooltip(stack, ModTooltips.TOGGLE_AUTO_CRAFTING.color(ChatFormatting.WHITE).build(), mouseX, mouseY);
		}
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		this.font.draw(stack, title, 25.0F, 6.0F, 4210752);
		this.font.draw(stack, this.playerInventoryTitle, 13.0F, this.imageHeight - 94.0F, 4210752);
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		super.renderDefaultBg(stack, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.isRunning()) {
			int i2 = this.getProgressBarScaled();
			this.blit(stack, x + 154, y + 61, 204, 0, 13, i2);
		}

		var recipe = this.getSelectedRecipe();

		if (recipe != null) {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					int index = (i * 5) + j;
					var item = recipe.getStackInSlot(index);

					GhostItemRenderer.renderItemIntoGui(item, x + 26 + (j * 18), y + 18 + (i * 18), this.itemRenderer);
				}
			}

			var output = recipe.getStackInSlot(recipe.getSlots() - 1);

			GhostItemRenderer.renderItemIntoGui(output, x + 154, y + 37, this.itemRenderer);
		}
	}

	private void onSelectButtonTooltip(Button button, PoseStack matrix, int mouseX, int mouseY) {
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

			this.renderComponentTooltip(matrix, tooltip, mouseX, mouseY);
		}
	}

	private AutoTableTileEntity getTileEntity() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var tile = level.getBlockEntity(this.getMenu().getPos());

			if (tile instanceof AutoTableTileEntity table)
				return table;
		}

		return null;
	}

	private boolean isRunning() {
		if (this.tile == null)
			return false;

		return this.tile.isRunning();
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

	private int getSelected() {
		if (this.tile == null)
			return 0;

		return this.tile.getRecipeStorage().getSelected();
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
		int j = this.getProgressRequired();
		return j != 0 && i != 0 ? i * 16 / j : 0;
	}
}