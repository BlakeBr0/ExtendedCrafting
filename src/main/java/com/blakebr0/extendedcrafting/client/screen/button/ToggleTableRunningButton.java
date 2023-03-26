package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.cucumber.client.screen.button.IconButton;
import com.blakebr0.extendedcrafting.client.screen.BasicAutoTableScreen;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.RunningSwitchMessage;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;

public class ToggleTableRunningButton extends IconButton {
    private final Supplier<Boolean> isRunning;

    public ToggleTableRunningButton(int x, int y, BlockPos pos, Supplier<Boolean> isRunning) {
        super(x, y, 13, 13, 194, 18, BasicAutoTableScreen.BACKGROUND, button -> {
            NetworkHandler.INSTANCE.sendToServer(new RunningSwitchMessage(pos));
        });

        this.isRunning = isRunning;
    }

    @Override
    protected int getYImage() {
        return !this.isRunning.get() ? 0 : 10;
    }
}
