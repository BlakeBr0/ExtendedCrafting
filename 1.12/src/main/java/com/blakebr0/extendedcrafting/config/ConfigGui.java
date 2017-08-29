package com.blakebr0.extendedcrafting.config;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.extendedcrafting.ExtendedCrafting;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigGui extends GuiConfig {

	public ConfigGui(GuiScreen parent) {
		super(parent, getConfigElements(), ExtendedCrafting.MOD_ID, false, false,
				GuiConfig.getAbridgedConfigPath(ModConfig.config.toString()));
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		for (String category : ModConfig.config.getCategoryNames()) {
			list.add(new ConfigElement(ModConfig.config.getCategory(category)));
		}

		return list;
	}
}
