package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.extendedcrafting.client.screen.BasicAutoTableScreen;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.SaveRecipeMessage;
import com.blakebr0.extendedcrafting.network.message.SelectRecipeMessage;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;

public class RecipeSelectButton extends Button {
    public final int selected;
    public boolean active;

    public RecipeSelectButton(int x, int y, BlockPos pos, int selected) {
        super(x, y, 11, 11, "", button -> {
            NetworkHandler.INSTANCE.sendToServer(new SelectRecipeMessage(pos, selected));

            if (Screen.hasShiftDown()) {
                NetworkHandler.INSTANCE.sendToServer(new SaveRecipeMessage(pos, selected));
            }
        });

        this.selected = selected;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(BasicAutoTableScreen.BACKGROUND);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.blit(this.x, this.y, 209 + (this.selected * 11), i * 11, this.width, this.height);
    }

    @Override
    protected int getYImage(boolean hovered) {
        int i = 1;
        if (hovered) {
            i = 2;
        } else if (this.active) {
            i = 0;
        }

        return i;
    }
}
