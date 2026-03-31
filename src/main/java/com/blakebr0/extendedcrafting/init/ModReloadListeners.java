package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.util.IngredientListCache;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

public final class ModReloadListeners implements ResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        IngredientListCache.getInstance().onResourceManagerReload(manager);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onAddReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(ExtendedCrafting.resource("reload_listeners"), this);
    }
}
