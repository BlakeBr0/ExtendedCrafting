package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.client.screen.widget.EnergyBarWidget;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.EjectModeSwitchButton;
import com.blakebr0.extendedcrafting.client.screen.button.InputLimitSwitchButton;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.Optional;

public class CompressorScreen extends BaseContainerScreen<CompressorContainer> {
	public static final Identifier BACKGROUND = ExtendedCrafting.resource("textures/gui/compressor.png");
	private CompressorTileEntity tile;

	public CompressorScreen(CompressorContainer container, Inventory inventory, Component title) {
		super(container, inventory, title, BACKGROUND, 176, 194);
	}

	@Override
	public void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		var pos = this.getMenu().getBlockPos();

		this.tile = this.getTileEntity();

		this.addRenderableWidget(new EjectModeSwitchButton(x + 69, y + 30, pos));
		this.addRenderableWidget(new InputLimitSwitchButton(x + 91, y + 74, pos, this.menu::isLimitingInput));
		this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 17, this.menu::getEnergyStored, this.menu::getMaxEnergyCapacity));
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		gfx.text(this.font, this.title, (this.imageWidth / 2 - this.font.width(title) / 2), 6, -12566464, false);
		gfx.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, -12566464, false);
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (mouseX > x + 60 && mouseX < x + 85 && mouseY > y + 74 && mouseY < y + 83) {
			var tooltip = new ArrayList<Component>();

			if (this.menu.getMaterialCount() < 1) {
				tooltip.add(ModTooltips.EMPTY.color(ChatFormatting.WHITE).toComponent());
			} else {
				var text = Component.literal(number(this.menu.getMaterialCount()) + " / " + number(this.menu.getMaterialsRequired()));

				tooltip.add(text);

				if (this.hasMaterialStack()) {
					var inputs = this.tile.getInputs();
					var size = inputs.size();
					for (int i = 0; i < size && i < 5; i++) {
						tooltip.add(inputs.get(i).getDisplayName());
					}

					if (size > 5) {
						tooltip.add(ModTooltips.AND_X_MORE.args(size - 5).toComponent());
					}
				}
			}

			gfx.setTooltipForNextFrame(this.font, tooltip, Optional.empty(), mouseX, mouseY);
		}

		if (mouseX > x + 68 && mouseX < x + 79 && mouseY > y + 28 && mouseY < y + 39) {
			if (this.menu.isEjecting()) {
				gfx.setTooltipForNextFrame(this.font, ModTooltips.EJECTING.color(ChatFormatting.WHITE).toComponent(), mouseX, mouseY);
			} else {
				gfx.setTooltipForNextFrame(this.font, ModTooltips.EJECT.color(ChatFormatting.WHITE).toComponent(), mouseX, mouseY);
			}
		}

		if (mouseX > x + 90 && mouseX < x + 98 && mouseY > y + 73 && mouseY < y + 84) {
			if (this.menu.isLimitingInput()) {
				gfx.setTooltipForNextFrame(this.font, ModTooltips.LIMITED_INPUT.color(ChatFormatting.WHITE).toComponent(), mouseX, mouseY);
			} else {
				gfx.setTooltipForNextFrame(this.font, ModTooltips.UNLIMITED_INPUT.color(ChatFormatting.WHITE).toComponent(), mouseX, mouseY);
			}
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float a) {
		super.extractBackground(gfx, mouseX, mouseY, a);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.hasRecipe()) {
			if (this.menu.getMaterialCount() > 0 && this.menu.getMaterialsRequired() > 0) {
				int i2 = this.getMaterialBarScaled(26);
				gfx.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x + 60, y + 74, 194, 19, i2 + 1, 10, 256, 256);
			}

			if (this.menu.getProgress() > 0 && this.menu.getEnergyRequired() > 0) {
				int i2 = this.getProgressBarScaled(24);
				gfx.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x + 96, y + 47, 194, 0, i2 + 1, 16, 256, 256);
			}
		}

		if (this.menu.isLimitingInput()) {
			gfx.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x + 90, y + 74, 203, 56, 9, 10, 256, 256);
		}
	}

	private CompressorTileEntity getTileEntity() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var tile = level.getBlockEntity(this.getMenu().getBlockPos());

			if (tile instanceof CompressorTileEntity compressor)
				return compressor;
		}

		return null;
	}

	public boolean hasRecipe() {
		if (this.tile == null)
			return false;

		return this.tile.hasRecipe();
	}

	public boolean hasMaterialStack() {
		if (this.tile == null)
			return false;

		return this.tile.hasMaterialStack();
	}

	public int getMaterialBarScaled(int pixels) {
		int i = Mth.clamp(this.menu.getMaterialCount(), 0, this.menu.getMaterialsRequired());
		int j = this.menu.getMaterialsRequired();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	public int getProgressBarScaled(int pixels) {
		int i = this.menu.getProgress();
		int j = this.menu.getEnergyRequired();
		return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
	}
}
