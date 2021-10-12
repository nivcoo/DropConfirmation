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
        Bukkit.getLogger().info("§c==============§b===============");
        Bukkit.getLogger().info("§7DropConfirm §av" + this.getDescription().getVersion());
        Bukkit.getLogger().info("§aPlugin Enabled!");
        Bukkit.getLogger().info("§c==============§b===============");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("§c==============§b===============");
        Bukkit.getLogger().info("§7DropConfirm §cv" + this.getDescription().getVersion());
        Bukkit.getLogger().info("§cPlugin Disabled!");
        Bukkit.getLogger().info("§c==============§b===============");
    }

    public void reload() {
        config.loadConfig();
    }

    public Config getConfiguration() {
        return config;
    }
}
