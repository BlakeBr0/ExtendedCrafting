package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.crafting.recipe.UltimateSingularityRecipe;
import com.blakebr0.extendedcrafting.lib.ModSingularities;
import com.blakebr0.extendedcrafting.network.payload.SyncSingularitiesPayload;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class SingularityRegistry {
    private static final SingularityRegistry INSTANCE = new SingularityRegistry();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private final Map<Identifier, Singularity> singularities = new LinkedHashMap<>();

    @SubscribeEvent
    public void onDatapackSync(OnDatapackSyncEvent event) {
        var payload = new SyncSingularitiesPayload(this.getSingularities());
        var player = event.getPlayer();

        if (player != null) {
            PacketDistributor.sendToPlayer(player, payload);
        } else {
            PacketDistributor.sendToAllPlayers(payload);
        }
    }

    public void loadSingularities(HolderLookup.Provider registries) {
        var stopwatch = Stopwatch.createStarted();
        var dir = FMLPaths.CONFIGDIR.get().resolve("extendedcrafting/singularities/").toFile();

        this.writeDefaultSingularityFiles();

        this.singularities.clear();

        if (!dir.mkdirs() && dir.isDirectory()) {
            this.loadFiles(dir, registries);
        }
        
        UltimateSingularityRecipe.invalidate();

        stopwatch.stop();

        ExtendedCrafting.LOGGER.info("Loaded {} singularity type(s) in {} ms", this.singularities.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public void writeDefaultSingularityFiles() {
        var dir = FMLPaths.CONFIGDIR.get().resolve("extendedcrafting/singularities/").toFile();

        if (!dir.exists() && dir.mkdirs()) {
            for (var singularity : ModSingularities.createAll()) {
                var json = singularity.toJson();
                FileWriter writer = null;

                try {
                    var file = new File(dir, singularity.id().getPath() + ".json");
                    writer = new FileWriter(file);

                    GSON.toJson(json, writer);
                    writer.close();
                } catch (Exception e) {
                    ExtendedCrafting.LOGGER.error("An error occurred while generating default singularities", e);
                } finally {
                    IOUtils.closeQuietly(writer);
                }
            }
        }
    }

    public List<Singularity> getSingularities() {
        return Lists.newArrayList(this.singularities.values());
    }

    public Singularity getSingularityById(Identifier id) {
        return this.singularities.get(id);
    }

    public void loadSingularities(SyncSingularitiesPayload payload) {
        var singularities = payload.singularities()
                .stream()
                .collect(Collectors.toMap(Singularity::getId, s -> s));

        this.singularities.clear();
        this.singularities.putAll(singularities);

        UltimateSingularityRecipe.invalidate();

        ExtendedCrafting.LOGGER.info("Loaded {} singularities from the server", singularities.size());
    }

    private void loadFiles(File dir, HolderLookup.Provider registries) {
        var files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (files == null)
            return;

        var ops = registries.createSerializationContext(JsonOps.INSTANCE);

        for (var file : files) {
            JsonObject json;
            InputStreamReader reader = null;
            Singularity singularity = null;

            try {
                reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                var id = ExtendedCrafting.resource(file.getName().replace(".json", ""));
                json = JsonParser.parseReader(reader).getAsJsonObject();

                // we'll remove tag ingredients if the tag is empty
                var ingredient = GsonHelper.getAsString(json, "ingredient");
                if (ingredient.startsWith("#")) {
                    var tag = registries.get(ItemTags.create(Identifier.parse(ingredient.substring(1))));
                    if (tag.isEmpty()) {
                        json.remove("ingredient");
                    }
                }

                singularity = Singularity.CODEC.decode(ops, json).getOrThrow().getFirst();
                singularity.setId(id);

                reader.close();
            } catch (Exception e) {
                ExtendedCrafting.LOGGER.error("An error occurred while loading singularities", e);
            } finally {
                IOUtils.closeQuietly(reader);
            }

            if (singularity != null && singularity.isEnabled()) {
                this.singularities.put(singularity.getId(), singularity);
            }
        }
    }

    public static SingularityRegistry getInstance() {
        return INSTANCE;
    }
}
