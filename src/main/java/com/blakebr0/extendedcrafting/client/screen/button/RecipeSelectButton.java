package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.cucumber.client.screen.button.IconButton;
import com.blakebr0.extendedcrafting.client.screen.BasicAutoTableScreen;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.SaveRecipeMessage;
import com.blakebr0.extendedcrafting.network.message.SelectRecipeMessage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public class RecipeSelectButton extends IconButton {
    private final int index;
    private final Function<Integer, Boolean> isSelected;

    public RecipeSelectButton(int x, int y, BlockPos pos, int index, Function<Integer, Boolean> isSelected) {
        super(x, y, 11, 11, 208 + (index * 11) + 1, 0, BasicAutoTableScreen.BACKGROUND, button -> {
            if (Screen.hasShiftDown()) {
                NetworkHandler.INSTANCE.sendToServer(new SaveRecipeMessage(pos, index));
            } else {
                NetworkHandler.INSTANCE.sendToServer(new SelectRecipeMessage(pos, index));
            }
        });

        this.index = index;
        this.isSelected = isSelected;
    }

    @Override
    protected int getYImage(boolean isHovered) {
        return isHovered ? 2 : this.isSelected.apply(this.index) ? 0 : 1;
    }

    public int getIndex() {
        return this.index;
    }
}
