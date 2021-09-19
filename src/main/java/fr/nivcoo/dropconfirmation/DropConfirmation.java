/**
 *
 */
package fr.nivcoo.dropconfirmation;

import fr.nivcoo.dropconfirmation.events.PlayerDropItem;
import fr.nivcoo.dropconfirmation.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DropConfirmation extends JavaPlugin {
    private static DropConfirmation INSTANCE;
    private Config config;

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
        config = new Config(new File("plugins" + File.separator + "DropConfirmation" + File.separator + "config.yml"));
        getCommand("dp").setExecutor(new DropConfirmationCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerDropItem(), this);
        Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
        Bukkit.getConsoleSender().sendMessage("§7DropConfirmation §av" + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§aPlugin Enabled !");
        Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
        Bukkit.getConsoleSender().sendMessage("§7DropConfirmation §cv" + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin Disabled !");
        Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
    }


    public void reload() {
        config.loadConfig();
    }


    public static DropConfirmation get() {
        return INSTANCE;
    }

    public Config getConfiguration() {
        return config;
    }
}
