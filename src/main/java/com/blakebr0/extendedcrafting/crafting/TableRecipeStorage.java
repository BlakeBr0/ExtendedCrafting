package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public class TableRecipeStorage implements ValueIOSerializable {
    private final CItemStacksHandler[] recipes = new CItemStacksHandler[3];
    private final int slots;
    private int selected = -1;
    private CItemStacksHandler selectedRecipeGrid = null;

    public TableRecipeStorage(int slots) {
        this.slots = slots;

        for (int i = 0; i < this.recipes.length; i++) {
            this.recipes[i] = CItemStacksHandler.create(slots);
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

    public CItemStacksHandler getRecipe(int index) {
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

    public void setRecipe(int index, CItemStacksHandler inventory, ItemStack output) {
        var recipe = CItemStacksHandler.create(this.slots);

        for (int i = 0; i < this.slots - 1; i++) {
            var resource = inventory.getResource(i);
            recipe.set(i, resource, resource.isEmpty() ? 0 : 1);
        }

        recipe.set(this.slots - 1, ItemResource.of(output), 1);

        this.recipes[index] = recipe;
    }

    public void unsetRecipe(int index) {
        if (index < 0 || index >= this.recipes.length)
            return;

        this.recipes[index] = CItemStacksHandler.create(this.slots);

        if (index == this.selected) {
            this.setSelected(-1);
        }
    }

    public CItemStacksHandler[] getRecipes() {
        return this.recipes;
    }

    public int getRecipeCount() {
        return Arrays.stream(this.recipes).mapToInt(recipe -> recipe.getStacks().stream().allMatch(ItemStack::isEmpty) ? 0 : 1).sum();
    }

    public CItemStacksHandler getSelectedRecipe() {
        if (this.selected < 0 || this.selected > this.recipes.length)
            return null;

        return this.recipes[this.selected];
    }

    public CItemStacksHandler getSelectedRecipeGrid() {
        return this.selectedRecipeGrid;
    }

    @Override
    public void deserialize(ValueInput input) {
        var child = input.childOrEmpty("RecipeStorage");
        var recipes = input.childrenListOrEmpty("Recipes").stream().toList();

        for (int i = 0; i < recipes.size(); i++) {
            this.recipes[i].deserialize(recipes.get(i));
        }

        this.selected = child.getIntOr("Selected", 0);

        this.updateSelectedRecipeGrid();
    }

    @Override
    public void serialize(ValueOutput output) {
        var child = output.child("RecipeStorage");
        var recipes = output.childrenList("Recipes");

        for (var recipe : this.recipes) {
            recipe.serialize(recipes.addChild());
        }

        child.putInt("Selected", this.selected);
    }

    public void read(CompoundTag tag) {
//        var child = tag.child("RecipeStorage");
//        var recipes = child.childrenList("Recipes");
//
//        for (int i = 0; i < recipes.size(); i++) {
//            this.recipes[i].deserialize(recipes.get(i));
//        }
//
//        this.selected = child.getIntOr("Selected", 0);
    }

    public CompoundTag write() {
        var tag = new CompoundTag();
//        var recipes = new ListTag();
//
////        tag.
        return tag;
    }

    public void validate(Function<CraftingInput, ItemStack> validator) {
        for (int i = 0; i < this.recipes.length; i++) {
            if (this.hasRecipe(i)) {
                var recipe = this.recipes[i];
                var grid = this.createRecipeGrid(recipe);
                var size = (int) Math.sqrt(recipe.size());
                var inventory = CraftingInput.of(size, size, grid.getStacks());
                var resource = ItemResource.of(validator.apply(inventory));

                recipe.set(this.slots - 1, resource, resource.isEmpty() ? 0 : 1);
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

    private CItemStacksHandler createRecipeGrid(CItemStacksHandler recipe) {
        var grid = CItemStacksHandler.create(this.slots - 1);

        for (int i = 0; i < this.slots - 1; i++) {
            var resource = recipe.getResource(i);
            grid.set(i, resource, resource.isEmpty() ? 0 : 1);
        }

        return grid;
    }
}
