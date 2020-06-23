package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.render.GhostItemRenderer;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.RecipeSelectButton;
import com.blakebr0.extendedcrafting.client.screen.button.ToggleTableRunningButton;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class EliteAutoTableScreen extends ContainerScreen<EliteAutoTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/elite_auto_table.png");
	private final RecipeSelectButton[] recipeSelectButtons = new RecipeSelectButton[3];

	public EliteAutoTableScreen(EliteAutoTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 220;
		this.ySize = 242;
	}

	@Override
	public void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		BlockPos pos = this.getContainer().getPos();

		this.addButton(new ToggleTableRunningButton(x + 192, y + 95, pos));
		this.recipeSelectButtons[0] = this.addButton(new RecipeSelectButton(x + 176, y + 7, pos, 0));
		this.recipeSelectButtons[1] = this.addButton(new RecipeSelectButton(x + 189, y + 7, pos, 1));
		this.recipeSelectButtons[2] = this.addButton(new RecipeSelectButton(x + 202, y + 7, pos, 2));
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int selected = this.getContainer().getSelected();
		this.updateSelectedRecipeButtons(selected);

		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		EliteAutoTableContainer container = this.getContainer();

		super.renderHoveredToolTip(mouseX, mouseY);

		if (mouseX > x + 7 && mouseX < x + 20 && mouseY > y + 41 && mouseY < y + 118) {
			this.renderTooltip(container.getEnergyStored() + " FE", mouseX, mouseY);
		}

		if (mouseX > x + 192 && mouseX < x + 205 && mouseY > y + 95 && mouseY < y + 111) {
			this.renderTooltip(ModTooltips.TOGGLE_AUTO_CRAFTING.color(TextFormatting.WHITE).buildString(), mouseX, mouseY);
		}

		for (RecipeSelectButton button : this.recipeSelectButtons) {
			if (button.isHovered()) {
				BaseItemStackHandler recipe = this.getRecipeInfo(button.selected);
				if (recipe != null) {
					List<String> tooltip;
					boolean hasRecipe = !recipe.getStacks().stream().allMatch(ItemStack::isEmpty);
					if (hasRecipe) {
						ItemStack output = recipe.getStackInSlot(recipe.getSlots() - 1);
						tooltip = Lists.newArrayList(
								output.getCount() + "x " + output.getDisplayName().getFormattedText(),
								"",
								"Shift + Left Click to delete this recipe."
						);
					} else {
						tooltip = Lists.newArrayList(
								"Shift + Left Click to save this recipe."
						);
					}

					this.renderTooltip(tooltip, mouseX, mouseY);
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = this.getTitle().getFormattedText();
		this.font.drawString(title, 26.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getFormattedText();
		this.font.drawString(inventory, 30.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		EliteAutoTableContainer container = this.getContainer();

		this.blit(x, y, 0, 0, this.xSize, this.ySize);

		int i1 = container.getEnergyBarScaled(78);
		this.blit(x + 7, y + 119 - i1, 222, 78 - i1, 15, i1 + 1);

		if (container.isRunning()) {
			int i2 = container.getProgressBarScaled(16);
			this.blit(x + 191, y + 95, 238, 0, 13, i2);
		} else {
			this.blit(x + 192, y + 97, 238, 18, 13, 13);
		}

		BaseItemStackHandler recipe = this.getSelectedRecipe();
		if (recipe != null) {
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					int index = (i * 7) + j;
					ItemStack stack = recipe.getStackInSlot(index);
					GhostItemRenderer.renderItemIntoGui(stack, x + 27 + (j * 18), y + 18 + (i * 18), this.itemRenderer);
				}
			}
		}
	}

	private void updateSelectedRecipeButtons(int selected) {
		for (RecipeSelectButton button : this.recipeSelectButtons) {
			button.active = button.selected == selected;
		}
	}

	private BaseItemStackHandler getRecipeInfo(int selected) {
		ClientWorld world = this.getMinecraft().world;
		if (world != null) {
			EliteAutoTableContainer container = this.getContainer();
			TileEntity tile = world.getTileEntity(container.getPos());
			if (tile instanceof AutoTableTileEntity) {
				AutoTableTileEntity table = (AutoTableTileEntity) tile;
				return table.getRecipeStorage().getRecipe(selected);
			}
		}

		return null;
	}

	private BaseItemStackHandler getSelectedRecipe() {
		ClientWorld world = this.getMinecraft().world;
		if (world != null) {
			EliteAutoTableContainer container = this.getContainer();
			TileEntity tile = world.getTileEntity(container.getPos());
			if (tile instanceof AutoTableTileEntity) {
				AutoTableTileEntity table = (AutoTableTileEntity) tile;
				return table.getRecipeStorage().getSelectedRecipe();
			}
		}

		return null;
	}
}