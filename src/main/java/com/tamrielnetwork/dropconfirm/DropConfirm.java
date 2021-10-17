package com.tamrielnetwork.dropconfirm;

import com.tamrielnetwork.dropconfirm.commands.DropConfirmCmd;
import com.tamrielnetwork.dropconfirm.listeners.PlayerDropItem;
import com.tamrielnetwork.dropconfirm.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public class DropConfirm extends JavaPlugin {
    // Define INSTANCE
    private static DropConfirm INSTANCE;
    // Define config
    private Config config;

    public static DropConfirm get() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Redefine INSTANCE
        INSTANCE = this;
        // Save default config.yml
        saveDefaultConfig();
        // Redefine config
        config = new Config(new File("plugins" + File.separator + "DropConfirm" + File.separator + "config.yml"));
        // Register Commands
        Objects.requireNonNull(getCommand("drop")).setExecutor(new DropConfirmCmd());
        Objects.requireNonNull(getCommand("drop")).setTabCompleter(new DropConfirmCmd());
        // Register Listeners
        Bukkit.getPluginManager().registerEvents(new PlayerDropItem(), this);

        // Log to console
        Bukkit.getLogger().info("§7DropConfirm " + this.getDescription().getVersion() + " enabled");
        Bukkit.getLogger().info("Copyright (C) 2021 Leopold Meinel");
        Bukkit.getLogger().info("This program comes with ABSOLUTELY NO WARRANTY!");
        Bukkit.getLogger().info("This is free software, and you are welcome to redistribute it under certain conditions.");
        Bukkit.getLogger().info("Visit https://github.com/TamrielNetwork/DropConfirm/blob/main/LICENSE for more details.");
    }

    @Override
    public void onDisable() {
        // Log to console
        Bukkit.getLogger().info("§7DropConfirm §av" + this.getDescription().getVersion() + " disabled");
    }

    // Define reload() for config
    public void reload() {
        config.loadConfig();
    }

    // Define getConfiguration() for config
    public Config getConfiguration() {
        return config;
    }
}
