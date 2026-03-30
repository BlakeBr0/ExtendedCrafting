package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.cucumber.client.screen.button.IconButton;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
import com.blakebr0.extendedcrafting.network.payload.EjectModeSwitchPayload;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class EjectModeSwitchButton extends IconButton {
    private final BlockPos pos;

    public EjectModeSwitchButton(int x, int y, BlockPos pos) {
        super(x, y, 11, 9, 195, 32, CompressorScreen.BACKGROUND);
        this.pos = pos;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        ClientPacketDistributor.sendToServer(new EjectModeSwitchPayload(this.pos));
    }

    @Override
    protected int getYImage() {
        return this.isHovered ? 0 : 10;
    }
}
