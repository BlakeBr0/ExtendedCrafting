package com.blakebr0.extendedcrafting.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemMaterial extends ItemMeta {

	public static ItemStack blackIron;
	
	public static ItemStack luminousIron;
	public static ItemStack luminousGold;
	public static ItemStack luminousDiamond;
	public static ItemStack luminousEmerald;
	
	public static ItemStack blackIronNugget;
	
	public static ItemStack luminousIronNugget;
	public static ItemStack luminousGoldNugget;
	
	public ItemMaterial(){
		super("material");
	}

	@Override
	public void init(){
		GameRegistry.register(this);
		
		blackIron = addItem(0, "black_iron", "ingotBlackIron");
		
		luminousIron = addItem(1, "luminous_iron", "ingotLuminousIron");
		luminousGold = addItem(2, "luminous_gold", "ingotLuminousGold");
		luminousDiamond = addItem(3, "luminous_diamond", "gemLuminousDiamond");
		luminousEmerald = addItem(4, "luminous_emerald", "gemLuminousEmerald");
		
		blackIronNugget = addItem(5, "black_iron_nugget", "nuggetBlackIron");
		
		luminousIronNugget = addItem(6, "luminous_iron_nugget", "nuggetLuminousIron");
		luminousGoldNugget = addItem(7, "luminous_gold_nugget", "nuggetLuminousGold");
	}
}
