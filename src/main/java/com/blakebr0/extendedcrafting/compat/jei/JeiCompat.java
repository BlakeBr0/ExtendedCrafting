package com.blakebr0.extendedcrafting.compat.jei;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.client.screen.AdvancedAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.AdvancedTableScreen;
import com.blakebr0.extendedcrafting.client.screen.BasicAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.BasicTableScreen;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
import com.blakebr0.extendedcrafting.client.screen.CraftingCoreScreen;
import com.blakebr0.extendedcrafting.client.screen.EliteAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.EliteTableScreen;
import com.blakebr0.extendedcrafting.client.screen.EnderCrafterScreen;
import com.blakebr0.extendedcrafting.client.screen.UltimateAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.UltimateTableScreen;
import com.blakebr0.extendedcrafting.compat.jei.table.AdvancedTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.table.BasicTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.table.EliteTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.table.UltimateTableCategory;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import com.blakebr0.extendedcrafting.container.AdvancedTableContainer;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
import com.blakebr0.extendedcrafting.container.BasicTableContainer;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import com.blakebr0.extendedcrafting.container.UltimateTableContainer;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.init.ModItems;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JeiPlugin
public final class JeiCompat implements IModPlugin {
	public static final ResourceLocation UID = new ResourceLocation(ExtendedCrafting.MOD_ID, "jei_plugin");
	public static final ResourceLocation ICONS = new ResourceLocation(ExtendedCrafting.MOD_ID, "textures/gui/jei/icons.png");

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();

		if (ModConfigs.ENABLE_CRAFTING_CORE.get()) {
			registration.addRecipeCategories(new CombinationCraftingCategory(helper));
		}

		if (ModConfigs.ENABLE_TABLES.get()) {
			registration.addRecipeCategories(
					new BasicTableCategory(helper),
					new AdvancedTableCategory(helper),
					new EliteTableCategory(helper),
					new UltimateTableCategory(helper)
			);
		}

		if (ModConfigs.ENABLE_COMPRESSOR.get()) {
			registration.addRecipeCategories(new CompressorCraftingCategory(helper));
		}

		if (ModConfigs.ENABLE_ENDER_CRAFTER.get()) {
			registration.addRecipeCategories(new EnderCrafterCategory(helper));
		}
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ClientWorld world = Minecraft.getInstance().world;
		if (world != null) {
			RecipeManager manager = world.getRecipeManager();

			if (ModConfigs.ENABLE_CRAFTING_CORE.get()) {
				registration.addRecipes(manager.getRecipes(RecipeTypes.COMBINATION).values(), CombinationCraftingCategory.UID);
			}

			if (ModConfigs.ENABLE_TABLES.get()) {
				Map<Integer, List<ITableRecipe>> recipes = manager.getRecipes(RecipeTypes.TABLE).values()
						.stream()
						.map(recipe -> (ITableRecipe) recipe)
						.collect(Collectors.groupingBy(ITableRecipe::getTier));

				registration.addRecipes(recipes.getOrDefault(1, new ArrayList<>()), BasicTableCategory.UID);
				registration.addRecipes(recipes.getOrDefault(2, new ArrayList<>()), AdvancedTableCategory.UID);
				registration.addRecipes(recipes.getOrDefault(3, new ArrayList<>()), EliteTableCategory.UID);
				registration.addRecipes(recipes.getOrDefault(4, new ArrayList<>()), UltimateTableCategory.UID);
			}

			if (ModConfigs.ENABLE_COMPRESSOR.get()) {
				registration.addRecipes(manager.getRecipes(RecipeTypes.COMPRESSOR).values(), CompressorCraftingCategory.UID);
			}

			if (ModConfigs.ENABLE_ENDER_CRAFTER.get()) {
				registration.addRecipes(manager.getRecipes(RecipeTypes.ENDER_CRAFTER).values(), EnderCrafterCategory.UID);
			}
		}
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		if (ModConfigs.ENABLE_HANDHELD_WORKBENCH.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModItems.HANDHELD_TABLE.get()), VanillaRecipeCategoryUid.CRAFTING);
		}

		if (ModConfigs.ENABLE_CRAFTING_CORE.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRAFTING_CORE.get()), CombinationCraftingCategory.UID);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.PEDESTAL.get()), CombinationCraftingCategory.UID);
		}

		if (ModConfigs.ENABLE_TABLES.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.BASIC_TABLE.get()), BasicTableCategory.UID);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.ADVANCED_TABLE.get()), AdvancedTableCategory.UID);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELITE_TABLE.get()), EliteTableCategory.UID);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.ULTIMATE_TABLE.get()), UltimateTableCategory.UID);

			if (ModConfigs.ENABLE_AUTO_TABLES.get()) {
				registration.addRecipeCatalyst(new ItemStack(ModBlocks.BASIC_AUTO_TABLE.get()), BasicTableCategory.UID);
				registration.addRecipeCatalyst(new ItemStack(ModBlocks.ADVANCED_AUTO_TABLE.get()), AdvancedTableCategory.UID);
				registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELITE_AUTO_TABLE.get()), EliteTableCategory.UID);
				registration.addRecipeCatalyst(new ItemStack(ModBlocks.ULTIMATE_AUTO_TABLE.get()), UltimateTableCategory.UID);
			}
		}

		if (ModConfigs.ENABLE_COMPRESSOR.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.COMPRESSOR.get()), CompressorCraftingCategory.UID);
		}

		if (ModConfigs.ENABLE_ENDER_CRAFTER.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.ENDER_CRAFTER.get()), EnderCrafterCategory.UID);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.ENDER_ALTERNATOR.get()), EnderCrafterCategory.UID);
		}
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		if (ModConfigs.ENABLE_TABLES.get()) {
			registration.addRecipeTransferHandler(BasicTableContainer.class, BasicTableCategory.UID, 1, 9, 10, 36);
			registration.addRecipeTransferHandler(AdvancedTableContainer.class, AdvancedTableCategory.UID, 1, 25, 26, 36);
			registration.addRecipeTransferHandler(EliteTableContainer.class, EliteTableCategory.UID, 1, 49, 50, 36);
			registration.addRecipeTransferHandler(UltimateTableContainer.class, UltimateTableCategory.UID, 1, 81, 82, 36);

			if (ModConfigs.ENABLE_AUTO_TABLES.get()) {
				registration.addRecipeTransferHandler(BasicAutoTableContainer.class, BasicTableCategory.UID, 1, 9, 11, 36);
				registration.addRecipeTransferHandler(AdvancedAutoTableContainer.class, AdvancedTableCategory.UID, 1, 25, 27, 36);
				registration.addRecipeTransferHandler(EliteAutoTableContainer.class, EliteTableCategory.UID, 1, 49, 51, 36);
				registration.addRecipeTransferHandler(UltimateAutoTableContainer.class, UltimateTableCategory.UID, 1, 81, 83, 36);
			}
		}

		if (ModConfigs.ENABLE_ENDER_CRAFTER.get()) {
			registration.addRecipeTransferHandler(EnderCrafterContainer.class, EnderCrafterCategory.UID, 1, 9, 10, 36);
		}
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		if (ModConfigs.ENABLE_CRAFTING_CORE.get()) {
			registration.addRecipeClickArea(CraftingCoreScreen.class, 117, 47, 21, 14, CombinationCraftingCategory.UID);
		}

		if (ModConfigs.ENABLE_TABLES.get()) {
			registration.addRecipeClickArea(BasicTableScreen.class, 91, 37, 21, 14, BasicTableCategory.UID);
			registration.addRecipeClickArea(AdvancedTableScreen.class, 109, 54, 21, 14, AdvancedTableCategory.UID);
			registration.addRecipeClickArea(EliteTableScreen.class, 139, 72, 21, 14, EliteTableCategory.UID);
			registration.addRecipeClickArea(UltimateTableScreen.class, 174, 90, 21, 14, UltimateTableCategory.UID);

			if (ModConfigs.ENABLE_AUTO_TABLES.get()) {
				registration.addRecipeClickArea(BasicAutoTableScreen.class, 97, 36, 21, 14, BasicTableCategory.UID);
				registration.addRecipeClickArea(AdvancedAutoTableScreen.class, 121, 39, 21, 14, AdvancedTableCategory.UID);
				registration.addRecipeClickArea(EliteAutoTableScreen.class, 158, 72, 21, 14, EliteTableCategory.UID);
				registration.addRecipeClickArea(UltimateAutoTableScreen.class, 193, 90, 21, 14, UltimateTableCategory.UID);
			}
		}

		if (ModConfigs.ENABLE_COMPRESSOR.get()) {
			registration.addRecipeClickArea(CompressorScreen.class, 97, 47, 21, 14, CompressorCraftingCategory.UID);
		}

		if (ModConfigs.ENABLE_ENDER_CRAFTER.get()) {
			registration.addRecipeClickArea(EnderCrafterScreen.class, 90, 36, 21, 14, EnderCrafterCategory.UID);
		}
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		ModItems.SINGULARITY.ifPresent(item -> {
			registration.registerSubtypeInterpreter(item, stack -> {
				Singularity singularity = SingularityUtils.getSingularity(stack);
				return singularity != null ? singularity.getId().toString() : "";
			});
		});

		ModItems.RECIPE_MAKER.ifPresent(item -> {
			registration.registerSubtypeInterpreter(item, stack -> NBTHelper.getString(stack, "Type"));
		});
	}
}
