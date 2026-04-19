package com.blakebr0.extendedcrafting.lib;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.singularity.DefaultSingularity;

import java.util.List;

public final class ModSingularities {
    public static List<DefaultSingularity> createAll() {
        return List.of(
                create("coal", 0x363739, 0x261E24, "minecraft:coal"),
                create("copper", 0xFA977C, 0xBC5430, "minecraft:copper_ingot"),
                create("iron", 0xE1E1E1, 0x6C6C6C, "minecraft:iron_ingot"),
                create("lapis_lazuli", 0x678DEA, 0x1B53A7, "minecraft:lapis_lazuli"),
                create("redstone", 0xFF0000, 0x8A0901, "minecraft:redstone"),
                create("glowstone", 0xFFD38F, 0xA06135, "minecraft:glowstone_dust"),
                create("gold", 0xFDF55F, 0xD98E04, "minecraft:gold_ingot"),
                create("diamond", 0xA6FCE9, 0x1AACA8, "minecraft:diamond"),
                create("emerald", 0x7DF8AC, 0x008E1A, "minecraft:emerald"),

                create("aluminum", 0xCACCDA, 0x9A9CA6, "#c:ingots/aluminum"),
                create("tin", 0xA0BEBD, 0x527889, "#c:ingots/tin"),
                create("bronze", 0xD99F43, 0xBB6B3B, "#c:ingots/bronze"),
                create("silver", 0xC0CDD2, 0x5F6E7C, "#c:ingots/silver"),
                create("lead", 0x6C7D92, 0x323562, "#c:ingots/lead"),
                create("steel", 0x565656, 0x232323, "#c:ingots/steel"),
                create("nickel", 0xE1D798, 0xB1976C, "#c:ingots/nickel"),
                create("electrum", 0xF5F18E, 0x9E8D3E, "#c:ingots/electrum"),
                create("invar", 0xBCC5BB, 0x5D7877, "#c:ingots/invar"),
                create("platinum", 0x6FEAEF, 0x57B8BC, "#c:ingots/platinum")
        );
    }

    private static DefaultSingularity create(String name, int overlayColor, int underlayColor, String ingredient) {
        var id = ExtendedCrafting.resource(name);
        var translationKey = "singularity.extendedcrafting." + name;
        return new DefaultSingularity(id, translationKey, overlayColor, underlayColor, ingredient);
    }
}
