package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.render.GhostItemRenderer;
import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.RecipeSelectButton;
import com.blakebr0.extendedcrafting.client.screen.button.ToggleTableRunningButton;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class AdvancedAutoTableScreen extends BaseContainerScreen<AdvancedAutoTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/advanced_auto_table.png");
	private final RecipeSelectButton[] recipeSelectButtons = new RecipeSelectButton[3];
	private AutoTableTileEntity tile;

	public AdvancedAutoTableScreen(AdvancedAutoTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title, BACKGROUND, 186, 206);
	}

	@Override
	public void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		BlockPos pos = this.getContainer().getPos();

		this.addButton(new ToggleTableRunningButton(x + 155, y + 62, pos, this::isRunning));

		this.recipeSelectButtons[0] = this.addButton(new RecipeSelectButton(x + 142, y + 7, pos, 0, this::isRecipeSelected));
		this.recipeSelectButtons[1] = this.addButton(new RecipeSelectButton(x + 155, y + 7, pos, 1, this::isRecipeSelected));
		this.recipeSelectButtons[2] = this.addButton(new RecipeSelectButton(x + 168, y + 7, pos, 2, this::isRecipeSelected));

		this.tile = this.getTileEntity();
	}

	@Override
	protected void renderHoveredTooltip(MatrixStack stack, int mouseX, int mouseY) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		super.renderHoveredTooltip(stack, mouseX, mouseY);

		if (mouseX > x + 7 && mouseX < x + 20 && mouseY > y + 23 && mouseY < y + 100) {
			StringTextComponent text = new StringTextComponent(number(this.getEnergyStored()) + " FE");
			this.renderTooltip(stack, text, mouseX, mouseY);
		}

		if (mouseX > x + 155 && mouseX < x + 168 && mouseY > y + 62 && mouseY < y + 78) {
			this.renderTooltip(stack, ModTooltips.TOGGLE_AUTO_CRAFTING.color(TextFormatting.WHITE).build(), mouseX, mouseY);
		}

		for (RecipeSelectButton button : this.recipeSelectButtons) {
			if (button.isHovered()) {
				BaseItemStackHandler recipe = this.getRecipeInfo(button.getIndex());
				if (recipe != null) {
					List<ITextComponent> tooltip;
					boolean hasRecipe = !recipe.getStacks().stream().allMatch(ItemStack::isEmpty);
					if (hasRecipe) {
						ItemStack output = recipe.getStackInSlot(recipe.getSlots() - 1);
						tooltip = Lists.newArrayList(
								new StringTextComponent(output.getCount() + "x " + output.getDisplayName().getString()),
								new StringTextComponent(""),
								ModTooltips.AUTO_TABLE_DELETE_RECIPE.color(TextFormatting.WHITE).build()
						);
					} else {
						tooltip = Lists.newArrayList(
								ModTooltips.AUTO_TABLE_SAVE_RECIPE.color(TextFormatting.WHITE).build()
						);
					}

					this.func_243308_b(stack, tooltip, mouseX, mouseY);
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.drawString(stack, title, 25.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getString();
		this.font.drawString(stack, inventory, 13.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(stack, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		int i1 = this.getEnergyBarScaled();
		this.blit(stack, x + 7, y + 101 - i1, 188, 78 - i1, 15, i1 + 1);

		if (this.isRunning()) {
			int i2 = this.getProgressBarScaled();
			this.blit(stack, x + 154, y + 61, 204, 0, 13, i2);
		}

		BaseItemStackHandler recipe = this.getSelectedRecipe();
		if (recipe != null) {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					int index = (i * 5) + j;
					ItemStack item = recipe.getStackInSlot(index);
					GhostItemRenderer.renderItemIntoGui(item, x + 26 + (j * 18), y + 18 + (i * 18), this.itemRenderer);
				}
			}

			ItemStack output = recipe.getStackInSlot(recipe.getSlots() - 1);
			GhostItemRenderer.renderItemIntoGui(output, x + 154, y + 37, this.itemRenderer);
		}
	}

	private boolean isRecipeSelected(int index) {
		return index == this.getSelected();
	}

	private AutoTableTileEntity getTileEntity() {
		ClientWorld world = this.getMinecraft().world;

		if (world != null) {
			TileEntity tile = world.getTileEntity(this.getContainer().getPos());

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