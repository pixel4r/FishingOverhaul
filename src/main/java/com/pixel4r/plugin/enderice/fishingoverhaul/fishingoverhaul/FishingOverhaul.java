package com.pixel4r.plugin.enderice.fishingoverhaul.fishingoverhaul;

import com.pixel4r.plugin.enderice.fishingoverhaul.fishingoverhaul.listeners.FishingEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FishingOverhaul extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager plugin = getServer().getPluginManager();
        plugin.registerEvents(new FishingEvents(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
