package com.Minor2CCh.retrial_key.config;

import com.Minor2CCh.retrial_key.Retrial_key;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.architectury.platform.Platform;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
// WIP(Intコンフィグがうまくいかないので)
public class ModConfigLoader {
    private static final File DIR = Platform.getConfigFolder().toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILENAME = Retrial_key.MOD_ID+".json";
    private static File FILE;
    //public static ModConfig CONFIG = get();
    private static Map<String, Object> CONFIG_MAP = new LinkedHashMap<>();
    public static void initialize(){
        setDefault();
        load();
    }
    public static void load() {
        FILE = getFile();
        if (!DIR.exists()) {
            DIR.mkdirs();
        }
        read();
        save();
        ModConfigLoader.getInt("heavyRetrialKeyDurability");
    }
    public static Object getKey(String key) {
        return CONFIG_MAP.get(key);
    }
    public static void setBoolean(String key, boolean value) {
        CONFIG_MAP.put(key, value);
    }
    public static Boolean getBoolean(String key) {
        Object value = getKey(key);
        if (value instanceof Integer i) {
            return i != 0;
        }
        if (value instanceof Boolean)
            return (Boolean) value;
        if (value instanceof String)
            return Boolean.getBoolean((String) value);

        return null;
    }
    public static void setInt(String key, int value) {
        CONFIG_MAP.put(key, value);
    }
    public static Integer getInt(String key) {
        Object value = getKey(key);
        if (value instanceof Integer)
            return (Integer) value;
        if (value instanceof Float)
            return ((Float) value).intValue();
        if (value instanceof String)
            return Integer.getInteger((String) value);
        return 1024;
        //return null;
    }
    public static void setDefault() {
        setBoolean("aa", false);
        setInt("heavyRetrialKeyDurability", 256);
        setInt("heavyRetrialKeyDurability2", 257);
    }
    public static boolean save() {
        String json = GSON.toJson(CONFIG_MAP);
        return fileWriteContents(FILE, json);
    }

    public static boolean read() {
        if (FILE.exists()) {
            String json = fileReadContents(FILE);
            Gson gson = new Gson();
            Type jsonMap = new TypeToken<LinkedHashMap<String, Object>>() {
            }.getType();
            CONFIG_MAP = gson.fromJson(json, jsonMap);
            return true;
        }

        return false;
    }
    public static boolean fileWriteContents(File file, String contents) {
        try {
            PrintWriter writer = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)));
            writer.println(contents);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String fileReadContents(File file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            StringBuilder contents = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                contents.append(line).append("\n");
            }
            reader.close();
            return contents.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static File getFile() {
        if (FILE == null) {
            FILE = new File(Platform.getConfigFolder().toFile(), FILENAME);
        }
        return FILE;
    }
}
