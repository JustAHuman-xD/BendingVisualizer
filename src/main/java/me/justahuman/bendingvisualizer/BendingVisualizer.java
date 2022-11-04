package me.justahuman.bendingvisualizer;

import me.justahuman.bendingvisualizer.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BendingVisualizer extends JavaPlugin {
    private static BendingVisualizer plugin;

    @Override
    public void onEnable() {
        plugin = this;
        ConfigManager.initialize();

    }

    @Override
    public void onDisable() {
       ConfigManager.save();
    }

    public static BendingVisualizer getPlugin() {
        return plugin;
    }
}
