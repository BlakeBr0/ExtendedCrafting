package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.crafting.DynamicRecipeManager;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ModReloadListeners implements IResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(IResourceManager manager) {
        SingularityRegistry.getInstance().onResourceManagerReload(manager);
        DynamicRecipeManager.getInstance().onResourceManagerReload(manager);
    }

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(this);
    }
}
