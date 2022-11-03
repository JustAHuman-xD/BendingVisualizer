package me.justahuman.bendingvisualizer;

import org.bukkit.plugin.java.JavaPlugin;

public final class BendingVisualizer extends JavaPlugin {

    private static BendingVisualizer plugin;

    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BendingVisualizer getPlugin() {
        return plugin;
    }
}
