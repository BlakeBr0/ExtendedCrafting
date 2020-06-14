package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class TableRecipeStorage {
    private final BaseItemStackHandler[] recipes = new BaseItemStackHandler[3];
    private final int slots;
    private int selected = -1;

    public TableRecipeStorage(int slots) {
        this.slots = slots;

        for (int i = 0; i < this.recipes.length; i++) {
            this.recipes[i] = new BaseItemStackHandler(slots);
        }
    }

    public int getSlots() {
        return this.slots;
    }

    public int getSelected() {
        return this.selected;
    }

    public void setSelected(int selected) {
        if (selected == this.selected || selected < -1 || selected > 2)
            selected = -1;

        this.selected = selected;
    }

    public BaseItemStackHandler getRecipe(int index) {
        if (index < 0 || index >= this.recipes.length)
            return null;

        return this.recipes[index];
    }

    public void setRecipe(int index, BaseItemStackHandler inventory, ItemStack output) {
        BaseItemStackHandler recipe = new BaseItemStackHandler(this.slots);
        for (int i = 0; i < this.slots - 1; i++) {
            recipe.setStackInSlot(i, inventory.getStackInSlot(i));
        }

        recipe.setStackInSlot(this.slots - 1, output);

        this.recipes[index] = recipe;
    }

    public void unsetRecipe(int index) {
        if (index < 0 || index >= this.recipes.length)
            return;

        this.recipes[index] = new BaseItemStackHandler(this.slots);

        if (index == this.selected)
            this.selected = -1;
    }

    public BaseItemStackHandler[] getRecipes() {
        return this.recipes;
    }

    public BaseItemStackHandler getSelectedRecipe() {
        if (this.selected < 0 || this.selected > this.recipes.length)
            return null;

        return this.recipes[this.selected];
    }

    public CompoundNBT serializeNBT() {
        ListNBT recipes = new ListNBT();
        for (int i = 0; i < this.recipes.length; i++) {
            recipes.add(i, this.recipes[i].serializeNBT());
        }

        CompoundNBT tag = new CompoundNBT();
        tag.put("Recipes", recipes);
        tag.putInt("Selected", this.selected);

        return tag;
    }

    public void deserializeNBT(CompoundNBT tag) {
        ListNBT recipes = tag.getList("Recipes", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < recipes.size(); i++) {
            this.recipes[i].deserializeNBT(recipes.getCompound(i));
        }

        this.selected = tag.getInt("Selected");
    }
}
