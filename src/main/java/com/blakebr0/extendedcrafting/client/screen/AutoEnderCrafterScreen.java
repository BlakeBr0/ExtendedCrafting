package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.render.GhostItemRenderer;
import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.client.screen.widget.EnergyBarWidget;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.RecipeSelectButton;
import com.blakebr0.extendedcrafting.container.AutoEnderCrafterContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AutoEnderCrafterTileEntity;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AutoEnderCrafterScreen extends BaseContainerScreen<AutoEnderCrafterContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/auto_ender_crafter.png");
	private AutoEnderCrafterTileEntity tile;

	public AutoEnderCrafterScreen(AutoEnderCrafterContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 194);
	}

	@Override
	protected void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		var pos = this.getMenu().getPos();

		this.tile = this.getTileEntity();

		if (this.tile != null) {
			this.addRenderableWidget(new RecipeSelectButton(x + 132, y + 7, pos, 0,  33, this.tile.getRecipeStorage(), this::onSelectButtonTooltip));
			this.addRenderableWidget(new RecipeSelectButton(x + 145, y + 7, pos, 1,  33, this.tile.getRecipeStorage(), this::onSelectButtonTooltip));
			this.addRenderableWidget(new RecipeSelectButton(x + 158, y + 7, pos, 2,  33, this.tile.getRecipeStorage(), this::onSelectButtonTooltip));

			this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 17, this.tile.getEnergy(), this));
		}
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		this.font.draw(stack, title, 32.0F, 6.0F, 4210752);
		this.font.draw(stack, this.playerInventoryTitle, 8.0F, this.imageHeight - 94.0F, 4210752);
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		super.renderDefaultBg(stack, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.getProgress() > 0) {
			int i2 = this.getProgressBarScaled();
			this.blit(stack, x + 92, y + 48, 194, 0, i2 + 1, 16);
		}

		var recipe = this.getSelectedRecipe();

		if (recipe != null) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int index = (i * 3) + j;
					var item = recipe.getStackInSlot(index);

					GhostItemRenderer.renderItemIntoGui(item, x + 33 + (j * 18), y + 30 + (i * 18), this.itemRenderer);
				}
			}

			var output = recipe.getStackInSlot(recipe.getSlots() - 1);

			GhostItemRenderer.renderItemIntoGui(output, x + 127, y + 48, this.itemRenderer);
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

	private AutoEnderCrafterTileEntity getTileEntity() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var tile = level.getBlockEntity(this.getMenu().getPos());

			if (tile instanceof AutoEnderCrafterTileEntity crafter)
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