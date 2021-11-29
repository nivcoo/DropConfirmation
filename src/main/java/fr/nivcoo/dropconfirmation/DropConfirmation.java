package fr.nivcoo.dropconfirmation;

import fr.nivcoo.dropconfirmation.commands.InfoCMD;
import fr.nivcoo.dropconfirmation.commands.ReloadCMD;
import fr.nivcoo.dropconfirmation.events.PlayerDropItem;
import fr.nivcoo.utilsz.commands.CommandManager;
import fr.nivcoo.utilsz.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DropConfirmation extends JavaPlugin {
    private static DropConfirmation INSTANCE;
    private Config config;

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
        config = new Config(new File("plugins" + File.separator + "DropConfirmation" + File.separator + "config.yml"));
        Bukkit.getPluginManager().registerEvents(new PlayerDropItem(), this);
        Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
        Bukkit.getConsoleSender().sendMessage("§7DropConfirmation §av" + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§aPlugin Enabled !");
        Bukkit.getConsoleSender().sendMessage("§c==============§b===============");

        commandManager = new CommandManager(this, config, "dropconfirmation", "dropconfirmation.commands");
        commandManager.addCommand(new InfoCMD());
        commandManager.addCommand(new ReloadCMD());
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

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
