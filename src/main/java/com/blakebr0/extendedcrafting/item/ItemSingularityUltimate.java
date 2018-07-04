package com.blakebr0.extendedcrafting.item;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.item.ItemBase;
import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class ItemSingularityUltimate extends ItemBase {

	public static ArrayList<ItemStack> singularities = new ArrayList<>();
	public static ArrayList<Integer> blacklistDefaults = new ArrayList<>();
	public static ArrayList<Integer> blacklistCustoms = new ArrayList<>();

	public ItemSingularityUltimate() {
		super("ec.singularity_ultimate");
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
		this.setMaxStackSize(16);
	}

	public void configure(Configuration config) {
		ConfigCategory category = config.getCategory("singularity");
		String[] values = config.get(category.getName(), "ultimate_singularity_recipe_blacklist", new String[0]).getStringList();
		category.get("ultimate_singularity_recipe_blacklist").setComment("Blacklist Singularities from being in the Ultimate Singularity crafting recipe."
						+ "\n- Syntax: singularityType;meta"
						+ "\n- 'singularityType' can be 'default' or 'custom'."
						+ "\n- 'default' for the ones added by the mod by default, 'custom' being the ones defined in '_custom_singularities'."
						+ "\n- Example: custom;12");

		for (String value : values) {
			String[] parts = value.split(";");

			if (parts.length != 2) {
				ExtendedCrafting.LOGGER.error("Invalid ultimate singularity blacklist syntax length: " + value);
				continue;
			}

			String type = parts[0];
			int meta;

			if (!type.equals("default") && !type.equals("custom")) {
				ExtendedCrafting.LOGGER.error("Invalid ultimate singularity blacklist type: " + value);
				continue;
			}

			try {
				meta = Integer.valueOf(parts[1]);
			} catch (NumberFormatException e) {
				ExtendedCrafting.LOGGER.error("Invalid ultimate singularity blacklist meta: " + value);
				continue;
			}

			if (type.equals("default")) {
				blacklistDefaults.add(meta);
			} else if (type.equals("custom")) {
				blacklistCustoms.add(meta);
			}
		}
	}

	public static void addSingularityToRecipe(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ItemSingularity) {
			if (blacklistDefaults.contains(stack.getMetadata())) {
				return;
			}
		}
		if (item instanceof ItemSingularityCustom) {
			if (blacklistCustoms.contains(stack.getMetadata())) {
				return;
			}
		}
		if (singularities.size() < 82) {
			singularities.add(stack);
		} else {
			ExtendedCrafting.LOGGER.error("Unable to add " + stack.getDisplayName() + " to the Ultimate Singularity recipe as there is no more room.");
		}
	}

	public void initRecipe() {
		if (!ModConfig.confUltimateSingularityRecipe) {
			return;
		}
		TableRecipeManager.getInstance().addShapeless(4, StackHelper.to(ModItems.itemSingularityUltimate, 1, 0), singularities.toArray());
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(Colors.ITALICS + Utils.localize("tooltip.ec.singularity_ultimate"));
	}

	public class BlacklistEntry {

		public String type;
		public int meta;

		public BlacklistEntry(String type, int meta) {
			this.type = type;
			this.meta = meta;
		}
	}
}
