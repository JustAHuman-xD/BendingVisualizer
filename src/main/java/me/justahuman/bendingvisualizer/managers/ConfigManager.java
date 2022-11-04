package me.justahuman.bendingvisualizer.managers;

import lombok.Getter;
import me.justahuman.bendingvisualizer.BendingVisualizer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    @Getter
    private static FileConfiguration config;

    private static final File configFile = new File("plugins/BendingVisualizer/config.yml");

    private static void saveDefaultResources() {
        // Save config.yml
        BendingVisualizer.getPlugin().saveDefaultConfig();
    }

    private static void loadResources() {
        //Load the Config File
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void initialize() {
        saveDefaultResources();
        loadResources();
    }

    public static void save() {
        try {
            config.save(configFile);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
