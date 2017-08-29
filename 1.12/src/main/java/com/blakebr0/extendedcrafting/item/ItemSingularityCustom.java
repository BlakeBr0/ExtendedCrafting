package com.blakebr0.extendedcrafting.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.item.ItemMeta;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfig;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class ItemSingularityCustom extends ItemMeta implements IModelHelper {

	public static ArrayList<CustomSingularity> singularities = new ArrayList<>();
	public static Map<Integer, Integer> singularityColors = new HashMap<>();
	public static Map<Integer, Object> singularityMaterials = new HashMap<>();
	private Configuration config = ModConfig.instance.config;

	public ItemSingularityCustom() {
		super("ec.singularity_custom", ExtendedCrafting.REGISTRY);
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String name = items.containsKey(stack.getMetadata())
				? items.get(stack.getMetadata()).getName().replace("_", " ") : "Dummy";
		return WordUtils.capitalize(name) + " " + Utils.localize("item.ec.singularity.name");
	}

	public void configure(Configuration config) {
		ConfigCategory category = config.getCategory("singularity");
		String[] values = config.get(category.getName(), "_custom_singularities", new String[0]).getStringList();
		category.get("_custom_singularities")
				.setComment("Here you can add your own custom Singularities." + "\n- Syntax: meta;name;material;color"
						+ "\n- Example: 12;super_potato;minecraft:carrot;123456"
						+ "\n- 'meta' must be different for each, and should not be changed."
						+ "\n- 'name' should be lower case with underscores for spaces. Singularity is added automatically."
						+ "\n- Example: 'lots_of_spaghetti' would show 'Lots Of Spaghetti Singularity'."
						+ "\n- 'material' can be an item id, or an OreDictionary value. This is for the generic crafting recipe."
						+ "\n- Note: if you plan on adding your own recipe with the CraftTweaker integration, put 'none'."
						+ "\n- Examples: 'minecraft:stone' is an item id, 'ore:ingotIron' is the OreDictionary value 'ingotIron'."
						+ "\n- 'color' the color of the singularity as a hex value. http://htmlcolorcodes.com/"
						+ "\n- Example: 123456 would color it as whatever that color is.");

		for (String value : values) {
			String[] parts = value.split(";");

			if (parts.length != 4) {
				ExtendedCrafting.LOGGER.error("Invalid custom singularity syntax length: " + value);
				continue;
			}

			int meta;
			String name = parts[1];
			String material = parts[2];
			int color;

			try {
				meta = Integer.valueOf(parts[0]);
				color = Integer.parseInt(parts[3], 16);
			} catch (NumberFormatException e) {
				ExtendedCrafting.LOGGER.error("Invalid custom singularity syntax ints: " + value);
				continue;
			}

			singularities.add(new CustomSingularity(meta, name, material, color));
		}
	}

	@Override
	public void init() {
		for (CustomSingularity sing : singularities) {
			if (sing.name.startsWith("ore:")) {
				addSingularity(sing.meta, sing.name, sing.material.substring(4), sing.color);
			} else {
				addSingularity(sing.meta, sing.name, sing.material, sing.color);
			}
		}
	}

	@Override
	public void initModels() {
		for (Map.Entry<Integer, MetaItem> item : items.entrySet()) {
			ModelLoader.setCustomModelResourceLocation(this, item.getKey(),
					new ModelResourceLocation(ExtendedCrafting.MOD_ID + ":singularity", "inventory"));
		}
	}

	public ItemStack addSingularity(int meta, String name, ItemStack material, int color) {
		singularityColors.put(meta, color);
		singularityMaterials.put(meta, material);
		ItemSingularityUltimate.addSingularityToRecipe(StackHelper.to(this, 1, meta));
		return addItem(meta, name, true);
	}

	public ItemStack addSingularity(int meta, String name, String oreName, int color) {
		singularityColors.put(meta, color);
		singularityMaterials.put(meta, oreName);
		ItemSingularityUltimate.addSingularityToRecipe(StackHelper.to(this, 1, meta));
		return addItem(meta, name, true);
	}

	public class CustomSingularity {

		public int meta;
		public String name;
		public String material;
		public int color;

		public CustomSingularity(int meta, String name, String material, int color) {
			this.meta = meta;
			this.name = name;
			this.material = material;
			this.color = color;
		}
	}

	public static class ColorHandler implements IItemColor {

		@Override
		public int getColorFromItemstack(ItemStack stack, int tintIndex) {
			return singularityColors.get(stack.getMetadata());
		}
	}
}
