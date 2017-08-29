package com.blakebr0.extendedcrafting.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
	
	public static ItemMaterial itemMaterial = new ItemMaterial();
	
	public static void init(){
		itemMaterial.init();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels(){
		itemMaterial.initModels();
	}
}
