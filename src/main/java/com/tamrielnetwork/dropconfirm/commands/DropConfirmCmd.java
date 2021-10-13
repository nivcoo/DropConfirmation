package com.tamrielnetwork.dropconfirm.commands;

import com.tamrielnetwork.dropconfirm.DropConfirm;
import com.tamrielnetwork.dropconfirm.utils.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DropConfirmCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Check args length
        if (args.length == 0) {
            sender.sendMessage("§cNo arguments specified!");
            // Cancel command
            return true;
        }
        // Define commands
        switch (args[0]) {
            case "reload" -> executeReload(sender, args);
            case "info" -> sender.sendMessage("§b/drop reload §7: Reload DropConfirm");
            default -> sender.sendMessage("§cInvalid option!");
        }
        // Cancel command
        return true;
    }

    private void executeReload(CommandSender sender, String[] args) {
        // Define INSTANCE
        DropConfirm drop = DropConfirm.get();
        // Define Config
        Config config = drop.getConfiguration();
        // Define prefix
        String prefix = config.getString("messages.prefix");

        //Check permissions
        if (!sender.hasPermission("dropconfirm.reload")) {
            sender.sendMessage(config.getString("messages.no_permission"));
            // Cancel command
            return;
        }

        //Check args length
        if (args.length != 1) {
            sender.sendMessage("§cArgument doesn't exist!");
            // Cancel command
            return;
        }
        // Otherwise, execute reload
        drop.reload();
        sender.sendMessage(prefix + ChatColor.GREEN + "DropConfirm Reloaded!");
    }

}
