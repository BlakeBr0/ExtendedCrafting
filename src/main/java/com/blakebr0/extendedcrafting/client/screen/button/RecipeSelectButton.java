package com.blakebr0.extendedcrafting.client.screen.button;

import com.blakebr0.cucumber.client.screen.button.IconButton;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.SaveRecipeMessage;
import com.blakebr0.extendedcrafting.network.message.SelectRecipeMessage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class RecipeSelectButton extends IconButton {
    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/widgets.png");

    private final int index;
    private final TableRecipeStorage recipeStorage;

    public RecipeSelectButton(int x, int y, BlockPos pos, int index, TableRecipeStorage recipeStorage, OnTooltip onTooltip) {
        this(x, y, pos, index, 0, recipeStorage, onTooltip);
    }

    public RecipeSelectButton(int x, int y, BlockPos pos, int index, int textureY, TableRecipeStorage recipeStorage, OnTooltip onTooltip) {
        super(x, y, 11, 11, index * 11, textureY, WIDGETS_LOCATION, button -> onPress(pos, index), onTooltip);
        this.index = index;
        this.recipeStorage = recipeStorage;
    }

    @Override
    protected int getYImage() {
        return this.isHovered ? 2 : this.isSelected() ? 0 : 1;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isSelected() {
        return this.recipeStorage.getSelected() == this.index;
    }

    private static void onPress(BlockPos pos, int index) {
        if (Screen.hasShiftDown()) {
            NetworkHandler.INSTANCE.sendToServer(new SaveRecipeMessage(pos, index));
        } else {
            NetworkHandler.INSTANCE.sendToServer(new SelectRecipeMessage(pos, index));
        }
    }
}
