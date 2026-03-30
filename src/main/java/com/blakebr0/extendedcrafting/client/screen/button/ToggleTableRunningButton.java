package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.cucumber.client.screen.button.IconButton;
import com.blakebr0.extendedcrafting.client.screen.BasicAutoTableScreen;
import com.blakebr0.extendedcrafting.network.payload.RunningSwitchPayload;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Supplier;

public class ToggleTableRunningButton extends IconButton {
    private final BlockPos pos;
    private final Supplier<Boolean> isRunning;

    public ToggleTableRunningButton(int x, int y, BlockPos pos, Supplier<Boolean> isRunning) {
        super(x, y, 13, 13, 194, 18, BasicAutoTableScreen.BACKGROUND);
        this.pos = pos;
        this.isRunning = isRunning;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        ClientPacketDistributor.sendToServer(new RunningSwitchPayload(this.pos));
    }

    @Override
    protected int getYImage() {
        return !this.isRunning.get() ? 0 : 10;
    }
}
