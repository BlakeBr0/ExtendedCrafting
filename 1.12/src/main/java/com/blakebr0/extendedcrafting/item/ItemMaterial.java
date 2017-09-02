package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.item.ItemMeta;
import com.blakebr0.extendedcrafting.ExtendedCrafting;

import net.minecraft.item.ItemStack;

public class ItemMaterial extends ItemMeta {

	public static ItemStack itemBlackIron;
	public static ItemStack itemBlackIronNugget;
	public static ItemStack itemBlackIronSlate;

	public static ItemStack itemLuminessence;

	public static ItemStack itemBasicCatalyst;
	public static ItemStack itemAdvancedCatalyst;
	public static ItemStack itemEliteCatalyst;
	public static ItemStack itemUltimateCatalyst;

	public static ItemStack itemBasicComponent;
	public static ItemStack itemAdvancedComponent;
	public static ItemStack itemEliteComponent;
	public static ItemStack itemUltimateComponent;
	
	public static ItemStack itemDiamondNugget;
	public static ItemStack itemEmeraldNugget;

	public ItemMaterial() {
		super("ec.material", ExtendedCrafting.REGISTRY);
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
	}

	@Override
	public void init() {
		itemBlackIron = addItem(0, "black_iron", "ingotBlackIron");
		itemBlackIronNugget = addItem(1, "black_iron_nugget", "nuggetBlackIron");
		itemBlackIronSlate = addItem(2, "black_iron_slate");

		itemLuminessence = addItem(7, "luminessence");

		itemBasicCatalyst = addItem(8, "basic_catalyst");
		itemAdvancedCatalyst = addItem(9, "advanced_catalyst");
		itemEliteCatalyst = addItem(10, "elite_catalyst");
		itemUltimateCatalyst = addItem(11, "ultimate_catalyst");

		itemBasicComponent = addItem(14, "basic_component");
		itemAdvancedComponent = addItem(15, "advanced_component");
		itemEliteComponent = addItem(16, "elite_component");
		itemUltimateComponent = addItem(17, "ultimate_component");
		
		itemDiamondNugget = addItem(128, "diamond_nugget", "nuggetDiamond");
		itemEmeraldNugget = addItem(129, "emerald_nugget", "nuggetEmerald");
	}
}
