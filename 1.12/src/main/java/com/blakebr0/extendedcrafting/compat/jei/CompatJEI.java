package com.blakebr0.extendedcrafting.compat.jei;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.client.container.ContainerAdvancedTable;
import com.blakebr0.extendedcrafting.client.container.ContainerBasicTable;
import com.blakebr0.extendedcrafting.client.container.ContainerCraftingTable;
import com.blakebr0.extendedcrafting.client.container.ContainerEliteTable;
import com.blakebr0.extendedcrafting.client.container.ContainerHandheldTable;
import com.blakebr0.extendedcrafting.client.container.ContainerUltimateTable;
import com.blakebr0.extendedcrafting.client.gui.GuiAdvancedTable;
import com.blakebr0.extendedcrafting.client.gui.GuiBasicTable;
import com.blakebr0.extendedcrafting.client.gui.GuiCompressor;
import com.blakebr0.extendedcrafting.client.gui.GuiCraftingCore;
import com.blakebr0.extendedcrafting.client.gui.GuiCraftingTable;
import com.blakebr0.extendedcrafting.client.gui.GuiEliteTable;
import com.blakebr0.extendedcrafting.client.gui.GuiHandheldTable;
import com.blakebr0.extendedcrafting.client.gui.GuiUltimateTable;
import com.blakebr0.extendedcrafting.compat.jei.combinationcrafting.CombinationCraftingCategory;
import com.blakebr0.extendedcrafting.compat.jei.combinationcrafting.CombinationCraftingWrapper;
import com.blakebr0.extendedcrafting.compat.jei.compressor.CompressorCraftingCategory;
import com.blakebr0.extendedcrafting.compat.jei.compressor.CompressorCraftingWrapper;
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
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
public class CompatJEI implements IModPlugin {

	public static List<Block> blocks = new ArrayList<Block>();
	public static List<Item> items = new ArrayList<Item>();

	public static IRecipeRegistry recipeRegistry;
	public static IJeiHelpers jeiHelpers;

	@Override
	public void register(IModRegistry registry) {
		for (Block block : blocks) {
			registry.addDescription(new ItemStack(block), "desc." + block.getUnlocalizedName());
		}
		for (Item item : items) {
			registry.addDescription(new ItemStack(item), "desc." + item.getUnlocalizedName());
		}

		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		this.jeiHelpers = jeiHelpers;
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		registry.addRecipeCategories(new CombinationCraftingCategory(guiHelper));
		registry.addRecipeCategories(new BasicTableCategory(guiHelper));
		registry.addRecipeCategories(new AdvancedTableCategory(guiHelper));
		registry.addRecipeCategories(new EliteTableCategory(guiHelper));
		registry.addRecipeCategories(new UltimateTableCategory(guiHelper));
		registry.addRecipeCategories(new CompressorCraftingCategory(guiHelper));

		registry.addRecipeCatalyst(StackHelper.to(ModBlocks.blockCraftingTable), VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeCatalyst(StackHelper.to(ModItems.itemHandheldTable), VanillaRecipeCategoryUid.CRAFTING);
		
		registry.addRecipeCatalyst(StackHelper.to(ModBlocks.blockCraftingCore), CombinationCraftingCategory.UID);
		registry.addRecipeCatalyst(StackHelper.to(ModBlocks.blockBasicTable), BasicTableCategory.UID);
		registry.addRecipeCatalyst(StackHelper.to(ModBlocks.blockAdvancedTable), AdvancedTableCategory.UID);
		registry.addRecipeCatalyst(StackHelper.to(ModBlocks.blockEliteTable), EliteTableCategory.UID);
		registry.addRecipeCatalyst(StackHelper.to(ModBlocks.blockUltimateTable), UltimateTableCategory.UID);
		registry.addRecipeCatalyst(StackHelper.to(ModBlocks.blockCompressor), CompressorCraftingCategory.UID);

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

		registry.addRecipeClickArea(GuiCraftingTable.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeClickArea(GuiHandheldTable.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeClickArea(GuiCraftingCore.class, 117, 37, 21, 14, CombinationCraftingCategory.UID);
		registry.addRecipeClickArea(GuiBasicTable.class, 91, 34, 21, 14, BasicTableCategory.UID);
		registry.addRecipeClickArea(GuiAdvancedTable.class, 109, 51, 21, 14, AdvancedTableCategory.UID);
		registry.addRecipeClickArea(GuiEliteTable.class, 145, 69, 21, 14, EliteTableCategory.UID);
		registry.addRecipeClickArea(GuiUltimateTable.class, 174, 80, 21, 14, UltimateTableCategory.UID);
		registry.addRecipeClickArea(GuiCompressor.class, 97, 37, 21, 14, CompressorCraftingCategory.UID);

		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();

		transferRegistry.addRecipeTransferHandler(ContainerCraftingTable.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		transferRegistry.addRecipeTransferHandler(ContainerHandheldTable.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		transferRegistry.addRecipeTransferHandler(ContainerBasicTable.class, BasicTableCategory.UID, 1, 9, 10, 36);
		transferRegistry.addRecipeTransferHandler(ContainerAdvancedTable.class, AdvancedTableCategory.UID, 1, 25, 26, 36);
		transferRegistry.addRecipeTransferHandler(ContainerEliteTable.class, EliteTableCategory.UID, 1, 49, 50, 36);
		transferRegistry.addRecipeTransferHandler(ContainerUltimateTable.class, UltimateTableCategory.UID, 1, 81, 82, 36);

		registry.addRecipes(CombinationRecipeManager.getInstance().getRecipes(), CombinationCraftingCategory.UID);

		registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(1), BasicTableCategory.UID);
		registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(2), AdvancedTableCategory.UID);
		registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(3), EliteTableCategory.UID);
		registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(4), UltimateTableCategory.UID);

		registry.addRecipes(CompressorRecipeManager.getInstance().getValidRecipes(), CompressorCraftingCategory.UID);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		recipeRegistry = jeiRuntime.getRecipeRegistry();
	}
}
