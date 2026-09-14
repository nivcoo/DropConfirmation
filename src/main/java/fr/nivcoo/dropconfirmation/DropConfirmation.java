package fr.nivcoo.dropconfirmation;

import fr.nivcoo.dropconfirmation.command.InfoCommand;
import fr.nivcoo.dropconfirmation.command.ReloadCommand;
import fr.nivcoo.dropconfirmation.config.MainConfig;
import fr.nivcoo.dropconfirmation.listener.PlayerDropListener;
import fr.nivcoo.dropconfirmation.manager.ConfirmationManager;
import fr.nivcoo.utilsz.core.commands.CommandManager;
import fr.nivcoo.utilsz.core.commands.CommandsConfigProvider;
import fr.nivcoo.utilsz.core.config.ConfigManager;
import fr.nivcoo.utilsz.platform.bukkit.commands.BukkitCommandRegistrar;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class DropConfirmation extends JavaPlugin {
    private static DropConfirmation instance;
    private ConfigManager configManager;
    private MainConfig config;
    private CommandManager commandManager;
    private ConfirmationManager confirmationManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(getDataFolder());
        config = configManager.load("config.yml", MainConfig.class);
        confirmationManager = new ConfirmationManager();
        getServer().getPluginManager().registerEvents(new PlayerDropListener(this), this);
        commandManager = new CommandManager(new BukkitCommandRegistrar(this), new CommandsConfigProvider() {
            @Override
            public Component noPermission() {
                return ConfigManager.parseDynamic(config.messages.commands.noPermission);
            }

            @Override
            public Component incorrectUsage() {
                return ConfigManager.parseDynamic(config.messages.commands.incorrectUsage);
            }

            @Override
            public List<Component> help() {
                return config.messages.commands.help.stream().map(ConfigManager::parseDynamic).toList();
            }
        }, "dropconfirmation", "dropconfirmation.commands");
        commandManager.addCommand(new InfoCommand(this));
        commandManager.addCommand(new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        if (confirmationManager != null) confirmationManager.clear();
        instance = null;
    }

    public void reload() {
        MainConfig reloaded = configManager.load("config.yml", MainConfig.class);
        config = reloaded;
        confirmationManager.clear();
    }

    public static DropConfirmation get() {
        return instance;
    }

    public MainConfig getConfiguration() {
        return config;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ConfirmationManager getConfirmationManager() {
        return confirmationManager;
    }
}
