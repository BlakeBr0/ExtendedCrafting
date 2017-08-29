package com.blakebr0.extendedcrafting.compat.jei;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.compat.jei.combinationcrafting.CombinationCraftingCategory;
import com.blakebr0.extendedcrafting.compat.jei.combinationcrafting.CombinationCraftingHandler;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class CompatJEI extends BlankModPlugin {
		
	public static List<Block> blocks = new ArrayList<Block>();
	public static List<Item> items = new ArrayList<Item>();
	
	public static IRecipeRegistry recipeRegistry;
	
	@Override
	public void register(IModRegistry registry) {
		for(Block block : blocks){
			registry.addDescription(new ItemStack(block), "desc." + block.getUnlocalizedName());
		}
		for(Item item : items){
			registry.addDescription(new ItemStack(item), "desc." + item.getUnlocalizedName());
		}
		
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registry.addRecipeCategories(
        		new CombinationCraftingCategory(guiHelper)
        );

        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockCraftingCore, 1, 0), CombinationCraftingCategory.UID);

        registry.addRecipeHandlers(
    //    		new AdvancedCraftingShapedHandler(jeiHelpers),
        		new CombinationCraftingHandler(jeiHelpers)
        );

     //   registry.addRecipeClickArea(GuiAdvancedCrafting.class, 109, 50, 22, 15, AdvancedCraftingCategory.UID);

     //   IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();
      //  transferRegistry.addRecipeTransferHandler(ContainerAdvancedCrafting.class, AdvancedCraftingCategory.UID, 1, 25, 26, 36);

        registry.addRecipes(CombinationRecipeManager.getInstance().getRecipes());
	}
	
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        recipeRegistry = jeiRuntime.getRecipeRegistry();
    }
}
