package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.cucumber.client.screen.button.IconButton;
import com.blakebr0.extendedcrafting.client.screen.BasicAutoTableScreen;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.SaveRecipeMessage;
import com.blakebr0.extendedcrafting.network.message.SelectRecipeMessage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.BlockPos;

public class RecipeSelectButton extends IconButton {
    public final int selected;
    public boolean active;

    public RecipeSelectButton(int x, int y, BlockPos pos, int selected) {
        super(x, y, 11, 11, 208 + (selected * 11) + 1, 0, BasicAutoTableScreen.BACKGROUND, button -> {
            if (Screen.hasShiftDown()) {
                NetworkHandler.INSTANCE.sendToServer(new SaveRecipeMessage(pos, selected));
            } else {
                NetworkHandler.INSTANCE.sendToServer(new SelectRecipeMessage(pos, selected));
            }
        });

        this.selected = selected;
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
