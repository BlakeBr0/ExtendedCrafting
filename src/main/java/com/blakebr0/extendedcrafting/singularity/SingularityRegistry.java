package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.google.common.base.Stopwatch;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SingularityRegistry {
    private static final Logger LOGGER = LogManager.getLogger(ExtendedCrafting.NAME);
    private static final SingularityRegistry INSTANCE = new SingularityRegistry();

    private final List<Singularity> singularities = new ArrayList<>();

    public void loadSingularities() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<ModInfo> mods = ModList.get().getMods();

        mods.forEach(mod -> {
            String id = mod.getModId();
            String folder = String.format("data/%s/singularities", id);
            File dir = mod.getOwningFile().getFile().getFilePath().resolve(folder).toFile();

            if (dir.exists() && dir.isDirectory()) {
                this.loadFiles(dir, id);
            }
        });

        File dir = FMLPaths.CONFIGDIR.get().resolve("extendedcrafting/singularities/").toFile();
        if (!dir.mkdirs() && dir.isDirectory()) {
            this.loadFiles(dir, ExtendedCrafting.MOD_ID);
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

    private void loadFiles(File dir, String domain) {
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
                singularity = SingularityUtils.loadFromJson(new ResourceLocation(domain, name), json);

                reader.close();
            } catch (Exception e) {
                LOGGER.error("An error occurred while loading singularities for domain {}", domain, e);
            } finally {
                IOUtils.closeQuietly(reader);
            }

            if (singularity != null) {
                this.singularities.add(singularity);
            }
        }
    }

    public static SingularityRegistry getInstance() {
        return INSTANCE;
    }
}
