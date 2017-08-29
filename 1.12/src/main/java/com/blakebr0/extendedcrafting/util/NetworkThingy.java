package com.blakebr0.extendedcrafting.util;

import com.blakebr0.extendedcrafting.ExtendedCrafting;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkThingy {

	public static final SimpleNetworkWrapper THINGY = new SimpleNetworkWrapper(ExtendedCrafting.MOD_ID);

	public static void init() {
		THINGY.registerMessage(EjectModeSwitchPacket.Handler.class, EjectModeSwitchPacket.class, 1, Side.SERVER);
	}
}
