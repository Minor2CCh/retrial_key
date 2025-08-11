package com.Minor2CCh.retrial_key.config;

import com.Minor2CCh.retrial_key.Retrial_key;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfigLoader {
    private static final File DIR = Platform.getConfigFolder().toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILENAME = Retrial_key.MOD_ID+".json";
    private static final Path CONFIG_PATH = Path.of(new File(DIR,FILENAME).getPath());
    private static ModConfig modConfig;
    public static void load(){
        System.out.println(CONFIG_PATH);
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                modConfig = GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                System.err.println("Failed to load config: " + e.getMessage());
                modConfig = new ModConfig();
            }
            modConfig.fillDefaults(); // ここで新フィールド補完
        } else {
            modConfig = new ModConfig();
        }
        save(); // 初回生成
    }
    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(modConfig, writer);
                //System.out.println("Create Success:"+CONFIG_PATH);
            }
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }
    public static ModConfig getConfig() {
        return modConfig;
    }
}
