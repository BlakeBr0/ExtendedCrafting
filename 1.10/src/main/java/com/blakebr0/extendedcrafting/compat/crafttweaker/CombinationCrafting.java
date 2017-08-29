package com.blakebr0.extendedcrafting.compat.crafttweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

import com.blakebr0.extendedcrafting.compat.jei.CompatJEI;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
// TODO: cleanup
@ZenClass("mods.extendedcrafting.CombinationCrafting")
public class CombinationCrafting {

    @ZenMethod
    public static void addRecipe(IItemStack output, int cost, int perTick, IItemStack input, IIngredient[] ingredients){
        MineTweakerAPI.apply(new Add(new CombinationRecipe(toStack(output), cost, perTick, toStack(input), toObjects(ingredients))));
    }

    @ZenMethod
    public static void remove(IItemStack target){
        MineTweakerAPI.apply(new Remove(toStack(target)));
    }

    private static class Add implements IUndoableAction {
        CombinationRecipe recipe;

        public Add(CombinationRecipe add) {
            recipe = add;
        }

        @Override
        public void apply(){
            CombinationRecipeManager.getInstance().getRecipes().add(recipe);
            if(Loader.isModLoaded("JEI") && CompatJEI.recipeRegistry != null){
                CompatJEI.recipeRegistry.addRecipe(recipe);
            }
        }

        @Override
        public boolean canUndo(){
            return true;
        }

        @Override
        public void undo(){
            CombinationRecipeManager.getInstance().getRecipes().remove(recipe);
            if(Loader.isModLoaded("JEI") && CompatJEI.recipeRegistry != null){
                CompatJEI.recipeRegistry.removeRecipe(recipe);
            }
        }

        @Override
        public String describe(){
            return "Adding a Combination Crafting recipe for " + recipe.getOutput().getDisplayName();
        }

        @Override
        public String describeUndo(){
            return "Undoing a Combination Crafting recipe addition for " + recipe.getOutput().getDisplayName();
        }

        @Override
        public Object getOverrideKey(){
            return null;
        }

    }

    private static class Remove implements IUndoableAction {
        CombinationRecipe recipe = null;
        ItemStack remove;

        public Remove(ItemStack remove){
            this.remove = remove;
        }

        @Override
        public void apply(){
            for(CombinationRecipe recipe : CombinationRecipeManager.getInstance().getRecipes()){
                    if(recipe.getOutput().isItemEqual(remove)){
                        this.recipe = recipe;
                        CombinationRecipeManager.getInstance().getRecipes().remove(recipe);
                        if(Loader.isModLoaded("JEI") && CompatJEI.recipeRegistry != null){
                            CompatJEI.recipeRegistry.removeRecipe(recipe);
                        }
                        break;
                    }
                }
        }

        @Override
        public boolean canUndo(){
            return recipe != null;
        }

        @Override
        public void undo(){
            CombinationRecipeManager.getInstance().getRecipes().add(recipe);
            if(Loader.isModLoaded("JEI") && CompatJEI.recipeRegistry != null){
                CompatJEI.recipeRegistry.addRecipe(recipe);
            }
        }

        @Override
        public String describe(){
            return "Removing an Advanced Crafting recipe for " + remove.getDisplayName();
        }

        @Override
        public String describeUndo(){
            return "Undoing an Advanced Crafting recipe removal for " + remove.getDisplayName();
        }

        @Override
        public Object getOverrideKey(){
            return null;
        }
    }
    
    private static ItemStack toStack(IItemStack item){
        if(item == null){
        	return null;
        } else {
            Object internal = item.getInternal();
            if(internal == null || !(internal instanceof ItemStack)){
                MineTweakerAPI.getLogger().logError("Not a valid item stack: " + item);
            }
            return (ItemStack) internal;
        }
    }

    private static Object toObject(IIngredient ingredient) {
        if (ingredient == null) return null;
        else {
            if (ingredient instanceof IOreDictEntry) {
                return toString((IOreDictEntry) ingredient);
            } else if (ingredient instanceof IItemStack) {
                return toStack((IItemStack) ingredient);
            } else return null;
        }
    }

    private static Object[] toObjects(IIngredient[] list) {
        if (list == null)
            return null;
        Object[] ingredients = new Object[list.length];
        for (int x = 0; x < list.length; x++) {
            ingredients[x] = toActualObject(list[x]);
        }
        return ingredients;
    }

    private static List toList(IIngredient[] list){
        return Arrays.asList(toObjects(list));
    }

    private static Object toActualObject(IIngredient ingredient) {
        if(ingredient == null) return null;
        else {
            if(ingredient instanceof IOreDictEntry) {
                return OreDictionary.getOres(toString((IOreDictEntry) ingredient));
            } else if(ingredient instanceof IItemStack) {
                return toStack((IItemStack) ingredient);
            } else return null;
        }
    }

    private static String toString(IOreDictEntry entry) {
        return ((IOreDictEntry) entry).getName();
    }
}