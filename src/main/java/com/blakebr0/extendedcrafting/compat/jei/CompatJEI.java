package com.blakebr0.extendedcrafting.compat.jei;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.client.container.ContainerAdvancedTable;
import com.blakebr0.extendedcrafting.client.container.ContainerBasicTable;
import com.blakebr0.extendedcrafting.client.container.ContainerEliteTable;
import com.blakebr0.extendedcrafting.client.container.ContainerEnderCrafter;
import com.blakebr0.extendedcrafting.client.container.ContainerHandheldTable;
import com.blakebr0.extendedcrafting.client.container.ContainerUltimateTable;
import com.blakebr0.extendedcrafting.client.gui.GuiAdvancedTable;
import com.blakebr0.extendedcrafting.client.gui.GuiBasicTable;
import com.blakebr0.extendedcrafting.client.gui.GuiCompressor;
import com.blakebr0.extendedcrafting.client.gui.GuiCraftingCore;
import com.blakebr0.extendedcrafting.client.gui.GuiEliteTable;
import com.blakebr0.extendedcrafting.client.gui.GuiEnderCrafter;
import com.blakebr0.extendedcrafting.client.gui.GuiHandheldTable;
import com.blakebr0.extendedcrafting.client.gui.GuiUltimateTable;
import com.blakebr0.extendedcrafting.compat.jei.combinationcrafting.CombinationCraftingCategory;
import com.blakebr0.extendedcrafting.compat.jei.combinationcrafting.CombinationCraftingWrapper;
import com.blakebr0.extendedcrafting.compat.jei.compressor.CompressorCraftingCategory;
import com.blakebr0.extendedcrafting.compat.jei.compressor.CompressorCraftingWrapper;
import com.blakebr0.extendedcrafting.compat.jei.endercrafter.EnderCrafterCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.AdvancedTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.BasicTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.EliteTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.TableShapedWrapper;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.TableShapelessWrapper;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.UltimateTableCategory;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShaped;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShapeless;
import com.blakebr0.extendedcrafting.item.ModItems;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class CompatJEI implements IModPlugin {

	public static List<Block> blocks = new ArrayList<Block>();
	public static List<Item> items = new ArrayList<Item>();

	public static IRecipeRegistry recipeRegistry;
	public static IJeiHelpers jeiHelpers;
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		this.jeiHelpers = jeiHelpers;
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		registry.addRecipeCategories(new CombinationCraftingCategory(guiHelper));
		registry.addRecipeCategories(new BasicTableCategory(guiHelper));
		registry.addRecipeCategories(new AdvancedTableCategory(guiHelper));
		registry.addRecipeCategories(new EliteTableCategory(guiHelper));
		registry.addRecipeCategories(new UltimateTableCategory(guiHelper));
		registry.addRecipeCategories(new CompressorCraftingCategory(guiHelper));
		registry.addRecipeCategories(new EnderCrafterCategory(guiHelper));
	}
	
	@Override
	public void register(IModRegistry registry) {
		for (Block block : blocks) {
			registry.addIngredientInfo(new ItemStack(block), ItemStack.class, "desc." + block.getUnlocalizedName());
		}
		
		for (Item item : items) {
			registry.addIngredientInfo(new ItemStack(item), ItemStack.class, "desc." + item.getUnlocalizedName());
		}

		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		this.jeiHelpers = jeiHelpers;

		registry.addRecipeCatalyst(new ItemStack(ModItems.itemHandheldTable), VanillaRecipeCategoryUid.CRAFTING);
		
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockCraftingCore), CombinationCraftingCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockBasicTable), BasicTableCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockAdvancedTable), AdvancedTableCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockEliteTable), EliteTableCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockUltimateTable), UltimateTableCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockCompressor), CompressorCraftingCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockEnderCrafter), EnderCrafterCategory.UID);

		registry.handleRecipes(CombinationRecipe.class, recipe -> new CombinationCraftingWrapper(jeiHelpers, recipe), CombinationCraftingCategory.UID);

		registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(jeiHelpers, recipe), BasicTableCategory.UID);
		registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(jeiHelpers, recipe), BasicTableCategory.UID);
		registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(jeiHelpers, recipe), AdvancedTableCategory.UID);
		registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(jeiHelpers, recipe), AdvancedTableCategory.UID);
		registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(jeiHelpers, recipe), EliteTableCategory.UID);
		registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(jeiHelpers, recipe), EliteTableCategory.UID);
		registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(jeiHelpers, recipe), UltimateTableCategory.UID);
		registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(jeiHelpers, recipe), UltimateTableCategory.UID);

		registry.handleRecipes(CompressorRecipe.class, recipe -> new CompressorCraftingWrapper(jeiHelpers, recipe), CompressorCraftingCategory.UID);
		
		registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(jeiHelpers, recipe), EnderCrafterCategory.UID);
		registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(jeiHelpers, recipe), EnderCrafterCategory.UID);

		registry.addRecipeClickArea(GuiHandheldTable.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeClickArea(GuiCraftingCore.class, 117, 47, 21, 14, CombinationCraftingCategory.UID);
		registry.addRecipeClickArea(GuiBasicTable.class, 91, 37, 21, 14, BasicTableCategory.UID);
		registry.addRecipeClickArea(GuiAdvancedTable.class, 109, 54, 21, 14, AdvancedTableCategory.UID);
		registry.addRecipeClickArea(GuiEliteTable.class, 139, 72, 21, 14, EliteTableCategory.UID);
		registry.addRecipeClickArea(GuiUltimateTable.class, 174, 90, 21, 14, UltimateTableCategory.UID);
		registry.addRecipeClickArea(GuiCompressor.class, 97, 47, 21, 14, CompressorCraftingCategory.UID);
		registry.addRecipeClickArea(GuiEnderCrafter.class, 90, 36, 21, 14, EnderCrafterCategory.UID);

		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();

		transferRegistry.addRecipeTransferHandler(ContainerHandheldTable.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		transferRegistry.addRecipeTransferHandler(ContainerBasicTable.class, BasicTableCategory.UID, 1, 9, 10, 36);
		transferRegistry.addRecipeTransferHandler(ContainerAdvancedTable.class, AdvancedTableCategory.UID, 1, 25, 26, 36);
		transferRegistry.addRecipeTransferHandler(ContainerEliteTable.class, EliteTableCategory.UID, 1, 49, 50, 36);
		transferRegistry.addRecipeTransferHandler(ContainerUltimateTable.class, UltimateTableCategory.UID, 1, 81, 82, 36);
		transferRegistry.addRecipeTransferHandler(ContainerEnderCrafter.class, EnderCrafterCategory.UID, 1, 9, 10, 36);

		registry.addRecipes(CombinationRecipeManager.getInstance().getRecipes(), CombinationCraftingCategory.UID);

		registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(1), BasicTableCategory.UID);
		registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(2), AdvancedTableCategory.UID);
		registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(3), EliteTableCategory.UID);
		registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(4), UltimateTableCategory.UID);

		registry.addRecipes(CompressorRecipeManager.getInstance().getValidRecipes(), CompressorCraftingCategory.UID);
		
		registry.addRecipes(EnderCrafterRecipeManager.getInstance().getRecipes(), EnderCrafterCategory.UID);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		recipeRegistry = jeiRuntime.getRecipeRegistry();
	}
}
