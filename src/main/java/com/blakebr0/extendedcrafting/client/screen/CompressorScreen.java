package com.blakebr0.extendedcrafting.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.EjectModeSwitchMessage;
import com.blakebr0.extendedcrafting.network.message.InputLimitSwitchMessage;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class CompressorScreen extends BaseContainerScreen<CompressorContainer> {
	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/compressor.png");

	public CompressorScreen(CompressorContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title, BACKGROUND, 176, 194);
	}

	@Override
	public void init() {
		super.init();

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		CompressorContainer container = this.getContainer();

		this.addButton(new Button(x + 69, y + 29, 11, 9, new StringTextComponent(""), button -> {
			NetworkHandler.INSTANCE.sendToServer(new EjectModeSwitchMessage(container.getPos()));
		}) {
			@Override
			public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {

			}
		});
		this.addButton(new Button(x + 91, y + 74, 7, 10, new StringTextComponent(""), button -> {
			NetworkHandler.INSTANCE.sendToServer(new InputLimitSwitchMessage(container.getPos()));
		}) {
			@Override
			public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {

			}
		});
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		int left = this.getGuiLeft();
		int top = this.getGuiTop();
		CompressorContainer container = this.getContainer();

		super.render(stack, mouseX, mouseY, partialTicks);

		if (mouseX > left + 7 && mouseX < left + 20 && mouseY > top + 17 && mouseY < top + 94) {
			StringTextComponent text = new StringTextComponent(container.getEnergyStored() + " FE");
			this.renderTooltip(stack, text, mouseX, mouseY);
		}

		if (mouseX > left + 60 && mouseX < left + 85 && mouseY > top + 74 && mouseY < top + 83) {
			List<ITextProperties> tooltip = new ArrayList<>();
			if (container.getMaterialCount() < 1) {
				tooltip.add(ModTooltips.EMPTY.color(TextFormatting.WHITE).build());
			} else {
				if (container.hasMaterialStack()) {
					tooltip.add(this.getMaterialStackDisplayName());
				}

				StringTextComponent text = new StringTextComponent(container.getMaterialCount() + " / " + container.getMaterialsRequired());
				tooltip.add(text);
			}

			this.renderTooltip(stack, tooltip, mouseX, mouseY);
		}

		if (mouseX > left + 68 && mouseX < left + 79 && mouseY > top + 28 && mouseY < top + 39) {
			if (container.isEjecting()) {
				this.renderTooltip(stack, ModTooltips.EJECTING.color(TextFormatting.WHITE).build(), mouseX, mouseY);
			} else {
				this.renderTooltip(stack, ModTooltips.EJECT.color(TextFormatting.WHITE).build(), mouseX, mouseY);
			}
		}

		if (mouseX > left + 90 && mouseX < left + 98 && mouseY > top + 73 && mouseY < top + 84) {
			if (container.isLimitingInput()) {
				this.renderTooltip(stack, ModTooltips.LIMITED_INPUT.color(TextFormatting.WHITE).build(), mouseX, mouseY);
			} else {
				this.renderTooltip(stack, ModTooltips.UNLIMITED_INPUT.color(TextFormatting.WHITE).build(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
		String title = this.getTitle().getString();
		this.font.drawString(stack, title, (float) (this.xSize / 2 - this.font.getStringWidth(title) / 2), 6.0F, 4210752);
		String inventory = this.playerInventory.getDisplayName().getString();
		this.font.drawString(stack, inventory, 8.0F, this.ySize - 94.0F, 4210752);
	}

	@Override
	protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
		super.func_230450_a_(stack, partialTicks, mouseX, mouseY);

		int x = this.getGuiLeft();
		int y = this.getGuiTop();
		CompressorContainer container = this.getContainer();

		int i1 = container.getEnergyBarScaled(78);
		this.blit(stack, x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);

		if (container.hasRecipe()) {
			if (container.getMaterialCount() > 0 && container.getMaterialsRequired() > 0) {
				int i2 = container.getMaterialBarScaled(26);
				this.blit(stack, x + 60, y + 74, 194, 19, i2 + 1, 10);
			}

			if (container.getProgress() > 0 && container.getEnergyRequired() > 0) {
				int i2 = container.getProgressBarScaled(24);
				this.blit(stack, x + 96, y + 47, 194, 0, i2 + 1, 16);
			}
		}

		if (mouseX > x + 68 && mouseX < x + 79 && mouseY > y + 28 && mouseY < y + 39) {
			this.blit(stack, x + 68, y + 30, 194, 32, 11, 9);
		}
		
		
		if (mouseX > x + 90 && mouseX < x + 98 && mouseY > y + 73 && mouseY < y + 84) {
			if (container.isLimitingInput()) {
				this.blit(stack, x + 90, y + 74, 194, 56, 9, 10);
			} else {
				this.blit(stack, x + 90, y + 74, 194, 43, 9, 10);
			}
		} else {
			if (container.isLimitingInput()) {
				this.blit(stack, x + 90, y + 74, 203, 56, 9, 10);
			}
		}
	}

	private ITextProperties getMaterialStackDisplayName() {
		ClientWorld world = this.getMinecraft().world;
		if (world != null) {
			CompressorContainer container = this.getContainer();
			TileEntity tile = world.getTileEntity(container.getPos());
			if (tile instanceof CompressorTileEntity) {
				CompressorTileEntity compressor = (CompressorTileEntity) tile;
				ItemStack materialStack = compressor.getMaterialStack();

				return materialStack.getDisplayName();
			}
		}

		return new StringTextComponent("");
	}
}
