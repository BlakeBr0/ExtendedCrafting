package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.cucumber.client.screen.button.IconButton;
import com.blakebr0.extendedcrafting.client.screen.BasicAutoTableScreen;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.SaveRecipeMessage;
import com.blakebr0.extendedcrafting.network.message.SelectRecipeMessage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;

public class RecipeSelectButton extends IconButton {
    private final int index;
    private final TableRecipeStorage recipeStorage;

    public RecipeSelectButton(int x, int y, BlockPos pos, int index, TableRecipeStorage recipeStorage, OnTooltip onTooltip) {
        super(x, y, 11, 11, 208 + (index * 11) + 1, 0, BasicAutoTableScreen.BACKGROUND, button -> {
            if (Screen.hasShiftDown()) {
                NetworkHandler.INSTANCE.sendToServer(new SaveRecipeMessage(pos, index));
            } else {
                NetworkHandler.INSTANCE.sendToServer(new SelectRecipeMessage(pos, index));
            }
        }, onTooltip);

        this.index = index;
        this.recipeStorage = recipeStorage;
    }

    @Override
    protected int getYImage(boolean isHovered) {
        return isHovered ? 2 : this.isSelected() ? 0 : 1;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isSelected() {
        return this.recipeStorage.getSelected() == this.index;
    }
}
