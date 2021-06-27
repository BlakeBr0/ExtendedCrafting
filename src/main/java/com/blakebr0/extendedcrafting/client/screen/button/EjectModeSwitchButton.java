package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.cucumber.client.screen.button.IconButton;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.EjectModeSwitchMessage;
import net.minecraft.util.math.BlockPos;

public class EjectModeSwitchButton extends IconButton {
    public EjectModeSwitchButton(int x, int y, BlockPos pos) {
        super(x, y, 11, 9, 184, 23, CompressorScreen.BACKGROUND, button -> {
            NetworkHandler.INSTANCE.sendToServer(new EjectModeSwitchMessage(pos));
        });
    }

    @Override
    protected int getYImage(boolean isHovered) {
        return isHovered ? 0 : 10;
    }
}
