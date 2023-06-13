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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;

public class CompressorScreen extends BaseContainerScreen<CompressorContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/compressor.png");
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

		this.addRenderableWidget(new EjectModeSwitchButton(x + 69, y + 30, pos));
		this.addRenderableWidget(new InputLimitSwitchButton(x + 91, y + 74, pos, this::isLimitingInput));

		this.tile = this.getTileEntity();

		if (this.tile != null) {
			this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 17, this.tile.getEnergy()));
		}
	}

	@Override
	public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		super.render(gfx, mouseX, mouseY, partialTicks);

		if (mouseX > x + 60 && mouseX < x + 85 && mouseY > y + 74 && mouseY < y + 83) {
			var tooltip = new ArrayList<Component>();

			if (this.getMaterialCount() < 1) {
				tooltip.add(ModTooltips.EMPTY.color(ChatFormatting.WHITE).build());
			} else {
				var text = Component.literal(number(this.getMaterialCount()) + " / " + number(this.getMaterialsRequired()));

				tooltip.add(text);

				if (this.hasMaterialStack()) {
					var inputs = this.tile.getInputs();
					var size = inputs.size();
					for (int i = 0; i < size && i < 5; i++) {
						tooltip.add(inputs.get(i).getDisplayName());
					}

					if (size > 5) {
						tooltip.add(ModTooltips.AND_X_MORE.args(size - 5).build());
					}
				}
			}

			gfx.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
		}

		if (mouseX > x + 68 && mouseX < x + 79 && mouseY > y + 28 && mouseY < y + 39) {
			if (this.isEjecting()) {
				gfx.renderTooltip(this.font, ModTooltips.EJECTING.color(ChatFormatting.WHITE).build(), mouseX, mouseY);
			} else {
				gfx.renderTooltip(this.font, ModTooltips.EJECT.color(ChatFormatting.WHITE).build(), mouseX, mouseY);
			}
		}

		if (mouseX > x + 90 && mouseX < x + 98 && mouseY > y + 73 && mouseY < y + 84) {
			if (this.isLimitingInput()) {
				gfx.renderTooltip(this.font, ModTooltips.LIMITED_INPUT.color(ChatFormatting.WHITE).build(), mouseX, mouseY);
			} else {
				gfx.renderTooltip(this.font, ModTooltips.UNLIMITED_INPUT.color(ChatFormatting.WHITE).build(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		gfx.drawString(this.font, title, (this.imageWidth / 2 - this.font.width(title) / 2), 6, 4210752, false);
		gfx.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
	}

	@Override
	protected void renderBg(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
		super.renderDefaultBg(gfx, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		if (this.hasRecipe()) {
			if (this.getMaterialCount() > 0 && this.getMaterialsRequired() > 0) {
				int i2 = this.getMaterialBarScaled(26);
				gfx.blit(BACKGROUND, x + 60, y + 74, 194, 19, i2 + 1, 10);
			}

			if (this.getProgress() > 0 && this.getEnergyRequired() > 0) {
				int i2 = this.getProgressBarScaled(24);
				gfx.blit(BACKGROUND, x + 96, y + 47, 194, 0, i2 + 1, 16);
			}
		}

		if (this.isLimitingInput()) {
			gfx.blit(BACKGROUND, x + 90, y + 74, 203, 56, 9, 10);
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

	public boolean isEjecting() {
		if (this.tile == null)
			return false;

		return this.tile.isEjecting();
	}

	public boolean isLimitingInput() {
		if (this.tile == null)
			return false;

		return this.tile.isLimitingInput();
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

	public int getProgress() {
		if (this.tile == null)
			return 0;

		return this.tile.getProgress();
	}

	public int getMaterialCount() {
		if (this.tile == null)
			return 0;

		return this.tile.getMaterialCount();
	}

	public int getEnergyRequired() {
		if (this.tile == null)
			return 0;

		return this.tile.getEnergyRequired();
	}

	public int getMaterialsRequired() {
		if (this.tile == null)
			return 0;

		return this.tile.getMaterialsRequired();
	}

	public int getMaterialBarScaled(int pixels) {
		int i = Mth.clamp(this.getMaterialCount(), 0, this.getMaterialsRequired());
		int j = this.getMaterialsRequired();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	public int getProgressBarScaled(int pixels) {
		int i = this.getProgress();
		int j = this.getEnergyRequired();
		return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
	}
}
