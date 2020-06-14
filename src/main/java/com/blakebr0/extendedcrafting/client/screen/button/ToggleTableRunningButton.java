package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.RunningSwitchMessage;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;

public class ToggleTableRunningButton extends Button {
    public ToggleTableRunningButton(int x, int y, BlockPos pos) {
        super(x, y, 13, 16, "", button -> {
            NetworkHandler.INSTANCE.sendToServer(new RunningSwitchMessage(pos));
        });
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

    }
}
