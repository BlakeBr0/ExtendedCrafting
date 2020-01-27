package com.blakebr0.extendedcrafting.network;

import com.blakebr0.extendedcrafting.ExtendedCrafting;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ExtendedCrafting.MOD_ID, "main"), () -> "1.0", (s) -> true, (s) -> true);
	private static int id = 0;

	public static void onCommonSetup() {
		INSTANCE.registerMessage(id(), ToggleHoverMessage.class, ToggleHoverMessage::write, ToggleHoverMessage::read, ToggleHoverMessage::onMessage);
		INSTANCE.registerMessage(id(), UpdateInputMessage.class, UpdateInputMessage::write, UpdateInputMessage::read, UpdateInputMessage::onMessage);
		INSTANCE.registerMessage(id(), ToggleEngineMessage.class, ToggleEngineMessage::write, ToggleEngineMessage::read, ToggleEngineMessage::onMessage);
	}

	private static int id() {
		return id++;
	}

	public static void init() {
		THINGY.registerMessage(EjectModeSwitchPacket.Handler.class, EjectModeSwitchPacket.class, 1, Side.SERVER);
		THINGY.registerMessage(InterfaceRecipeChangePacket.Handler.class, InterfaceRecipeChangePacket.class, 2, Side.SERVER);
		THINGY.registerMessage(InterfaceAutoChangePacket.Handler.class, InterfaceAutoChangePacket.class, 3, Side.SERVER);
		THINGY.registerMessage(InputLimitSwitchPacket.Handler.class, InputLimitSwitchPacket.class, 4, Side.SERVER);
	}
}
