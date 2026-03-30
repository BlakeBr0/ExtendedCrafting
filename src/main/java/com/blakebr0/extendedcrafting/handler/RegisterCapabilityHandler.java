package com.blakebr0.extendedcrafting.handler;

import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class RegisterCapabilityHandler {
    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModTileEntities.CRAFTING_CORE.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModTileEntities.BASIC_AUTO_TABLE.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModTileEntities.ADVANCED_AUTO_TABLE.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModTileEntities.ELITE_AUTO_TABLE.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModTileEntities.ULTIMATE_AUTO_TABLE.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModTileEntities.COMPRESSOR.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModTileEntities.AUTO_ENDER_CRAFTER.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModTileEntities.FLUX_ALTERNATOR.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModTileEntities.AUTO_FLUX_CRAFTER.get(), (block, direction) -> block.getEnergy());

        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.PEDESTAL.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.CRAFTING_CORE.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.BASIC_TABLE.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.ADVANCED_TABLE.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.ELITE_TABLE.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.ULTIMATE_TABLE.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.BASIC_AUTO_TABLE.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.ADVANCED_AUTO_TABLE.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.ELITE_AUTO_TABLE.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.ULTIMATE_AUTO_TABLE.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.COMPRESSOR.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.ENDER_CRAFTER.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.AUTO_ENDER_CRAFTER.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.FLUX_CRAFTER.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModTileEntities.AUTO_FLUX_CRAFTER.get(), (block, direction) -> block.getInventory());
    }
}
