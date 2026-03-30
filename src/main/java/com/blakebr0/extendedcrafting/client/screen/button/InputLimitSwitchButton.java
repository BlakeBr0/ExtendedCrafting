package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.cucumber.client.screen.button.IconButton;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
import com.blakebr0.extendedcrafting.network.payload.InputLimitSwitchPayload;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.function.Supplier;

public class InputLimitSwitchButton extends IconButton {
    private final BlockPos pos;
    private final Supplier<Boolean> isLimitingInput;

    public InputLimitSwitchButton(int x, int y, BlockPos pos, Supplier<Boolean> isLimitingInput) {
        super(x, y, 8, 13, 195, 43, CompressorScreen.BACKGROUND);
        this.pos = pos;
        this.isLimitingInput = isLimitingInput;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        ClientPacketDistributor.sendToServer(new InputLimitSwitchPayload(this.pos));
    }

    @Override
    protected int getYImage() {
        return this.isHovered ? this.isLimitingInput.get() ? 1 : 0 : 10;
    }
}
