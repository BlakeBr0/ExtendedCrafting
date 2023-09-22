package com.blakebr0.extendedcrafting.lib;

import com.blakebr0.cucumber.util.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModList;

public final class ModTooltips {
    public static final Tooltip EMPTY = new Tooltip("tooltip.extendedcrafting.empty");
    public static final Tooltip EJECT = new Tooltip("tooltip.extendedcrafting.eject");
    public static final Tooltip EJECTING = new Tooltip("tooltip.extendedcrafting.ejecting");
    public static final Tooltip LIMITED_INPUT = new Tooltip("tooltip.extendedcrafting.limited_input");
    public static final Tooltip UNLIMITED_INPUT = new Tooltip("tooltip.extendedcrafting.unlimited_input");
    public static final Tooltip TOGGLE_AUTO_CRAFTING = new Tooltip("tooltip.extendedcrafting.toggle_auto_crafting");
    public static final Tooltip SELECTED = new Tooltip("tooltip.extendedcrafting.selected");
    public static final Tooltip TIER = new Tooltip("tooltip.extendedcrafting.tier");
    public static final Tooltip CRAFTING = new Tooltip("tooltip.extendedcrafting.crafting");
    public static final Tooltip TYPE = new Tooltip("tooltip.extendedcrafting.type");
    public static final Tooltip MODE = new Tooltip("tooltip.extendedcrafting.mode");
    public static final Tooltip NUM_ITEMS = new Tooltip("tooltip.extendedcrafting.num_items");
    public static final Tooltip AND_X_MORE = new Tooltip("tooltip.extendedcrafting.and_x_more");
    public static final Tooltip TICKS = new Tooltip("tooltip.extendedcrafting.ticks");
    public static final Tooltip SECONDS = new Tooltip("tooltip.extendedcrafting.seconds");
    public static final Tooltip REQUIRES_TABLE = new Tooltip("tooltip.extendedcrafting.requires_table");
    public static final Tooltip ITEMS_REQUIRED = new Tooltip("tooltip.extendedcrafting.items_required");
    public static final Tooltip PER_ALTERNATOR = new Tooltip("tooltip.extendedcrafting.per_alternator");
    public static final Tooltip RECIPE_COUNT = new Tooltip("tooltip.extendedcrafting.recipe_count");
    public static final Tooltip SINGULARITY_ID = new Tooltip("tooltip.extendedcrafting.singularity_id");
    public static final Tooltip ADDED_BY = new Tooltip("tooltip.extendedcrafting.added_by");
    public static final Tooltip AUTO_TABLE_SAVE_RECIPE = new Tooltip("tooltip.extendedcrafting.auto_table_save_recipe");
    public static final Tooltip AUTO_TABLE_DELETE_RECIPE = new Tooltip("tooltip.extendedcrafting.auto_table_delete_recipe");

    public static Component getAddedByTooltip(String modid) {
        var name = ModList.get().getModFileById(modid).getMods().get(0).getDisplayName();
        return ADDED_BY.args(name).build();
    }
}
