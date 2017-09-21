package com.blakebr0.extendedcrafting.item;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.blakebr0.cucumber.item.ItemMeta;
import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.google.common.collect.Lists;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMaterial extends ItemMeta {
	
	public static Set<Integer> ultimateMetas = new HashSet<>(Arrays.asList(13, 19, 32, 33));

	public static ItemStack itemBlackIron;
	public static ItemStack itemBlackIronNugget;
	public static ItemStack itemBlackIronSlate;
	public static ItemStack itemBlackIronRod;

	public static ItemStack itemLuminessence;

	public static ItemStack itemBasicCraftingCatalyst;
	public static ItemStack itemAdvancedCraftingCatalyst;
	public static ItemStack itemEliteCraftingCatalyst;
	public static ItemStack itemUltimateCraftingCatalyst;

	public static ItemStack itemBasicCraftingComponent;
	public static ItemStack itemAdvancedCraftingComponent;
	public static ItemStack itemEliteCraftingComponent;
	public static ItemStack itemUltimateCraftingComponent;
	
	public static ItemStack itemCrystaltineIngot;
	public static ItemStack itemCrystaltineNugget;
	public static ItemStack itemCrystaltineCatalyst;
	public static ItemStack itemCrystaltineComponent;
	
	public static ItemStack itemUltimateIngot;
	public static ItemStack itemUltimateNugget;
	public static ItemStack itemUltimateCatalyst;
	public static ItemStack itemUltimateComponent;
	
	public static ItemStack itemDiamondNugget;
	public static ItemStack itemEmeraldNugget;
	
	public static ItemStack itemNetherStarNugget;

	public ItemMaterial() {
		super("ec.material", ExtendedCrafting.REGISTRY);
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
	}

	@Override
	public void init() {
		itemBlackIron = addItem(0, "black_iron", "ingotBlackIron");
		itemBlackIronNugget = addItem(1, "black_iron_nugget", "nuggetBlackIron");
		itemBlackIronSlate = addItem(2, "black_iron_slate");
		itemBlackIronRod = addItem(3, "black_iron_rod");

		itemLuminessence = addItem(7, "luminessence");

		itemBasicCraftingCatalyst = addItem(8, "basic_catalyst");
		itemAdvancedCraftingCatalyst = addItem(9, "advanced_catalyst");
		itemEliteCraftingCatalyst = addItem(10, "elite_catalyst");
		itemUltimateCraftingCatalyst = addItem(11, "ultimate_catalyst");
		itemCrystaltineCatalyst = addItem(12, "crystaltine_catalyst");
		itemUltimateCatalyst = addItem(13, "ultimater_catalyst");

		itemBasicCraftingComponent = addItem(14, "basic_component");
		itemAdvancedCraftingComponent = addItem(15, "advanced_component");
		itemEliteCraftingComponent = addItem(16, "elite_component");
		itemUltimateCraftingComponent = addItem(17, "ultimate_component");
		itemCrystaltineComponent = addItem(18, "crystaltine_component");
		itemUltimateComponent = addItem(19, "ultimater_component");

		itemCrystaltineIngot = addItem(24, "crystaltine_ingot", "ingotCrystaltine");
		itemCrystaltineNugget = addItem(25, "crystaltine_nugget", "nuggetCrystaltine");
		
		itemUltimateIngot = addItem(32, "ultimate_ingot", "ingotUltimate");
		itemUltimateNugget = addItem(33, "ultimate_nugget", "nuggetUltimate");
		
		itemDiamondNugget = addItem(128, "diamond_nugget", "nuggetDiamond");
		itemEmeraldNugget = addItem(129, "emerald_nugget", "nuggetEmerald");
		
		itemNetherStarNugget = addItem(140, "nether_star_nugget", "nuggetNetherStar");
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return ultimateMetas.contains(stack.getMetadata());
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return ultimateMetas.contains(stack.getMetadata()) ? EnumRarity.EPIC : EnumRarity.COMMON;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		switch (stack.getMetadata()) {
		case 13:
			tooltip.add(Colors.ITALICS + Utils.localize("tooltip.ec.ultimate_catalyst"));
			break;
		case 19:
			tooltip.add(Colors.ITALICS + Utils.localize("tooltip.ec.ultimate_component"));
			break;
		case 32:
			tooltip.add(Colors.ITALICS + Utils.localize("tooltip.ec.ultimate_ingot"));
			break;
		case 33:
			tooltip.add(Colors.ITALICS + Utils.localize("tooltip.ec.ultimate_nugget"));
			break;
		default:
			break;
		}
	}
}
