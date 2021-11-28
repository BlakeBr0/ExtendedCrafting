package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfigs;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
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

        if (!dir.exists() && dir.mkdirs()) {
            for (var singularity : defaults()) {
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

        if (!dir.mkdirs() && dir.isDirectory()) {
            this.loadFiles(dir);
        }

        stopwatch.stop();

        ExtendedCrafting.LOGGER.info("Loaded {} singularity type(s) in {} ms", this.singularities.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
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

    private static List<Singularity> defaults() {
        int count = ModConfigs.SINGULARITY_MATERIALS_REQUIRED.get();

        return List.of(
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "coal"), "singularity.extendedcrafting.coal", new int[] { 3289650, 1052693 }, Ingredient.of(Items.COAL), count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "iron"), "singularity.extendedcrafting.iron", new int[] { 14211288, 11053224 }, Ingredient.of(Items.IRON_INGOT), count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "lapis_lazuli"), "singularity.extendedcrafting.lapis_lazuli", new int[] { 5931746, 3432131 }, Ingredient.of(Items.LAPIS_LAZULI), count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "redstone"), "singularity.extendedcrafting.redstone", new int[] { 11144961, 7471104 }, Ingredient.of(Items.REDSTONE), count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "glowstone"), "singularity.extendedcrafting.glowstone", new int[] { 16759902, 11825472 }, Ingredient.of(Items.GLOWSTONE_DUST), count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "diamond"), "singularity.extendedcrafting.diamond", new int[] { 4910553, 2147765 }, Ingredient.of(Items.DIAMOND), count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "emerald"), "singularity.extendedcrafting.emerald", new int[] { 4322180, 43564 }, Ingredient.of(Items.EMERALD), count, true),

                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "aluminum"), "singularity.extendedcrafting.aluminum", new int[] { 13290714, 13290714 }, "forge:ingots/aluminum", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "copper"), "singularity.extendedcrafting.copper", new int[] { 13529601, 13529601 }, "forge:ingots/copper", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "tin"), "singularity.extendedcrafting.tin", new int[] { 7770277, 7770277 }, "forge:ingots/tin", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "bronze"), "singularity.extendedcrafting.bronze", new int[] { 11040068, 11040068 }, "forge:ingots/bronze", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "silver"), "singularity.extendedcrafting.silver", new int[] { 8628914, 8628914 }, "forge:ingots/silver", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "lead"), "singularity.extendedcrafting.lead", new int[] { 4738919, 4738919 }, "forge:ingots/lead", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "steel"), "singularity.extendedcrafting.steel", new int[] { 5658198, 5658198 }, "forge:ingots/steel", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "nickel"), "singularity.extendedcrafting.nickel", new int[] { 12498050, 12498050 }, "forge:ingots/nickel", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "electrum"), "singularity.extendedcrafting.electrum", new int[] { 10981685, 10981685 }, "forge:ingots/electrum", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "invar"), "singularity.extendedcrafting.invar", new int[] { 9608599, 9608599 }, "forge:ingots/invar", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "uranium"), "singularity.extendedcrafting.uranium", new int[] { 4620301, 4620301 }, "forge:ingots/uranium", count, true),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "platinum"), "singularity.extendedcrafting.platinum", new int[] { 7334639, 7334639 }, "forge:ingots/platinum", count, true)
        );
    }
}
