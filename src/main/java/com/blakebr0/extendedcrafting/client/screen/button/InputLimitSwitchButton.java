package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.cucumber.client.screen.button.IconButton;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.InputLimitSwitchMessage;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;

public class InputLimitSwitchButton extends IconButton {
    private final Supplier<Boolean> isLimitingInput;

    public InputLimitSwitchButton(int x, int y, BlockPos pos, Supplier<Boolean> isLimitingInput) {
        super(x, y, 8, 13, 195, 43, CompressorScreen.BACKGROUND, button -> {
            NetworkHandler.INSTANCE.sendToServer(new InputLimitSwitchMessage(pos));
        });

        this.isLimitingInput = isLimitingInput;
    }

    @Override
    protected int getYImage() {
        return this.isHovered ? this.isLimitingInput.get() ? 1 : 0 : 10;
    }
}
