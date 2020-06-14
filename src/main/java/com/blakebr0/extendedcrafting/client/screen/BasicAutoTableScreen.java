package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.RecipeSelectButton;
import com.blakebr0.extendedcrafting.client.screen.button.ToggleTableRunningButton;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
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

public class BasicAutoTableScreen extends ContainerScreen<BasicAutoTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/basic_auto_table.png");
	private final RecipeSelectButton[] recipeSelectButtons = new RecipeSelectButton[3];

	public BasicAutoTableScreen(BasicAutoTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 176;
		this.ySize = 194;
	}

	@Override
	public void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		BlockPos pos = this.getContainer().getPos();

		this.addButton(new ToggleTableRunningButton(x + 129, y + 58, pos));
		this.recipeSelectButtons[0] = this.addButton(new RecipeSelectButton(x + 132, y + 7, pos, 0));
		this.recipeSelectButtons[1] = this.addButton(new RecipeSelectButton(x + 145, y + 7, pos, 1));
		this.recipeSelectButtons[2] = this.addButton(new RecipeSelectButton(x + 158, y + 7, pos, 2));
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
		BasicAutoTableContainer container = this.getContainer();

		super.renderHoveredToolTip(mouseX, mouseY);

		if (mouseX > x + 7 && mouseX < x + 20 && mouseY > y + 17 && mouseY < y + 94) {
			this.renderTooltip(container.getEnergyStored() + " FE", mouseX, mouseY);
		}

		if (mouseX > x + 129 && mouseX < x + 142 && mouseY > y + 58 && mouseY < y + 73) {
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
								output.getDisplayName().getFormattedText(),
								"",
								"Shift + Left Click to delete this recipe."
						);
					} else {
						tooltip = Lists.newArrayList(
								"Shift + left Click to save this recipe."
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
		this.font.drawString(title, 32.0F, 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getFormattedText();
		this.font.drawString(inventory, 8.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		BasicAutoTableContainer container = this.getContainer();

		this.blit(x, y, 0, 0, this.xSize, this.ySize);

		int i1 = container.getEnergyBarScaled(78);
		this.blit(x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);

		if (container.isRunning()) {
			int i2 = container.getProgressBarScaled(16);
			this.blit(x + 129, y + 58, 194, 0, 13, i2);
		} else {
			this.blit(x + 130, y + 60, 194, 18, 13, 13);
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
			BasicAutoTableContainer container = this.getContainer();
			TileEntity tile = world.getTileEntity(container.getPos());
			if (tile instanceof AutoTableTileEntity) {
				AutoTableTileEntity table = (AutoTableTileEntity) tile;
				return table.getRecipeStorage().getRecipe(selected);
			}
		}

		return null;
	}
}