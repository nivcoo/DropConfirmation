package fr.nivcoo.dropconfirmation;

import fr.nivcoo.dropconfirmation.utils.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DropConfirmationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!"dp".equalsIgnoreCase(command.getLabel()))
            return false;
        DropConfirmation dp = DropConfirmation.get();
        Config config = dp.getConfiguration();
        String prefix = config.getString("messages.prefix");
        String unknownCmd = config.getString("messages.no_permission");
        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            sender.sendMessage(
                    ChatColor.YELLOW + "-----------[" + ChatColor.GOLD + dp.getDescription().getName() + ChatColor.YELLOW + "]-----------");
            sender.sendMessage(
                    ChatColor.AQUA + "- /dp info" + ChatColor.YELLOW + " : See informations about plugin");
            if (sender.hasPermission("dropconfirmation.reload"))
                sender.sendMessage(ChatColor.AQUA + "- /dp reload" + ChatColor.YELLOW + " : Reload the plugin");
        } else if ("info".equalsIgnoreCase(args[0])) {
            sender.sendMessage(
                    ChatColor.YELLOW + "-----------[" + ChatColor.GOLD + dp.getDescription().getName() + ChatColor.YELLOW + "]-----------");
            sender.sendMessage(ChatColor.AQUA + "Created by nivcoo");
            sender.sendMessage(ChatColor.GREEN + "v" + dp.getDescription().getVersion());
            sender.sendMessage(ChatColor.AQUA + "https://www.nivcoo.fr");
            sender.sendMessage(ChatColor.AQUA + "https://www.github.com/nivcoo");
        } else if ("reload".equalsIgnoreCase(args[0])) {

            if (!sender.hasPermission("dropconfirmation.reload")) {
                sender.sendMessage(unknownCmd);
                return true;
            }
            sender.sendMessage(prefix + ChatColor.GREEN + "Reloading ...");
            dp.reload();
            sender.sendMessage(prefix + ChatColor.GREEN + "Reloaded");
        }
        return true;
    }

}
