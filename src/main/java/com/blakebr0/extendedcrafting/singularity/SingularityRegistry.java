package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.lib.ModSingularities;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.network.message.SyncSingularitiesMessage;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class SingularityRegistry {
    private static final SingularityRegistry INSTANCE = new SingularityRegistry();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private final Map<ResourceLocation, Singularity> singularities = new LinkedHashMap<>();

    @SubscribeEvent
    public void onDatapackSync(OnDatapackSyncEvent event) {
        var message = new SyncSingularitiesMessage(this.getSingularities());
        var player = event.getPlayer();

        if (player != null) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
        } else {
            NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), message);
        }
    }

    public void onResourceManagerReload(ResourceManager manager) {
        this.loadSingularities();
    }

    public void loadSingularities() {
        var stopwatch = Stopwatch.createStarted();
        var dir = FMLPaths.CONFIGDIR.get().resolve("extendedcrafting/singularities/").toFile();

        this.writeDefaultSingularityFiles();

        this.singularities.clear();

        if (!dir.mkdirs() && dir.isDirectory()) {
            this.loadFiles(dir);
        }

        stopwatch.stop();

        ExtendedCrafting.LOGGER.info("Loaded {} singularity type(s) in {} ms", this.singularities.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public void writeDefaultSingularityFiles() {
        var dir = FMLPaths.CONFIGDIR.get().resolve("extendedcrafting/singularities/").toFile();

        if (!dir.exists() && dir.mkdirs()) {
            for (var singularity : ModSingularities.getDefaults()) {
                var json = SingularityUtils.writeToJson(singularity);
                FileWriter writer = null;

                try {
                    var file = new File(dir, singularity.getId().getPath() + ".json");
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

    public Singularity getSingularityById(ResourceLocation id) {
        return this.singularities.get(id);
    }

    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.singularities.size());

        this.singularities.forEach((id, singularity) -> {
            singularity.write(buffer);
        });
    }

    public List<Singularity> readFromBuffer(FriendlyByteBuf buffer) {
        List<Singularity> singularities = new ArrayList<>();

        int size = buffer.readVarInt();

        for (int i = 0; i < size; i++) {
            var singularity = Singularity.read(buffer);

            singularities.add(singularity);
        }

        return singularities;
    }

    public void loadSingularities(SyncSingularitiesMessage message) {
        var singularities = message.getSingularities()
                .stream()
                .collect(Collectors.toMap(Singularity::getId, s -> s));

        this.singularities.clear();
        this.singularities.putAll(singularities);

        ExtendedCrafting.LOGGER.info("Loaded {} singularities from the server", singularities.size());
    }

    private void loadFiles(File dir) {
        var files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (files == null)
            return;

        for (var file : files) {
            JsonObject json;
            InputStreamReader reader = null;
            Singularity singularity = null;

            try {
                var parser = new JsonParser();
                reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                var name = file.getName().replace(".json", "");
                json = parser.parse(reader).getAsJsonObject();

                singularity = SingularityUtils.loadFromJson(new ResourceLocation(ExtendedCrafting.MOD_ID, name), json);

                reader.close();
            } catch (Exception e) {
                ExtendedCrafting.LOGGER.error("An error occurred while loading singularities", e);
            } finally {
                IOUtils.closeQuietly(reader);
            }

            if (singularity != null) {
                var id = singularity.getId();

                this.singularities.put(id, singularity);
            }
        }
    }

    public static SingularityRegistry getInstance() {
        return INSTANCE;
    }
}
