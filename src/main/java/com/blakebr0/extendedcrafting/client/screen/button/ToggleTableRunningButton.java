package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.RunningSwitchMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
// TODO
public class ToggleTableRunningButton extends Button {
    public ToggleTableRunningButton(int x, int y, BlockPos pos) {
        super(x, y, 13, 16, new StringTextComponent(""), button -> {
            NetworkHandler.INSTANCE.sendToServer(new RunningSwitchMessage(pos));
        });
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {

    }
}
