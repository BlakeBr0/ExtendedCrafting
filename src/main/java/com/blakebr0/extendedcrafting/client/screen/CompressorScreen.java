package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.EjectModeSwitchButton;
import com.blakebr0.extendedcrafting.client.screen.button.InputLimitSwitchButton;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

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
		var pos = this.getMenu().getPos();

		this.addWidget(new EjectModeSwitchButton(x + 69, y + 30, pos));
		this.addWidget(new InputLimitSwitchButton(x + 91, y + 74, pos, this::isLimitingInput));

		this.tile = this.getTileEntity();
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		super.render(stack, mouseX, mouseY, partialTicks);

		if (mouseX > x + 7 && mouseX < x + 20 && mouseY > y + 17 && mouseY < y + 94) {
			var text = new TextComponent(number(this.getEnergyStored()) + " / " + number(this.getMaxEnergyStored()) + " FE");
			this.renderTooltip(stack, text, mouseX, mouseY);
		}

		if (mouseX > x + 60 && mouseX < x + 85 && mouseY > y + 74 && mouseY < y + 83) {
			List<Component> tooltip = new ArrayList<>();

			if (this.getMaterialCount() < 1) {
				tooltip.add(ModTooltips.EMPTY.color(ChatFormatting.WHITE).build());
			} else {
				if (this.hasMaterialStack()) {
					tooltip.add(this.getMaterialStackDisplayName());
				}

				var text = new TextComponent(number(this.getMaterialCount()) + " / " + number(this.getMaterialsRequired()));

				tooltip.add(text);
			}

			this.renderComponentTooltip(stack, tooltip, mouseX, mouseY);
		}

		if (mouseX > x + 68 && mouseX < x + 79 && mouseY > y + 28 && mouseY < y + 39) {
			if (this.isEjecting()) {
				this.renderTooltip(stack, ModTooltips.EJECTING.color(ChatFormatting.WHITE).build(), mouseX, mouseY);
			} else {
				this.renderTooltip(stack, ModTooltips.EJECT.color(ChatFormatting.WHITE).build(), mouseX, mouseY);
			}
		}

		if (mouseX > x + 90 && mouseX < x + 98 && mouseY > y + 73 && mouseY < y + 84) {
			if (this.isLimitingInput()) {
				this.renderTooltip(stack, ModTooltips.LIMITED_INPUT.color(ChatFormatting.WHITE).build(), mouseX, mouseY);
			} else {
				this.renderTooltip(stack, ModTooltips.UNLIMITED_INPUT.color(ChatFormatting.WHITE).build(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		var title = this.getTitle().getString();

		this.font.draw(stack, title, (float) (this.imageWidth / 2 - this.font.width(title) / 2), 6.0F, 4210752);
		this.font.draw(stack, this.playerInventoryTitle, 8.0F, this.imageHeight - 94.0F, 4210752);
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
//		super.renderBg(stack, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		int i1 = this.getEnergyBarScaled(78);

		this.blit(stack, x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);

		if (this.hasRecipe()) {
			if (this.getMaterialCount() > 0 && this.getMaterialsRequired() > 0) {
				int i2 = this.getMaterialBarScaled(26);
				this.blit(stack, x + 60, y + 74, 194, 19, i2 + 1, 10);
			}

			if (this.getProgress() > 0 && this.getEnergyRequired() > 0) {
				int i2 = this.getProgressBarScaled(24);
				this.blit(stack, x + 96, y + 47, 194, 0, i2 + 1, 16);
			}
		}

		if (this.isLimitingInput()) {
			this.blit(stack, x + 90, y + 74, 203, 56, 9, 10);
		}
	}

	private Component getMaterialStackDisplayName() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var container = this.getMenu();
			var tile = level.getBlockEntity(container.getPos());

			if (tile instanceof CompressorTileEntity compressor) {
				var materialStack = compressor.getMaterialStack();

				return materialStack.getHoverName();
			}
		}

		return new TextComponent("");
	}

	private CompressorTileEntity getTileEntity() {
		var level = this.getMinecraft().level;

		if (level != null) {
			var tile = level.getBlockEntity(this.getMenu().getPos());

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

	public int getEnergyStored() {
		if (this.tile == null)
			return 0;

		return this.tile.getEnergy().getEnergyStored();
	}

	public int getMaxEnergyStored() {
		if (this.tile == null)
			return 0;

		return this.tile.getEnergy().getMaxEnergyStored();
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

	public int getEnergyBarScaled(int pixels) {
		int i = this.getEnergyStored();
		int j = this.getMaxEnergyStored();
		return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
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
