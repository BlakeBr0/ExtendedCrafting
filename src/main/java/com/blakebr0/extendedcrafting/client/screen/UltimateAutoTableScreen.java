package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.helper.RenderHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.render.GhostItemRenderer;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.RecipeSelectButton;
import com.blakebr0.extendedcrafting.client.screen.button.ToggleTableRunningButton;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
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

public class UltimateAutoTableScreen extends ContainerScreen<UltimateAutoTableContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/ultimate_auto_table.png");
	private final RecipeSelectButton[] recipeSelectButtons = new RecipeSelectButton[3];

	public UltimateAutoTableScreen(UltimateAutoTableContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.xSize = 254;
		this.ySize = 278;
	}

	@Override
	public void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		BlockPos pos = this.getContainer().getPos();

		this.addButton(new ToggleTableRunningButton(x + 226, y + 113, pos));
		this.recipeSelectButtons[0] = this.addButton(new RecipeSelectButton(x + 210, y + 7, pos, 0));
		this.recipeSelectButtons[1] = this.addButton(new RecipeSelectButton(x + 223, y + 7, pos, 1));
		this.recipeSelectButtons[2] = this.addButton(new RecipeSelectButton(x + 236, y + 7, pos, 2));
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
		UltimateAutoTableContainer container = this.getContainer();

		super.renderHoveredToolTip(mouseX, mouseY);

		if (mouseX > x + 7 && mouseX < x + 20 && mouseY > y + 59 && mouseY < y + 136) {
			this.renderTooltip(container.getEnergyStored() + " FE", mouseX, mouseY);
		}

		if (mouseX > x + 226 && mouseX < x + 239 && mouseY > y + 113 && mouseY < y + 129) {
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
		this.font.drawString(inventory, 47.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		UltimateAutoTableContainer container = this.getContainer();

		RenderHelper.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize, 512, 512);

		int i1 = container.getEnergyBarScaled(78);
		RenderHelper.drawTexturedModalRect(x + 7, y + 137 - i1, 256, 78 - i1, 15, i1 + 1, 512, 512);

		if (container.isRunning()) {
			int i2 = container.getProgressBarScaled(16);
			RenderHelper.drawTexturedModalRect(x + 225, y + 113, 272, 0, 13, i2, 512, 512);
		} else {
			RenderHelper.drawTexturedModalRect(x + 226, y + 115, 272, 18, 13, 13, 512, 512);
		}

		BaseItemStackHandler recipe = this.getSelectedRecipe();
		if (recipe != null) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					int index = (i * 9) + j;
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
			UltimateAutoTableContainer container = this.getContainer();
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
			UltimateAutoTableContainer container = this.getContainer();
			TileEntity tile = world.getTileEntity(container.getPos());
			if (tile instanceof AutoTableTileEntity) {
				AutoTableTileEntity table = (AutoTableTileEntity) tile;
				return table.getRecipeStorage().getSelectedRecipe();
			}
		}

		return null;
	}
}