package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.button.EjectModeSwitchButton;
import com.blakebr0.extendedcrafting.client.screen.button.InputLimitSwitchButton;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class CompressorScreen extends BaseContainerScreen<CompressorContainer> {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/compressor.png");
	private CompressorTileEntity tile;

	public CompressorScreen(CompressorContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title, BACKGROUND, 176, 194);
	}

	@Override
	public void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		BlockPos pos = this.getMenu().getPos();

		this.addButton(new EjectModeSwitchButton(x + 69, y + 30, pos));
		this.addButton(new InputLimitSwitchButton(x + 91, y + 74, pos, this::isLimitingInput));

		this.tile = this.getTileEntity();
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		int x = this.getGuiLeft();
		int y = this.getGuiTop();

		super.render(stack, mouseX, mouseY, partialTicks);

		if (mouseX > x + 7 && mouseX < x + 20 && mouseY > y + 17 && mouseY < y + 94) {
			StringTextComponent text = new StringTextComponent(number(this.getEnergyStored()) + " / " + number(this.getMaxEnergyStored()) + " FE");
			this.renderTooltip(stack, text, mouseX, mouseY);
		}

		if (mouseX > x + 60 && mouseX < x + 85 && mouseY > y + 74 && mouseY < y + 83) {
			List<ITextComponent> tooltip = new ArrayList<>();
			if (this.getMaterialCount() < 1) {
				tooltip.add(ModTooltips.EMPTY.color(TextFormatting.WHITE).build());
			} else {
				if (this.hasMaterialStack()) {
					tooltip.add(this.getMaterialStackDisplayName());
				}

				StringTextComponent text = new StringTextComponent(number(this.getMaterialCount()) + " / " + number(this.getMaterialsRequired()));
				tooltip.add(text);
			}

			this.renderComponentTooltip(stack, tooltip, mouseX, mouseY);
		}

		if (mouseX > x + 68 && mouseX < x + 79 && mouseY > y + 28 && mouseY < y + 39) {
			if (this.isEjecting()) {
				this.renderTooltip(stack, ModTooltips.EJECTING.color(TextFormatting.WHITE).build(), mouseX, mouseY);
			} else {
				this.renderTooltip(stack, ModTooltips.EJECT.color(TextFormatting.WHITE).build(), mouseX, mouseY);
			}
		}

		if (mouseX > x + 90 && mouseX < x + 98 && mouseY > y + 73 && mouseY < y + 84) {
			if (this.isLimitingInput()) {
				this.renderTooltip(stack, ModTooltips.LIMITED_INPUT.color(TextFormatting.WHITE).build(), mouseX, mouseY);
			} else {
				this.renderTooltip(stack, ModTooltips.UNLIMITED_INPUT.color(TextFormatting.WHITE).build(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.draw(stack, title, (float) (this.imageWidth / 2 - this.font.width(title) / 2), 6.0F, 4210752);
		String inventory = this.inventory.getDisplayName().getString();
		this.font.draw(stack, inventory, 8.0F, this.imageHeight - 94.0F, 4210752);
	}

	@Override
	protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(stack, partialTicks, mouseX, mouseY);

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

	private ITextComponent getMaterialStackDisplayName() {
		ClientWorld world = this.getMinecraft().level;
		if (world != null) {
			CompressorContainer container = this.getMenu();
			TileEntity tile = world.getBlockEntity(container.getPos());
			if (tile instanceof CompressorTileEntity) {
				CompressorTileEntity compressor = (CompressorTileEntity) tile;
				ItemStack materialStack = compressor.getMaterialStack();

				return materialStack.getHoverName();
			}
		}

		return new StringTextComponent("");
	}

	private CompressorTileEntity getTileEntity() {
		ClientWorld world = this.getMinecraft().level;

		if (world != null) {
			TileEntity tile = world.getBlockEntity(this.getMenu().getPos());

			if (tile instanceof CompressorTileEntity) {
				return (CompressorTileEntity) tile;
			}
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
		int i = MathHelper.clamp(this.getMaterialCount(), 0, this.getMaterialsRequired());
		int j = this.getMaterialsRequired();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	public int getProgressBarScaled(int pixels) {
		int i = this.getProgress();
		int j = this.getEnergyRequired();
		return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
	}
}
