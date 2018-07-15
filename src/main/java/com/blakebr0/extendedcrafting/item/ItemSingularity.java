package com.blakebr0.extendedcrafting.item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.ItemMeta;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemSingularity extends ItemMeta implements IEnableable {

	public static Map<Integer, Integer> singularityColors = new HashMap<>();
	public static Map<Integer, Object> singularityMaterials = new HashMap<>();
	private Configuration config = ModConfig.instance.config;

	public ItemSingularity() {
		super("ec.singularity", ExtendedCrafting.REGISTRY);
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
	
	@Override
	public void init() {
		addSingularity(0, "coal", new ItemStack(Items.COAL), 0x1B1B1B);
		addSingularity(1, "iron", "ingotIron", 0x969696);
		addSingularity(2, "lapis_lazuli", new ItemStack(Items.DYE, 1, 4), 0x345EC3);
		addSingularity(3, "redstone", new ItemStack(Items.REDSTONE), 0x720000);
		addSingularity(4, "glowstone", new ItemStack(Items.GLOWSTONE_DUST), 0x868600);
		addSingularity(5, "gold", "ingotGold", 0xDEDE00);
		addSingularity(6, "diamond", "gemDiamond", 0x2CCDB1);
		addSingularity(7, "emerald", "gemEmerald", 0x00A835);

		addSingularity(16, "aluminum", "ingotAluminum", 0xCACCDA);
		addSingularity(17, "copper", "ingotCopper", 0xCE7201);
		addSingularity(18, "tin", "ingotTin", 0x7690A5);
		addSingularity(19, "bronze", "ingotBronze", 0xA87544);
		addSingularity(20, "zinc", "ingotZinc", 0xCFD2CC);
		addSingularity(21, "brass", "ingotBrass", 0xBC8B22);
		addSingularity(22, "silver", "ingotSilver", 0x83AAB2);
		addSingularity(23, "lead", "ingotLead", 0x484F67);
		addSingularity(24, "steel", "ingotSteel", 0x565656);
		addSingularity(25, "nickel", "ingotNickel", 0xBEB482);
		addSingularity(26, "constantan", "ingotConstantan", 0xA98544);
		addSingularity(27, "electrum", "ingotElectrum", 0xA79135);
		addSingularity(28, "invar", "ingotInvar", 0x929D97);
		addSingularity(29, "mithril", "ingotMithril", 0x659ABB);
		addSingularity(30, "tungsten", "ingotTungsten", 0x494E51);
		addSingularity(31, "titanium", "ingotTitanium", 0xA6A7B8);
		addSingularity(32, "uranium", "ingotUranium", 0x46800D);
		addSingularity(33, "chrome", "ingotChrome", 0xC1A9AE);
		addSingularity(34, "platinum", "ingotPlatinum", 0x6FEAEF);
		addSingularity(35, "iridium", "ingotIridium", 0x949FBE);
		
		addSingularity(48, "signalum", "ingotSignalum", 0xDD3B00);
		addSingularity(49, "lumium", "ingotLumium", 0xDEE59C);
		addSingularity(50, "enderium", "ingotEnderium", 0x0B4D4E);
		
		addSingularity(64, "ardite", "ingotArdite", 0xDA4817);
		addSingularity(65, "cobalt", "ingotCobalt", 0x023C9B);
		addSingularity(66, "manyullyn", "ingotManyullyn", 0x5C268A);
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

	public ItemStack addSingularity(int meta, String name, ItemStack material, int color) {
		addToConfig(name);
		
		boolean enabled = checkConfig(name);
		
		if (enabled) {
			singularityColors.put(meta, color);
			singularityMaterials.put(meta, material);
			ItemSingularityUltimate.addSingularityToRecipe(new ItemStack(this, 1, meta));
		}
		
		return addItem(meta, name, enabled);
	}

	public ItemStack addSingularity(int meta, String name, String oreName, int color) {
		addToConfig(name);
		
		boolean enabled = checkConfig(name);
		
		if (enabled) {
			singularityColors.put(meta, color);
			singularityMaterials.put(meta, oreName);
			ItemSingularityUltimate.addSingularityToRecipe(new ItemStack(this, 1, meta));
		}
		
		return addItem(meta, name, enabled);
	}
	
	public void initRecipes() {
		if (!ModConfig.confSingularityRecipes || !this.isEnabled()) 
			return;
		
		for (Map.Entry<Integer, Object> obj : singularityMaterials.entrySet()) {
			Object value = obj.getValue();
			int meta = obj.getKey();
			if (value instanceof ItemStack) {
				ItemStack stack = (ItemStack) value;
				if (!stack.isEmpty()) {
					CompressorRecipeManager.getInstance().addRecipe(new ItemStack(this, 1, meta), stack.copy(), ModConfig.confSingularityAmount, ItemSingularity.getCatalystStack(), false, ModConfig.confSingularityRF);
				}
			} else if (value instanceof String) {
				String name = (String) value;
				CompressorRecipeManager.getInstance().addRecipe(new ItemStack(this, 1, meta), name, ModConfig.confSingularityAmount, ItemSingularity.getCatalystStack(), false, ModConfig.confSingularityRF);
			} else {
				ExtendedCrafting.LOGGER.error("Invalid material for singularity: " + value.toString());
				continue;
			}
		}
	}
	
	public static ItemStack getCatalystStack() {
		String[] parts = ModConfig.confSingularityCatalyst.split(":");
		if (parts.length != 3) {
			return ModItems.itemMaterial.itemUltimateCatalyst;
		}
		
		Item item = ForgeRegistries.ITEMS.getValue(ResourceHelper.getResource(parts[0], parts[1]));
		if (item == null) {
			return ModItems.itemMaterial.itemUltimateCatalyst;
		}
		
		return new ItemStack(item, 1, Integer.parseInt(parts[2]));
	}
	
	public boolean checkConfig(String name) {
		String[] values = this.config.get("singularity", "default_singularities", new String[0]).getStringList();
		
		for (int i = 0; i < values.length; i++) {
			String[] entry = values[i].split("=");
			if (entry[0].equals(name)) {
				return Boolean.valueOf(entry[1]);
			}
		}
		
		return false;
	}
	
	private void addToConfig(String name) {
		Property prop = this.config.get("singularity", "default_singularities", new String[0]);
		String[] values = prop.getStringList();
		if (!Arrays.stream(values).anyMatch(s -> s.split("=")[0].equals(name))) {
			String[] newValues = new String[values.length + 1];
			for (int i = 0; i < newValues.length; i++) {
				if (i < values.length) {
					newValues[i] = values[i];
				} else {
					newValues[i] = name + "=" + ModConfig.removeSingularity(name);
				}
			}
			
			prop.setComment("Disable specific default singularities here.");
			prop.set(newValues);
			this.config.save();
		}
	}

	public static class ColorHandler implements IItemColor {

		@Override
		public int getColorFromItemstack(ItemStack stack, int tintIndex) {
			return singularityColors.get(stack.getMetadata());
		}
	}
}
