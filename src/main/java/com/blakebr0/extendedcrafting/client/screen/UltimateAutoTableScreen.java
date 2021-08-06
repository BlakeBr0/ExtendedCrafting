package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.render.GhostItemRenderer;
import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.RecipeSelectButton;
import com.blakebr0.extendedcrafting.client.screen.button.ToggleTableRunningButton;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class UltimateAutoTableScreen extends BaseContainerScreen<UltimateAutoTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/ultimate_auto_table.png");
	private final RecipeSelectButton[] recipeSelectButtons = new RecipeSelectButton[3];
	private AutoTableTileEntity tile;

	public UltimateAutoTableScreen(UltimateAutoTableContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 254, 278, 512, 512);
	}

	@Override
	public void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		BlockPos pos = this.getMenu().getPos();

		this.addButton(new ToggleTableRunningButton(x + 226, y + 114, pos, this::isRunning));

		this.recipeSelectButtons[0] = this.addButton(new RecipeSelectButton(x + 210, y + 7, pos, 0, this::isRecipeSelected));
		this.recipeSelectButtons[1] = this.addButton(new RecipeSelectButton(x + 223, y + 7, pos, 1, this::isRecipeSelected));
		this.recipeSelectButtons[2] = this.addButton(new RecipeSelectButton(x + 236, y + 7, pos, 2, this::isRecipeSelected));

		this.tile = this.getTileEntity();
	}

	@Override
	protected void renderTooltip(PoseStack stack, int mouseX, int mouseY) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		super.renderTooltip(stack, mouseX, mouseY);

		if (mouseX > x + 7 && mouseX < x + 20 && mouseY > y + 59 && mouseY < y + 136) {
			TextComponent text = new TextComponent(number(this.getEnergyStored()) + " / " + number(this.getMaxEnergyStored()) + " FE");
			this.renderTooltip(stack, text, mouseX, mouseY);
		}

		if (mouseX > x + 226 && mouseX < x + 239 && mouseY > y + 113 && mouseY < y + 129) {
			this.renderTooltip(stack, ModTooltips.TOGGLE_AUTO_CRAFTING.color(ChatFormatting.WHITE).build(), mouseX, mouseY);
		}

		for (RecipeSelectButton button : this.recipeSelectButtons) {
			if (button.isHovered()) {
				BaseItemStackHandler recipe = this.getRecipeInfo(button.getIndex());
				if (recipe != null) {
					List<Component> tooltip;
					boolean hasRecipe = !recipe.getStacks().stream().allMatch(ItemStack::isEmpty);
					if (hasRecipe) {
						ItemStack output = recipe.getStackInSlot(recipe.getSlots() - 1);
						tooltip = Lists.newArrayList(
								new TextComponent(output.getCount() + "x " + output.getHoverName().getString()),
								new TextComponent(""),
								ModTooltips.AUTO_TABLE_DELETE_RECIPE.color(ChatFormatting.WHITE).build()
						);
					} else {
						tooltip = Lists.newArrayList(
								ModTooltips.AUTO_TABLE_SAVE_RECIPE.color(ChatFormatting.WHITE).build()
						);
					}

					this.renderComponentTooltip(stack, tooltip, mouseX, mouseY);
				}
			}
		}
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.draw(stack, title, 26.0F, 6.0F, 4210752);
		String inventory = this.inventory.getDisplayName().getString();
		this.font.draw(stack, inventory, 47.0F, this.imageHeight - 94.0F, 4210752);
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(stack, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		int i1 = this.getEnergyBarScaled();
		blit(stack, x + 7, y + 137 - i1, 256, 78 - i1, 15, i1 + 1, 512, 512);

		if (this.isRunning()) {
			int i2 = this.getProgressBarScaled();
			blit(stack, x + 225, y + 113, 272, 0, 13, i2, 512, 512);
		}

		BaseItemStackHandler recipe = this.getSelectedRecipe();
		if (recipe != null) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					int index = (i * 9) + j;
					ItemStack item = recipe.getStackInSlot(index);
					GhostItemRenderer.renderItemIntoGui(item, x + 27 + (j * 18), y + 18 + (i * 18), this.itemRenderer);
				}
			}

			ItemStack output = recipe.getStackInSlot(recipe.getSlots() - 1);
			GhostItemRenderer.renderItemIntoGui(output, x + 225, y + 89, this.itemRenderer);
		}
	}

	private boolean isRecipeSelected(int index) {
		return index == this.getSelected();
	}

	private AutoTableTileEntity getTileEntity() {
		ClientLevel world = this.getMinecraft().level;

		if (world != null) {
			BlockEntity tile = world.getBlockEntity(this.getMenu().getPos());

			if (tile instanceof AutoTableTileEntity) {
				return (AutoTableTileEntity) tile;
			}
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

	private int getEnergyBarScaled() {
		int i = this.getEnergyStored();
		int j = this.getMaxEnergyStored();
		return (int) (j != 0 && i != 0 ? (long) i * 78 / j : 0);
	}

	private int getProgressBarScaled() {
		int i = this.getProgress();
		int j = this.getProgressRequired();
		return j != 0 && i != 0 ? i * 16 / j : 0;
	}
}