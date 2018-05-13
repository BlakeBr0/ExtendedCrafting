package com.blakebr0.extendedcrafting.item;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.blakebr0.cucumber.item.ItemMeta;
import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMaterial extends ItemMeta {
	
	public static Set<Integer> ultimateMetas = new HashSet<>(Arrays.asList(13, 19, 32, 33, 40, 41, 48, 49));

	public static ItemStack itemBlackIron;
	public static ItemStack itemBlackIronNugget;
	public static ItemStack itemBlackIronSlate;
	public static ItemStack itemBlackIronRod;

	public static ItemStack itemLuminessence;

	public static ItemStack itemBasicCatalyst;
	public static ItemStack itemAdvancedCatalyst;
	public static ItemStack itemEliteCatalyst;
	public static ItemStack itemUltimateCatalyst;
	public static ItemStack itemCrystaltineCatalyst;
	public static ItemStack itemTheUltimateCatalyst;

	public static ItemStack itemBasicComponent;
	public static ItemStack itemAdvancedComponent;
	public static ItemStack itemEliteComponent;
	public static ItemStack itemUltimateComponent;
	public static ItemStack itemCrystaltineComponent;
	public static ItemStack itemTheUltimateComponent;

	public static ItemStack itemCrystaltineIngot;
	public static ItemStack itemCrystaltineNugget;
	
	public static ItemStack itemTheUltimateIngot;
	public static ItemStack itemTheUltimateNugget;
	
	public static ItemStack itemEnderIngot;
	public static ItemStack itemEnderNugget;
	
	public static ItemStack itemEnderStar;
	public static ItemStack itemEnderStarNugget;
	
	public static ItemStack itemEnhancedEnderIngot;
	public static ItemStack itemEnhancedEnderNugget;
	
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

		itemBasicCatalyst = addItem(8, "basic_catalyst");
		itemAdvancedCatalyst = addItem(9, "advanced_catalyst");
		itemEliteCatalyst = addItem(10, "elite_catalyst");
		itemUltimateCatalyst = addItem(11, "ultimate_catalyst");
		itemCrystaltineCatalyst = addItem(12, "crystaltine_catalyst");
		itemTheUltimateCatalyst = addItem(13, "ultimater_catalyst");

		itemBasicComponent = addItem(14, "basic_component");
		itemAdvancedComponent = addItem(15, "advanced_component");
		itemEliteComponent = addItem(16, "elite_component");
		itemUltimateComponent = addItem(17, "ultimate_component");
		itemCrystaltineComponent = addItem(18, "crystaltine_component");
		itemTheUltimateComponent = addItem(19, "ultimater_component");

		itemCrystaltineIngot = addItem(24, "crystaltine_ingot", "ingotCrystaltine");
		itemCrystaltineNugget = addItem(25, "crystaltine_nugget", "nuggetCrystaltine");
		
		itemTheUltimateIngot = addItem(32, "ultimate_ingot", "ingotUltimate");
		itemTheUltimateNugget = addItem(33, "ultimate_nugget", "nuggetUltimate");
		
		itemEnderIngot = addItem(36, "ender_ingot");
		itemEnderNugget = addItem(37, "ender_nugget");
		
		itemEnderStar = addItem(40, "ender_star");
		itemEnderStarNugget = addItem(41, "ender_star_nugget");
		
		itemEnhancedEnderIngot = addItem(48, "enhanced_ender_ingot");
		itemEnhancedEnderNugget = addItem(49, "enhanced_ender_nugget");
				
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
