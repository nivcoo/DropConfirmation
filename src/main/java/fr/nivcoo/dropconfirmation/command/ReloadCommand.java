package fr.nivcoo.dropconfirmation.command;

import fr.nivcoo.dropconfirmation.DropConfirmation;
import fr.nivcoo.utilsz.core.config.ConfigManager;
import fr.nivcoo.utilsz.platform.bukkit.commands.BukkitCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.logging.Level;

public final class ReloadCommand implements BukkitCommand {

    private final DropConfirmation plugin;

    public ReloadCommand(DropConfirmation plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getAliases() {
        return List.of("reload");
    }

    @Override
    public String getPermission() {
        return "dropconfirmation.command.reload";
    }

    @Override
    public String getUsage() {
        return "reload";
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
        try {
            plugin.reload();
        } catch (RuntimeException exception) {
            sender.sendMessage(ConfigManager.parseDynamic(plugin.getConfiguration().messages.prefix
                    + plugin.getConfiguration().messages.commands.reloadFailed));
            plugin.getLogger().log(Level.SEVERE, "Unable to reload DropConfirmation configuration", exception);
            return;
        }
        sender.sendMessage(ConfigManager.parseDynamic(plugin.getConfiguration().messages.prefix
                + plugin.getConfiguration().messages.commands.reloadSuccess));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        return List.of();
    }
}
