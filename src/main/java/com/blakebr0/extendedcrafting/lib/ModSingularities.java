package com.blakebr0.extendedcrafting.lib;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public final class ModSingularities {
    public static final Singularity COAL = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "coal"), "singularity.extendedcrafting.coal", new int[] { 3289650, 1052693 }, Ingredient.of(Items.COAL));
    public static final Singularity IRON = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "iron"), "singularity.extendedcrafting.iron", new int[] { 14211288, 11053224 }, Ingredient.of(Items.IRON_INGOT));
    public static final Singularity LAPIS_LAZULI = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "lapis_lazuli"), "singularity.extendedcrafting.lapis_lazuli", new int[] { 5931746, 3432131 }, Ingredient.of(Items.LAPIS_LAZULI));
    public static final Singularity REDSTONE = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "redstone"), "singularity.extendedcrafting.redstone", new int[] { 11144961, 7471104 }, Ingredient.of(Items.REDSTONE));
    public static final Singularity GLOWSTONE = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "glowstone"), "singularity.extendedcrafting.glowstone", new int[] { 16759902, 11825472 }, Ingredient.of(Items.GLOWSTONE_DUST));
    public static final Singularity DIAMOND = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "diamond"), "singularity.extendedcrafting.diamond", new int[] { 4910553, 2147765 }, Ingredient.of(Items.DIAMOND));
    public static final Singularity EMERALD = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "emerald"), "singularity.extendedcrafting.emerald", new int[] { 4322180, 43564 }, Ingredient.of(Items.EMERALD));

    public static final Singularity ALUMINUM = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "aluminum"), "singularity.extendedcrafting.aluminum", new int[] { 13290714, 13290714 }, "forge:ingots/aluminum");
    public static final Singularity COPPER = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "copper"), "singularity.extendedcrafting.copper", new int[] { 13529601, 13529601 }, "forge:ingots/copper");
    public static final Singularity TIN = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "tin"), "singularity.extendedcrafting.tin", new int[] { 7770277, 7770277 }, "forge:ingots/tin");
    public static final Singularity BRONZE = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "bronze"), "singularity.extendedcrafting.bronze", new int[] { 11040068, 11040068 }, "forge:ingots/bronze");
    public static final Singularity SILVER = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "silver"), "singularity.extendedcrafting.silver", new int[] { 8628914, 8628914 }, "forge:ingots/silver");
    public static final Singularity LEAD = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "lead"), "singularity.extendedcrafting.lead", new int[] { 4738919, 4738919 }, "forge:ingots/lead");
    public static final Singularity STEEL = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "steel"), "singularity.extendedcrafting.steel", new int[] { 5658198, 5658198 }, "forge:ingots/steel");
    public static final Singularity NICKEL = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "nickel"), "singularity.extendedcrafting.nickel", new int[] { 12498050, 12498050 }, "forge:ingots/nickel");
    public static final Singularity ELECTRUM = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "electrum"), "singularity.extendedcrafting.electrum", new int[] { 10981685, 10981685 }, "forge:ingots/electrum");
    public static final Singularity INVAR = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "invar"), "singularity.extendedcrafting.invar", new int[] { 9608599, 9608599 }, "forge:ingots/invar");
    public static final Singularity URANIUM = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "uranium"), "singularity.extendedcrafting.uranium", new int[] { 4620301, 4620301 }, "forge:ingots/uranium");
    public static final Singularity PLATINUM = new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "platinum"), "singularity.extendedcrafting.platinum", new int[] { 7334639, 7334639 }, "forge:ingots/platinum");

    public static List<Singularity> getDefaults() {
        return List.of(
                COAL,
                IRON,
                LAPIS_LAZULI,
                REDSTONE,
                GLOWSTONE,
                DIAMOND,
                EMERALD,

                ALUMINUM,
                COPPER,
                TIN,
                BRONZE,
                SILVER,
                LEAD,
                STEEL,
                NICKEL,
                ELECTRUM,
                INVAR,
                URANIUM,
                PLATINUM
        );
    }
}
