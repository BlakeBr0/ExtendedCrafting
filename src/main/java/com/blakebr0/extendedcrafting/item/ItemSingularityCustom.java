package com.blakebr0.extendedcrafting.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.item.ItemMeta;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ItemSingularityCustom extends ItemMeta implements IModelHelper, IEnableable {

	public static ArrayList<CustomSingularity> singularities = new ArrayList<>();
	public static Map<Integer, Integer> singularityColors = new HashMap<>();
	public static Map<Integer, Object> singularityMaterials = new HashMap<>();
	private Configuration config = ModConfig.instance.config;

	public ItemSingularityCustom() {
		super("ec.singularity_custom", ExtendedCrafting.REGISTRY);
		this.setCreativeTab(ExtendedCrafting.CREATIVE_TAB);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String name = items.containsKey(stack.getMetadata()) ? items.get(stack.getMetadata()).getName().replace("_", " ") : "Dummy";
		return WordUtils.capitalize(name) + " " + Utils.localize("item.ec.singularity.name");
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}

	public void configure(Configuration config) {
		ConfigCategory category = config.getCategory("singularity");
		String[] values = config.get(category.getName(), "custom_singularities", new String[0]).getStringList();
		category.get("custom_singularities").setComment("Here you can add your own custom Singularities." 
						+ "\n- Syntax: meta;name;material;color"
						+ "\n- Example: 12;super_potato;minecraft:carrot;123456"
						+ "\n- 'meta' must be different for each, and should not be changed."
						+ "\n- 'name' should be lower case with underscores for spaces. Singularity is added automatically."
						+ "\n- Example: 'lots_of_spaghetti' would show 'Lots Of Spaghetti Singularity'."
						+ "\n- 'material' is an item id or ore dictionary entry. This is for the generic crafting recipe."
						+ "\n- Note: if you plan on adding your own recipe with the CraftTweaker integration, put 'none'."
						+ "\n- Examples: 'minecraft:stone' for stone, 'ore:ingotIron' for the ore dictionary entry 'ingotIron'."
						+ "\n- Note: you can also specify meta for item ids, by adding them to the end of the item id."
						+ "\n- Example: minecraft:stone:3 for a meta of 3. Make the meta 32767 for wildcard value."
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
			addSingularity(sing.meta, sing.name, sing.material, sing.color);
		}
	}

	@Override
	public void initModels() {
		for (Map.Entry<Integer, MetaItem> item : items.entrySet()) {
			ModelLoader.setCustomModelResourceLocation(this, item.getKey(), ResourceHelper.getModelResource(ExtendedCrafting.MOD_ID, "singularity", "inventory"));
		}
	}
	
	@Override
	public boolean isEnabled() {
		return ModConfig.confSingularityEnabled;
	}

	public ItemStack addSingularity(int meta, String name, String material, int color) {
		singularityColors.put(meta, color);
		singularityMaterials.put(meta, material);
		ItemSingularityUltimate.addSingularityToRecipe(new ItemStack(this, 1, meta));
		return addItem(meta, name, true);
	}
	
	public void initRecipes() {
		if (!this.isEnabled()) return;
		
		for (Map.Entry<Integer, Object> obj : singularityMaterials.entrySet()) {
			Object value = obj.getValue();
			int meta = obj.getKey();
			Item item = null;
			ItemStack stack = ItemStack.EMPTY;
			if (value instanceof String) {
				if ("none".equalsIgnoreCase((String) value)) {
					continue;
				}
				String[] parts = ((String) value).split(":");
				int matMeta = 0;
				if (parts.length == 3) {
					try {
						matMeta = Integer.valueOf(parts[2]);
					} catch (NumberFormatException e) {
						ExtendedCrafting.LOGGER.error("Invalid meta for singularity: " + value.toString());
						continue;
					}
					item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
					stack = new ItemStack(item, 1, matMeta);
					if (!stack.isEmpty()) {
						CompressorRecipeManager.getInstance().addRecipe(new ItemStack(this, 1, meta), stack.copy(), ModConfig.confSingularityAmount, ItemSingularity.getCatalystStack(), false, ModConfig.confSingularityRF);
					}
				} else if (parts.length == 2) {
					if (((String) value).startsWith("ore:")) {
						String ore = ((String) value).substring(4);
						if (OreDictionary.doesOreNameExist(ore)) {
							if (!OreDictionary.getOres(ore).isEmpty()) {
								CompressorRecipeManager.getInstance().addRecipe(new ItemStack(this, 1, meta), ore, ModConfig.confSingularityAmount, ItemSingularity.getCatalystStack(), false, ModConfig.confSingularityRF);
							}
						}
					} else {
						item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
						stack = new ItemStack(item);
						if (!stack.isEmpty()) {
							CompressorRecipeManager.getInstance().addRecipe(new ItemStack(this, 1, meta), stack.copy(), ModConfig.confSingularityAmount, ItemSingularity.getCatalystStack(), false, ModConfig.confSingularityRF);
						}
					}
				} else {
					ExtendedCrafting.LOGGER.error("Invalid material for singularity: " + value.toString());
					continue;
				}
			} else {
				ExtendedCrafting.LOGGER.error("Invalid material for singularity: " + value.toString());
				continue;
			}
		}
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
		public int colorMultiplier(ItemStack stack, int tintIndex) {
			return singularityColors.get(stack.getMetadata());
		}
	}
}
