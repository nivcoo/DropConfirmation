package fr.nivcoo.dropconfirmation.command;

import fr.nivcoo.dropconfirmation.DropConfirmation;
import fr.nivcoo.utilsz.core.config.ConfigManager;
import fr.nivcoo.utilsz.platform.bukkit.commands.BukkitCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class InfoCommand implements BukkitCommand {

    private final DropConfirmation plugin;

    public InfoCommand(DropConfirmation plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getAliases() {
        return List.of("info");
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public String getUsage() {
        return "info";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        sender.sendMessage(ConfigManager.parseDynamic("&e-----------[&6" + plugin.getPluginMeta().getName() + "&e]-----------"));
        sender.sendMessage(ConfigManager.parseDynamic("&bCreated by nivcoo"));
        sender.sendMessage(ConfigManager.parseDynamic("&av" + plugin.getPluginMeta().getVersion()));
        sender.sendMessage(ConfigManager.parseDynamic("&bhttps://www.nivcoo.fr"));
        sender.sendMessage(ConfigManager.parseDynamic("&bhttps://www.github.com/nivcoo"));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        return List.of();
    }
}
