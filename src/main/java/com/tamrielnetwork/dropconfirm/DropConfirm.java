package com.tamrielnetwork.dropconfirm;

import com.tamrielnetwork.dropconfirm.events.PlayerDropItem;
import com.tamrielnetwork.dropconfirm.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public class DropConfirm extends JavaPlugin {
    private static DropConfirm INSTANCE;
    private Config config;

    public static DropConfirm get() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
        config = new Config(new File("plugins" + File.separator + "DropConfirm" + File.separator + "config.yml"));
        Objects.requireNonNull(getCommand("drop")).setExecutor(new DropConfirmCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerDropItem(), this);
        Bukkit.getLogger().info("§7DropConfirm §av" + this.getDescription().getVersion() + "enabled");
        Bukkit.getLogger().info("Copyright (C) 2021 Leopold Meinel");
        Bukkit.getLogger().info("This program comes with ABSOLUTELY NO WARRANTY!");
        Bukkit.getLogger().info("This is free software, and you are welcome to redistribute it under certain conditions.");
        Bukkit.getLogger().info("Visit https://github.com/TamrielNetwork/DropConfirm/blob/main/LICENSE for more details.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("§7DropConfirm §av" + this.getDescription().getVersion() + "disabled");
    }

    public void reload() {
        config.loadConfig();
    }

    public Config getConfiguration() {
        return config;
    }
}
