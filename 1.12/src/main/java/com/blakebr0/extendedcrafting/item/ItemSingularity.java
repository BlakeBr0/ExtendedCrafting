package com.blakebr0.extendedcrafting.item;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.item.ItemMeta;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfig;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;

public class ItemSingularity extends ItemMeta {

	public static Map<Integer, Integer> singularityColors = new HashMap<>();
	public static Map<Integer, Object> singularityMaterials = new HashMap<>();
	private Configuration config = ModConfig.instance.config;

	public ItemSingularity() {
		super("ec.singularity", ExtendedCrafting.REGISTRY);
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String name = items.containsKey(stack.getMetadata())
				? items.get(stack.getMetadata()).getName().replace("_", " ") : "Dummy";
		return WordUtils.capitalize(name) + " " + Utils.localize("item.ec.singularity.name");
	}

	@Override
	public void init() {
		addSingularity(0, "coal", StackHelper.to(Items.COAL), 0x1B1B1B);
		addSingularity(1, "iron", StackHelper.to(Items.IRON_INGOT), 0x969696);
		addSingularity(2, "lapis_lazuli", StackHelper.to(Items.DYE, 1, 4), 0x345EC3);
		addSingularity(3, "redstone", StackHelper.to(Items.REDSTONE), 0x720000);
		addSingularity(4, "glowstone", StackHelper.to(Items.GLOWSTONE_DUST), 0x868600);
		addSingularity(5, "gold", StackHelper.to(Items.GOLD_INGOT), 0xDEDE00);
		addSingularity(6, "diamond", StackHelper.to(Items.DIAMOND), 0x2CCDB1);
		addSingularity(7, "emerald", StackHelper.to(Items.EMERALD), 0x00A835);

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
			ModelLoader.setCustomModelResourceLocation(this, item.getKey(),
					new ModelResourceLocation(ExtendedCrafting.MOD_ID + ":singularity", "inventory"));
		}
	}

	public ItemStack addSingularity(int meta, String name, ItemStack material, int color) {
		singularityColors.put(meta, color);
		singularityMaterials.put(meta, material);
		boolean enabled = config.get("singularity", name, true).getBoolean();
		config.save();
		if (enabled) {
			ItemSingularityUltimate.addSingularityToRecipe(StackHelper.to(this, 1, meta));
		}
		return addItem(meta, name, enabled);
	}

	public ItemStack addSingularity(int meta, String name, String oreName, int color) {
		singularityColors.put(meta, color);
		singularityMaterials.put(meta, oreName);
		boolean enabled = config.get("singularity", name, true).getBoolean();
		config.save();
		if (enabled) {
			ItemSingularityUltimate.addSingularityToRecipe(StackHelper.to(this, 1, meta));
		}
		return addItem(meta, name, enabled);
	}
	
	

	public static class ColorHandler implements IItemColor {

		@Override
		public int getColorFromItemstack(ItemStack stack, int tintIndex) {
			return singularityColors.get(stack.getMetadata());
		}
	}
}
