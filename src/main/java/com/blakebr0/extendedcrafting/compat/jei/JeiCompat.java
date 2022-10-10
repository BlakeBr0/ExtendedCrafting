package com.blakebr0.extendedcrafting.compat.jei;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.AdvancedAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.AdvancedTableScreen;
import com.blakebr0.extendedcrafting.client.screen.BasicAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.BasicTableScreen;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
import com.blakebr0.extendedcrafting.client.screen.CraftingCoreScreen;
import com.blakebr0.extendedcrafting.client.screen.EliteAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.EliteTableScreen;
import com.blakebr0.extendedcrafting.client.screen.EnderCrafterScreen;
import com.blakebr0.extendedcrafting.client.screen.FluxCrafterScreen;
import com.blakebr0.extendedcrafting.client.screen.UltimateAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.UltimateTableScreen;
import com.blakebr0.extendedcrafting.compat.jei.category.CombinationCraftingCategory;
import com.blakebr0.extendedcrafting.compat.jei.category.CompressorCraftingCategory;
import com.blakebr0.extendedcrafting.compat.jei.category.EnderCrafterCategory;
import com.blakebr0.extendedcrafting.compat.jei.category.FluxCraftingCategory;
import com.blakebr0.extendedcrafting.compat.jei.category.table.AdvancedTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.category.table.BasicTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.category.table.EliteTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.category.table.UltimateTableCategory;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import com.blakebr0.extendedcrafting.container.AdvancedTableContainer;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
import com.blakebr0.extendedcrafting.container.BasicTableContainer;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import com.blakebr0.extendedcrafting.container.FluxCrafterContainer;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import com.blakebr0.extendedcrafting.container.UltimateTableContainer;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.init.ModContainerTypes;
import com.blakebr0.extendedcrafting.init.ModItems;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		var helper = registration.getJeiHelpers().getGuiHelper();

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

		if (ModConfigs.ENABLE_FLUX_CRAFTER.get()) {
			registration.addRecipeCategories(new FluxCraftingCategory(helper));
		}
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		var level = Minecraft.getInstance().level;

		if (level != null) {
			var manager = level.getRecipeManager();

			if (ModConfigs.ENABLE_CRAFTING_CORE.get()) {
				registration.addRecipes(CombinationCraftingCategory.RECIPE_TYPE, manager.getAllRecipesFor(ModRecipeTypes.COMBINATION.get()));
			}

			if (ModConfigs.ENABLE_TABLES.get()) {
				var recipes = Stream.of(1, 2, 3, 4).collect(Collectors.toMap(tier -> tier, tier ->
					manager.byType(ModRecipeTypes.TABLE.get()).values()
						.stream()
						.filter(recipe -> recipe.hasRequiredTier() ? tier == recipe.getTier() : tier >= recipe.getTier())
						.toList()
				));

				registration.addRecipes(BasicTableCategory.RECIPE_TYPE, recipes.getOrDefault(1, new ArrayList<>()));
				registration.addRecipes(AdvancedTableCategory.RECIPE_TYPE, recipes.getOrDefault(2, new ArrayList<>()));
				registration.addRecipes(EliteTableCategory.RECIPE_TYPE, recipes.getOrDefault(3, new ArrayList<>()));
				registration.addRecipes(UltimateTableCategory.RECIPE_TYPE, recipes.getOrDefault(4, new ArrayList<>()));
			}

			if (ModConfigs.ENABLE_COMPRESSOR.get()) {
				registration.addRecipes(CompressorCraftingCategory.RECIPE_TYPE, manager.getAllRecipesFor(ModRecipeTypes.COMPRESSOR.get()));
			}

			if (ModConfigs.ENABLE_ENDER_CRAFTER.get()) {
				registration.addRecipes(EnderCrafterCategory.RECIPE_TYPE, manager.getAllRecipesFor(ModRecipeTypes.ENDER_CRAFTER.get()));
			}

			if (ModConfigs.ENABLE_FLUX_CRAFTER.get()) {
				registration.addRecipes(FluxCraftingCategory.RECIPE_TYPE, manager.getAllRecipesFor(ModRecipeTypes.FLUX_CRAFTER.get()));
			}
		}
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		if (ModConfigs.ENABLE_HANDHELD_WORKBENCH.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModItems.HANDHELD_TABLE.get()), mezz.jei.api.constants.RecipeTypes.CRAFTING);
		}

		if (ModConfigs.ENABLE_CRAFTING_CORE.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRAFTING_CORE.get()), CombinationCraftingCategory.RECIPE_TYPE);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.PEDESTAL.get()), CombinationCraftingCategory.RECIPE_TYPE);
		}

		if (ModConfigs.ENABLE_TABLES.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.BASIC_TABLE.get()), BasicTableCategory.RECIPE_TYPE);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.ADVANCED_TABLE.get()), AdvancedTableCategory.RECIPE_TYPE);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELITE_TABLE.get()), EliteTableCategory.RECIPE_TYPE);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.ULTIMATE_TABLE.get()), UltimateTableCategory.RECIPE_TYPE);

			if (ModConfigs.TABLE_USE_VANILLA_RECIPES.get()) {
				registration.addRecipeCatalyst(new ItemStack(ModBlocks.BASIC_TABLE.get()), mezz.jei.api.constants.RecipeTypes.CRAFTING);
			}

			if (ModConfigs.ENABLE_AUTO_TABLES.get()) {
				registration.addRecipeCatalyst(new ItemStack(ModBlocks.BASIC_AUTO_TABLE.get()), BasicTableCategory.RECIPE_TYPE);
				registration.addRecipeCatalyst(new ItemStack(ModBlocks.ADVANCED_AUTO_TABLE.get()), AdvancedTableCategory.RECIPE_TYPE);
				registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELITE_AUTO_TABLE.get()), EliteTableCategory.RECIPE_TYPE);
				registration.addRecipeCatalyst(new ItemStack(ModBlocks.ULTIMATE_AUTO_TABLE.get()), UltimateTableCategory.RECIPE_TYPE);

				if (ModConfigs.TABLE_USE_VANILLA_RECIPES.get()) {
					registration.addRecipeCatalyst(new ItemStack(ModBlocks.BASIC_AUTO_TABLE.get()), mezz.jei.api.constants.RecipeTypes.CRAFTING);
				}
			}
		}

		if (ModConfigs.ENABLE_COMPRESSOR.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.COMPRESSOR.get()), CompressorCraftingCategory.RECIPE_TYPE);
		}

		if (ModConfigs.ENABLE_ENDER_CRAFTER.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.ENDER_CRAFTER.get()), EnderCrafterCategory.RECIPE_TYPE);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.ENDER_ALTERNATOR.get()), EnderCrafterCategory.RECIPE_TYPE);
		}

		if (ModConfigs.ENABLE_FLUX_CRAFTER.get()) {
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUX_CRAFTER.get()), FluxCraftingCategory.RECIPE_TYPE);
			registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUX_ALTERNATOR.get()), FluxCraftingCategory.RECIPE_TYPE);
		}
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		if (ModConfigs.ENABLE_TABLES.get()) {
			registration.addRecipeTransferHandler(BasicTableContainer.class, ModContainerTypes.BASIC_TABLE.get(), BasicTableCategory.RECIPE_TYPE, 1, 9, 10, 36);
			registration.addRecipeTransferHandler(AdvancedTableContainer.class, ModContainerTypes.ADVANCED_TABLE.get(), AdvancedTableCategory.RECIPE_TYPE, 1, 25, 26, 36);
			registration.addRecipeTransferHandler(EliteTableContainer.class, ModContainerTypes.ELITE_TABLE.get(), EliteTableCategory.RECIPE_TYPE, 1, 49, 50, 36);
			registration.addRecipeTransferHandler(UltimateTableContainer.class, ModContainerTypes.ULTIMATE_TABLE.get(), UltimateTableCategory.RECIPE_TYPE, 1, 81, 82, 36);

			if (ModConfigs.TABLE_USE_VANILLA_RECIPES.get()) {
				registration.addRecipeTransferHandler(BasicTableContainer.class, ModContainerTypes.BASIC_TABLE.get(), mezz.jei.api.constants.RecipeTypes.CRAFTING, 1, 9, 10, 36);
			}

			if (ModConfigs.ENABLE_AUTO_TABLES.get()) {
				registration.addRecipeTransferHandler(BasicAutoTableContainer.class, ModContainerTypes.BASIC_AUTO_TABLE.get(), BasicTableCategory.RECIPE_TYPE, 1, 9, 11, 36);
				registration.addRecipeTransferHandler(AdvancedAutoTableContainer.class, ModContainerTypes.ADVANCED_AUTO_TABLE.get(), AdvancedTableCategory.RECIPE_TYPE, 1, 25, 27, 36);
				registration.addRecipeTransferHandler(EliteAutoTableContainer.class, ModContainerTypes.ELITE_AUTO_TABLE.get(), EliteTableCategory.RECIPE_TYPE, 1, 49, 51, 36);
				registration.addRecipeTransferHandler(UltimateAutoTableContainer.class, ModContainerTypes.ULTIMATE_AUTO_TABLE.get(), UltimateTableCategory.RECIPE_TYPE, 1, 81, 83, 36);

				if (ModConfigs.TABLE_USE_VANILLA_RECIPES.get()) {
					registration.addRecipeTransferHandler(BasicAutoTableContainer.class, ModContainerTypes.BASIC_AUTO_TABLE.get(), mezz.jei.api.constants.RecipeTypes.CRAFTING, 1, 9, 11, 36);
				}
			}
		}

		if (ModConfigs.ENABLE_ENDER_CRAFTER.get()) {
			registration.addRecipeTransferHandler(EnderCrafterContainer.class, ModContainerTypes.ENDER_CRAFTER.get(), EnderCrafterCategory.RECIPE_TYPE, 1, 9, 10, 36);
		}

		if (ModConfigs.ENABLE_FLUX_CRAFTER.get()) {
			registration.addRecipeTransferHandler(FluxCrafterContainer.class, ModContainerTypes.FLUX_CRAFTER.get(), FluxCraftingCategory.RECIPE_TYPE, 1, 9, 10, 36);
		}
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		if (ModConfigs.ENABLE_CRAFTING_CORE.get()) {
			registration.addRecipeClickArea(CraftingCoreScreen.class, 117, 47, 21, 14, CombinationCraftingCategory.RECIPE_TYPE);
		}

		if (ModConfigs.ENABLE_TABLES.get()) {
			registration.addRecipeClickArea(BasicTableScreen.class, 91, 37, 21, 14, BasicTableCategory.RECIPE_TYPE);
			registration.addRecipeClickArea(AdvancedTableScreen.class, 109, 54, 21, 14, AdvancedTableCategory.RECIPE_TYPE);
			registration.addRecipeClickArea(EliteTableScreen.class, 139, 72, 21, 14, EliteTableCategory.RECIPE_TYPE);
			registration.addRecipeClickArea(UltimateTableScreen.class, 174, 90, 21, 14, UltimateTableCategory.RECIPE_TYPE);

			if (ModConfigs.ENABLE_AUTO_TABLES.get()) {
				registration.addRecipeClickArea(BasicAutoTableScreen.class, 97, 36, 21, 14, BasicTableCategory.RECIPE_TYPE);
				registration.addRecipeClickArea(AdvancedAutoTableScreen.class, 121, 39, 21, 14, AdvancedTableCategory.RECIPE_TYPE);
				registration.addRecipeClickArea(EliteAutoTableScreen.class, 158, 72, 21, 14, EliteTableCategory.RECIPE_TYPE);
				registration.addRecipeClickArea(UltimateAutoTableScreen.class, 193, 90, 21, 14, UltimateTableCategory.RECIPE_TYPE);
			}
		}

		if (ModConfigs.ENABLE_COMPRESSOR.get()) {
			registration.addRecipeClickArea(CompressorScreen.class, 97, 47, 21, 14, CompressorCraftingCategory.RECIPE_TYPE);
		}

		if (ModConfigs.ENABLE_ENDER_CRAFTER.get()) {
			registration.addRecipeClickArea(EnderCrafterScreen.class, 90, 36, 21, 14, EnderCrafterCategory.RECIPE_TYPE);
		}

		if (ModConfigs.ENABLE_FLUX_CRAFTER.get()) {
			registration.addRecipeClickArea(FluxCrafterScreen.class, 90, 36, 21, 14, FluxCraftingCategory.RECIPE_TYPE);
		}
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		ModItems.SINGULARITY.ifPresent(item -> {
			registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, (stack, context) -> {
				var singularity = SingularityUtils.getSingularity(stack);
				return singularity != null ? singularity.getId().toString() : "";
			});
		});

		ModItems.RECIPE_MAKER.ifPresent(item -> {
			registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, (stack, context) -> NBTHelper.getString(stack, "Type"));
		});
	}
}
