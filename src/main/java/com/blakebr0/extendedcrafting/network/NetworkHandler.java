package com.blakebr0.extendedcrafting.network;

import com.blakebr0.cucumber.network.BaseNetworkHandler;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.network.message.EjectModeSwitchMessage;
import com.blakebr0.extendedcrafting.network.message.InputLimitSwitchMessage;
import com.blakebr0.extendedcrafting.network.message.RunningSwitchMessage;
import com.blakebr0.extendedcrafting.network.message.SaveRecipeMessage;
import com.blakebr0.extendedcrafting.network.message.SelectRecipeMessage;
import com.blakebr0.extendedcrafting.network.message.SyncSingularitiesMessage;
import net.minecraft.resources.ResourceLocation;

public class NetworkHandler {
	public static final BaseNetworkHandler INSTANCE = new BaseNetworkHandler(new ResourceLocation(ExtendedCrafting.MOD_ID, "main"));

	public static void onCommonSetup() {
		INSTANCE.register(EjectModeSwitchMessage.class, new EjectModeSwitchMessage());
		INSTANCE.register(InputLimitSwitchMessage.class, new InputLimitSwitchMessage());
		INSTANCE.register(RunningSwitchMessage.class, new RunningSwitchMessage());
		INSTANCE.register(SelectRecipeMessage.class, new SelectRecipeMessage());
		INSTANCE.register(SaveRecipeMessage.class, new SaveRecipeMessage());
		INSTANCE.register(SyncSingularitiesMessage.class, new SyncSingularitiesMessage());
	}
}
