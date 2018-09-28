package com.blakebr0.extendedcrafting.compat.jei;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
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
import com.blakebr0.extendedcrafting.config.ModConfig;
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
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JEIPlugin
public class CompatJEI implements IModPlugin {

	public static final ResourceLocation ICONS = ResourceHelper.getResource(ExtendedCrafting.MOD_ID, "textures/jei/icons.png");
	
	public static List<Block> blocks = new ArrayList<Block>();
	public static List<Item> items = new ArrayList<Item>();
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
		
		if (ModConfig.confCraftingCoreEnabled) {
			registry.addRecipeCategories(new CombinationCraftingCategory(helper));
		}
		
		if (ModConfig.confTableEnabled) {
			registry.addRecipeCategories(new BasicTableCategory(helper));
			registry.addRecipeCategories(new AdvancedTableCategory(helper));
			registry.addRecipeCategories(new EliteTableCategory(helper));
			registry.addRecipeCategories(new UltimateTableCategory(helper));
		}

		if (ModConfig.confCompressorEnabled) {
			registry.addRecipeCategories(new CompressorCraftingCategory(helper));
		}
		
		if (ModConfig.confEnderEnabled) {
			registry.addRecipeCategories(new EnderCrafterCategory(helper));
		}
	}
	
	@Override
	public void register(IModRegistry registry) {
		blocks.forEach(block -> registry.addIngredientInfo(new ItemStack(block), ItemStack.class, "desc." + block.getUnlocalizedName()));
		items.forEach(item -> registry.addIngredientInfo(new ItemStack(item), ItemStack.class, "desc." + item.getUnlocalizedName()));
		
		IJeiHelpers helpers = registry.getJeiHelpers();
		IRecipeTransferRegistry transfer = registry.getRecipeTransferRegistry();

		if (ModConfig.confHandheldTableEnabled) {
			registry.addRecipeCatalyst(new ItemStack(ModItems.itemHandheldTable), VanillaRecipeCategoryUid.CRAFTING);
			registry.addRecipeClickArea(GuiHandheldTable.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
			transfer.addRecipeTransferHandler(ContainerHandheldTable.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		}
		
		if (ModConfig.confCraftingCoreEnabled) {
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockCraftingCore), CombinationCraftingCategory.UID);
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockPedestal), CombinationCraftingCategory.UID);
			registry.handleRecipes(CombinationRecipe.class, recipe -> new CombinationCraftingWrapper(helpers, recipe), CombinationCraftingCategory.UID);
			registry.addRecipeClickArea(GuiCraftingCore.class, 117, 47, 21, 14, CombinationCraftingCategory.UID);
			registry.addRecipes(CombinationRecipeManager.getInstance().getRecipes(), CombinationCraftingCategory.UID);
		}
		
		if (ModConfig.confTableEnabled) {
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockBasicTable), BasicTableCategory.UID);
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockAdvancedTable), AdvancedTableCategory.UID);
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockEliteTable), EliteTableCategory.UID);
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockUltimateTable), UltimateTableCategory.UID);
			registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(helpers, recipe, 217, 0), BasicTableCategory.UID);
			registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(helpers, recipe, 215, 0), BasicTableCategory.UID);
			registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(helpers, recipe, 285, 0), AdvancedTableCategory.UID);
			registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(helpers, recipe, 283, 0), AdvancedTableCategory.UID);
			registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(helpers, recipe, 237, 257), EliteTableCategory.UID);
			registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(helpers, recipe, 235, 257), EliteTableCategory.UID);
			registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(helpers, recipe, 308, 329), UltimateTableCategory.UID);
			registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(helpers, recipe, 306, 329), UltimateTableCategory.UID);
			registry.addRecipeClickArea(GuiBasicTable.class, 91, 37, 21, 14, BasicTableCategory.UID);
			registry.addRecipeClickArea(GuiAdvancedTable.class, 109, 54, 21, 14, AdvancedTableCategory.UID);
			registry.addRecipeClickArea(GuiEliteTable.class, 139, 72, 21, 14, EliteTableCategory.UID);
			registry.addRecipeClickArea(GuiUltimateTable.class, 174, 90, 21, 14, UltimateTableCategory.UID);
			registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(1), BasicTableCategory.UID);
			registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(2), AdvancedTableCategory.UID);
			registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(3), EliteTableCategory.UID);
			registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(4), UltimateTableCategory.UID);
			transfer.addRecipeTransferHandler(ContainerBasicTable.class, BasicTableCategory.UID, 1, 9, 10, 36);
			transfer.addRecipeTransferHandler(ContainerAdvancedTable.class, AdvancedTableCategory.UID, 1, 25, 26, 36);
			transfer.addRecipeTransferHandler(ContainerEliteTable.class, EliteTableCategory.UID, 1, 49, 50, 36);
			transfer.addRecipeTransferHandler(ContainerUltimateTable.class, UltimateTableCategory.UID, 1, 81, 82, 36);
			
			if (ModConfig.confTableUseRecipes) {
				registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockBasicTable), VanillaRecipeCategoryUid.CRAFTING);
				transfer.addRecipeTransferHandler(ContainerBasicTable.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
			}
		}

		if (ModConfig.confCompressorEnabled) {
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockCompressor), CompressorCraftingCategory.UID);
			registry.handleRecipes(CompressorRecipe.class, recipe -> new CompressorCraftingWrapper(helpers, recipe), CompressorCraftingCategory.UID);
			registry.addRecipeClickArea(GuiCompressor.class, 97, 47, 21, 14, CompressorCraftingCategory.UID);
			registry.addRecipes(CompressorRecipeManager.getInstance().getValidRecipes(), CompressorCraftingCategory.UID);
		}
		
		if (ModConfig.confEnderEnabled) {
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockEnderCrafter), EnderCrafterCategory.UID);
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockEnderAlternator), EnderCrafterCategory.UID);
			registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(helpers, recipe), EnderCrafterCategory.UID);
			registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(helpers, recipe), EnderCrafterCategory.UID);
			registry.addRecipeClickArea(GuiEnderCrafter.class, 90, 36, 21, 14, EnderCrafterCategory.UID);
			registry.addRecipes(EnderCrafterRecipeManager.getInstance().getRecipes(), EnderCrafterCategory.UID);
			transfer.addRecipeTransferHandler(ContainerEnderCrafter.class, EnderCrafterCategory.UID, 1, 9, 10, 36);
		}
	}
}
