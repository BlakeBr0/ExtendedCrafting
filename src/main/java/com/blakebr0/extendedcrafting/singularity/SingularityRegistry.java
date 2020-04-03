package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SingularityRegistry {
    private static final Logger LOGGER = LogManager.getLogger(ExtendedCrafting.NAME);
    private static final SingularityRegistry INSTANCE = new SingularityRegistry();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private final List<Singularity> singularities = new ArrayList<>();

    public void loadSingularities() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        File dir = FMLPaths.CONFIGDIR.get().resolve("extendedcrafting/singularities/").toFile();

        if (!dir.exists() && dir.mkdirs()) {
            for (Singularity singularity : defaults()) {
                JsonObject json = SingularityUtils.writeToJson(singularity);
                FileWriter writer = null;
                try {
                    File file = new File(dir, singularity.getId().getPath() + ".json");
                    writer = new FileWriter(file);
                    GSON.toJson(json, writer);
                    writer.close();
                } catch (Exception e) {
                    LOGGER.error("An error occurred while generating default singularities", e);
                } finally {
                    IOUtils.closeQuietly(writer);
                }
            }
        }

        if (!dir.mkdirs() && dir.isDirectory()) {
            this.loadFiles(dir);
        }

        stopwatch.stop();
        LOGGER.info("Loaded {} singularity type(s) in {} ms", this.singularities.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public List<Singularity> getSingularities() {
        return this.singularities;
    }

    public Singularity getSingularityById(ResourceLocation id) {
        return this.singularities.stream().filter(s -> id.equals(s.getId())).findFirst().orElse(null);
    }

    private void loadFiles(File dir) {
        File[] files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (files == null)
            return;

        for (File file : files) {
            JsonObject json;
            FileReader reader = null;
            Singularity singularity = null;
            try {
                JsonParser parser = new JsonParser();
                reader = new FileReader(file);
                json = parser.parse(reader).getAsJsonObject();
                String name = file.getName().replace(".json", "");
                singularity = SingularityUtils.loadFromJson(new ResourceLocation(ExtendedCrafting.MOD_ID, name), json);

                reader.close();
            } catch (Exception e) {
                LOGGER.error("An error occurred while loading singularities", e);
            } finally {
                IOUtils.closeQuietly(reader);
            }

            if (singularity != null) {
                ResourceLocation id = singularity.getId();
                this.singularities.removeIf(s -> id.equals(s.getId()));
                this.singularities.add(singularity);
            }
        }
    }

    public static SingularityRegistry getInstance() {
        return INSTANCE;
    }

    private static List<Singularity> defaults() {
        int count = ModConfigs.SINGULARITY_MATERIALS_REQUIRED.get();
        return Lists.newArrayList(
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "coal"), "singularity.extendedcrafting.coal", new int[] { 3289650, 1052693 }, Ingredient.fromItems(Items.COAL), count),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "iron"), "singularity.extendedcrafting.iron", new int[] { 14211288, 11053224 }, Ingredient.fromTag(Tags.Items.INGOTS_IRON), count),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "lapis_lazuli"), "singularity.extendedcrafting.lapis_lazuli", new int[] { 5931746, 3432131 }, Ingredient.fromTag(Tags.Items.GEMS_LAPIS), count),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "redstone"), "singularity.extendedcrafting.redstone", new int[] { 11144961, 7471104 }, Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), count),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "glowstone"), "singularity.extendedcrafting.glowstone", new int[] { 16759902, 11825472 }, Ingredient.fromTag(Tags.Items.DUSTS_GLOWSTONE), count),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "diamond"), "singularity.extendedcrafting.diamond", new int[] { 4910553, 2147765 }, Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), count),
                new Singularity(new ResourceLocation(ExtendedCrafting.MOD_ID, "emerald"), "singularity.extendedcrafting.emerald", new int[] { 4322180, 43564 }, Ingredient.fromTag(Tags.Items.GEMS_EMERALD), count)
        );
    }
}
