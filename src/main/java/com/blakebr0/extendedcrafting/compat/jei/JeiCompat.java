package com.blakebr0.extendedcrafting.compat.jei;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.client.screen.AdvancedTableScreen;
import com.blakebr0.extendedcrafting.client.screen.BasicTableScreen;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
import com.blakebr0.extendedcrafting.client.screen.CraftingCoreScreen;
import com.blakebr0.extendedcrafting.client.screen.EliteTableScreen;
import com.blakebr0.extendedcrafting.client.screen.EnderCrafterScreen;
import com.blakebr0.extendedcrafting.client.screen.GuiHandheldTable;
import com.blakebr0.extendedcrafting.client.screen.UltimateTableScreen;
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
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.AdvancedTableContainer;
import com.blakebr0.extendedcrafting.container.BasicTableContainer;
import com.blakebr0.extendedcrafting.container.ContainerHandheldTable;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import com.blakebr0.extendedcrafting.container.UltimateTableContainer;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShaped;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShapeless;
import com.blakebr0.extendedcrafting.item.ModItems;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JeiCompat implements IModPlugin {
	public static final ResourceLocation UID = new ResourceLocation(ExtendedCrafting.MOD_ID, "jei_plugin");
	public static final ResourceLocation ICONS = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/jei/icons.png");

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();

		if (ModConfigs.confCraftingCoreEnabled) {
			registry.addRecipeCategories(new CombinationCraftingCategory(helper));
		}

		if (ModConfigs.confTableEnabled) {
			registry.addRecipeCategories(new BasicTableCategory(helper));
			registry.addRecipeCategories(new AdvancedTableCategory(helper));
			registry.addRecipeCategories(new EliteTableCategory(helper));
			registry.addRecipeCategories(new UltimateTableCategory(helper));
		}

		if (ModConfigs.confCompressorEnabled) {
			registry.addRecipeCategories(new CompressorCraftingCategory(helper));
		}

		if (ModConfigs.confEnderEnabled) {
			registry.addRecipeCategories(new EnderCrafterCategory(helper));
		}
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {

	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {

	}

	@Override
	public void register(IModRegistry registry) {
		blocks.forEach(block -> registry.addIngredientInfo(new ItemStack(block), ItemStack.class, "desc." + block.getUnlocalizedName()));
		items.forEach(item -> registry.addIngredientInfo(new ItemStack(item), ItemStack.class, "desc." + item.getUnlocalizedName()));

		IJeiHelpers helpers = registry.getJeiHelpers();
		IRecipeTransferRegistry transfer = registry.getRecipeTransferRegistry();

		if (ModConfigs.confHandheldTableEnabled) {
			registry.addRecipeCatalyst(new ItemStack(ModItems.itemHandheldTable), VanillaRecipeCategoryUid.CRAFTING);
			registry.addRecipeClickArea(GuiHandheldTable.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
			transfer.addRecipeTransferHandler(ContainerHandheldTable.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		}

		if (ModConfigs.confCraftingCoreEnabled) {
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockCraftingCore), CombinationCraftingCategory.UID);
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockPedestal), CombinationCraftingCategory.UID);
			registry.handleRecipes(CombinationRecipe.class, recipe -> new CombinationCraftingWrapper(helpers, recipe), CombinationCraftingCategory.UID);
			registry.addRecipeClickArea(CraftingCoreScreen.class, 117, 47, 21, 14, CombinationCraftingCategory.UID);
			registry.addRecipes(CombinationRecipeManager.getInstance().getRecipes(), CombinationCraftingCategory.UID);
		}

		if (ModConfigs.confTableEnabled) {
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockBasicTable), BasicTableCategory.UID);
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.ADVANCED_TABLE), AdvancedTableCategory.UID);
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.ELITE_TABLE), EliteTableCategory.UID);
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.ULTIMATE_TABLE), UltimateTableCategory.UID);
			registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(helpers, recipe, 217, 0), BasicTableCategory.UID);
			registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(helpers, recipe, 215, 0), BasicTableCategory.UID);
			registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(helpers, recipe, 285, 0), AdvancedTableCategory.UID);
			registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(helpers, recipe, 283, 0), AdvancedTableCategory.UID);
			registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(helpers, recipe, 237, 257), EliteTableCategory.UID);
			registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(helpers, recipe, 235, 257), EliteTableCategory.UID);
			registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(helpers, recipe, 308, 329), UltimateTableCategory.UID);
			registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(helpers, recipe, 306, 329), UltimateTableCategory.UID);
			registry.addRecipeClickArea(BasicTableScreen.class, 91, 37, 21, 14, BasicTableCategory.UID);
			registry.addRecipeClickArea(AdvancedTableScreen.class, 109, 54, 21, 14, AdvancedTableCategory.UID);
			registry.addRecipeClickArea(EliteTableScreen.class, 139, 72, 21, 14, EliteTableCategory.UID);
			registry.addRecipeClickArea(UltimateTableScreen.class, 174, 90, 21, 14, UltimateTableCategory.UID);
			registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(1), BasicTableCategory.UID);
			registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(2), AdvancedTableCategory.UID);
			registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(3), EliteTableCategory.UID);
			registry.addRecipes(TableRecipeManager.getInstance().getRecipesTiered(4), UltimateTableCategory.UID);
			transfer.addRecipeTransferHandler(BasicTableContainer.class, BasicTableCategory.UID, 1, 9, 10, 36);
			transfer.addRecipeTransferHandler(AdvancedTableContainer.class, AdvancedTableCategory.UID, 1, 25, 26, 36);
			transfer.addRecipeTransferHandler(EliteTableContainer.class, EliteTableCategory.UID, 1, 49, 50, 36);
			transfer.addRecipeTransferHandler(UltimateTableContainer.class, UltimateTableCategory.UID, 1, 81, 82, 36);

			if (ModConfigs.confTableUseRecipes) {
				registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockBasicTable), VanillaRecipeCategoryUid.CRAFTING);
				transfer.addRecipeTransferHandler(BasicTableContainer.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
			}
		}

		if (ModConfigs.confCompressorEnabled) {
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockCompressor), CompressorCraftingCategory.UID);
			registry.handleRecipes(CompressorRecipe.class, recipe -> new CompressorCraftingWrapper(helpers, recipe), CompressorCraftingCategory.UID);
			registry.addRecipeClickArea(CompressorScreen.class, 97, 47, 21, 14, CompressorCraftingCategory.UID);
			registry.addRecipes(CompressorRecipeManager.getInstance().getValidRecipes(), CompressorCraftingCategory.UID);
		}

		if (ModConfigs.confEnderEnabled) {
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.ENDER_CRAFTER), EnderCrafterCategory.UID);
			registry.addRecipeCatalyst(new ItemStack(ModBlocks.ENDER_ALTERNATOR), EnderCrafterCategory.UID);
			registry.handleRecipes(TableRecipeShaped.class, recipe -> new TableShapedWrapper(helpers, recipe), EnderCrafterCategory.UID);
			registry.handleRecipes(TableRecipeShapeless.class, recipe -> new TableShapelessWrapper(helpers, recipe), EnderCrafterCategory.UID);
			registry.addRecipeClickArea(EnderCrafterScreen.class, 90, 36, 21, 14, EnderCrafterCategory.UID);
			registry.addRecipes(EnderCrafterRecipeManager.getInstance().getRecipes(), EnderCrafterCategory.UID);
			transfer.addRecipeTransferHandler(EnderCrafterContainer.class, EnderCrafterCategory.UID, 1, 9, 10, 36);
		}
	}
}
