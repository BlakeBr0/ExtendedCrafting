package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.extendedcrafting.container.inventory.ExtendedCraftingInventory;
import com.blakebr0.extendedcrafting.util.EmptyContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.stream.IntStream;

public class TableRecipeStorage {
    private final BaseItemStackHandler[] recipes = new BaseItemStackHandler[3];
    private final int slots;
    private int selected = -1;
    private BaseItemStackHandler selectedRecipeGrid = null;

    public TableRecipeStorage(int slots) {
        this.slots = slots;

        for (int i = 0; i < this.recipes.length; i++) {
            this.recipes[i] = BaseItemStackHandler.create(slots);
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

        this.updateSelectedRecipeGrid();
    }

    public BaseItemStackHandler getRecipe(int index) {
        if (index < 0 || index >= this.recipes.length)
            return null;

        return this.recipes[index];
    }

    public boolean hasRecipe(int index) {
        if (index < 0 || index >= this.recipes.length)
            return false;

        return !this.recipes[index].getStacks().stream().allMatch(ItemStack::isEmpty);
    }

    public boolean hasRecipes() {
        return IntStream.range(0, this.recipes.length).anyMatch(this::hasRecipe);
    }

    public void setRecipe(int index, Container inventory, ItemStack output) {
        var recipe = BaseItemStackHandler.create(this.slots);

        for (int i = 0; i < this.slots - 1; i++) {
            recipe.setStackInSlot(i, inventory.getItem(i).copy());
        }

        recipe.setStackInSlot(this.slots - 1, output);

        this.recipes[index] = recipe;
    }

    public void unsetRecipe(int index) {
        if (index < 0 || index >= this.recipes.length)
            return;

        this.recipes[index] = BaseItemStackHandler.create(this.slots);

        if (index == this.selected) {
            this.setSelected(-1);
        }
    }

    public BaseItemStackHandler[] getRecipes() {
        return this.recipes;
    }

    public int getRecipeCount() {
        return Arrays.stream(this.recipes).mapToInt(recipe -> recipe.getStacks().stream().allMatch(ItemStack::isEmpty) ? 0 : 1).sum();
    }

    public BaseItemStackHandler getSelectedRecipe() {
        if (this.selected < 0 || this.selected > this.recipes.length)
            return null;

        return this.recipes[this.selected];
    }

    public BaseItemStackHandler getSelectedRecipeGrid() {
        return this.selectedRecipeGrid;
    }

    public CompoundTag serializeNBT() {
        var recipes = new ListTag();

        for (int i = 0; i < this.recipes.length; i++) {
            recipes.add(i, this.recipes[i].serializeNBT());
        }

        var tag = new CompoundTag();

        tag.put("Recipes", recipes);
        tag.putInt("Selected", this.selected);

        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        var recipes = tag.getList("Recipes", Tag.TAG_COMPOUND);

        for (int i = 0; i < recipes.size(); i++) {
            this.recipes[i].deserializeNBT(recipes.getCompound(i));
        }

        this.selected = tag.getInt("Selected");

        this.updateSelectedRecipeGrid();
    }

    public void onLoad(Level level, RecipeType<? extends Recipe<Container>> type) {
        for (int i = 0; i < this.recipes.length; i++) {
            if (this.hasRecipe(i)) {
                var recipe = this.recipes[i];
                var grid = this.createRecipeGrid(recipe);
                var inventory = new ExtendedCraftingInventory(EmptyContainer.INSTANCE, grid, 3);
                var result = level.getRecipeManager().getRecipeFor(type, inventory, level)
                        .map(r -> r.assemble(inventory, level.registryAccess()))
                        .orElse(ItemStack.EMPTY);

                recipe.setStackInSlot(this.slots - 1, result);
            }
        }
    }

    private void updateSelectedRecipeGrid() {
        if (this.selected > -1) {
            var recipe = this.recipes[this.selected];

            this.selectedRecipeGrid = this.createRecipeGrid(recipe);
        } else {
            this.selectedRecipeGrid = null;
        }
    }

    private BaseItemStackHandler createRecipeGrid(BaseItemStackHandler recipe) {
        var grid = BaseItemStackHandler.create(this.slots - 1);

        for (int i = 0; i < this.slots - 1; i++) {
            grid.setStackInSlot(i, recipe.getStackInSlot(i));
        }

        return grid;
    }
}
