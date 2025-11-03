package com.blakebr0.extendedcrafting.util;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class IngredientListCache {
    private static final IngredientListCache INSTANCE = new IngredientListCache();
    private final Object2ObjectOpenHashMap<Recipe<?>, List<List<Component>>> lists;

    private IngredientListCache() {
        this.lists = new Object2ObjectOpenHashMap<>();
    }

    public List<Component> getIngredientsList(Recipe<?> recipe, Supplier<NonNullList<Ingredient>> ingredients) {
        return this.lists.computeIfAbsent(recipe, r -> createIngredientsList(ingredients.get()))
                .stream()
                .map(l -> {
                    var index = (System.currentTimeMillis() / 2000L) % l.size();
                    return l.get(Math.toIntExact(index));
                })
                .toList();
    }

    public void onResourceManagerReload(ResourceManager manager) {
        this.lists.clear();
    }

    public static IngredientListCache getInstance() {
        return INSTANCE;
    }

    private static List<List<Component>> createIngredientsList(NonNullList<Ingredient> ingredients) {
        var lists = new ArrayList<ItemList>();

        for (var ingredient : ingredients) {
            var items = Arrays.stream(ingredient.getItems()).map(ItemStack::getItem).toList();
            var matched = false;

            // increment quantity if there's already a matching list
            for (var list : lists) {
                if (list.containsAll(items)) {
                    list.quantity++;
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                lists.add(new ItemList(items));
            }
        }

        return lists.stream()
                .map(s -> s.items.stream()
                        .map(i -> (Component) Component.literal(s.quantity + "x ").append(i.getDefaultInstance().getHoverName()))
                        .toList()
                )
                .toList();
    }

    private static class ItemList {
        private final Set<Item> itemSet;
        public final List<Item> items;
        public int quantity;

        public ItemList(List<Item> items) {
            this.itemSet = new HashSet<>(items);
            this.items = items;
            this.quantity = 1;
        }

        public boolean containsAll(Collection<Item> items) {
            return this.itemSet.containsAll(items);
        }
    }
}
