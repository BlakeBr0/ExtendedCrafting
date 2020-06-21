package com.blakebr0.extendedcrafting.network;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.network.message.EjectModeSwitchMessage;
import com.blakebr0.extendedcrafting.network.message.InputLimitSwitchMessage;
import com.blakebr0.extendedcrafting.network.message.RunningSwitchMessage;
import com.blakebr0.extendedcrafting.network.message.SaveRecipeMessage;
import com.blakebr0.extendedcrafting.network.message.SelectRecipeMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ExtendedCrafting.MOD_ID, "main"), () -> "1.0", (s) -> true, (s) -> true);
	private static int id = 0;

	public static void onCommonSetup() {
		INSTANCE.registerMessage(id(), EjectModeSwitchMessage.class, EjectModeSwitchMessage::write, EjectModeSwitchMessage::read, EjectModeSwitchMessage::onMessage);
		INSTANCE.registerMessage(id(), InputLimitSwitchMessage.class, InputLimitSwitchMessage::write, InputLimitSwitchMessage::read, InputLimitSwitchMessage::onMessage);
		INSTANCE.registerMessage(id(), RunningSwitchMessage.class, RunningSwitchMessage::write, RunningSwitchMessage::read, RunningSwitchMessage::onMessage);
		INSTANCE.registerMessage(id(), SelectRecipeMessage.class, SelectRecipeMessage::write, SelectRecipeMessage::read, SelectRecipeMessage::onMessage);
		INSTANCE.registerMessage(id(), SaveRecipeMessage.class, SaveRecipeMessage::write, SaveRecipeMessage::read, SaveRecipeMessage::onMessage);
	}

	private static int id() {
		return id++;
	}
}
