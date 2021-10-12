package com.tamrielnetwork.dropconfirm;

import com.tamrielnetwork.dropconfirm.utils.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DropConfirmCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (!"drop".equalsIgnoreCase(command.getLabel()))
            return false;
        DropConfirm drop = DropConfirm.get();
        Config config = drop.getConfiguration();
        String prefix = config.getString("messages.prefix");
        String unknownCmd = config.getString("messages.no_permission");
        if (sender.hasPermission("dropconfirm.reload")) {
            sender.sendMessage(ChatColor.AQUA + "- /drop reload" + ChatColor.YELLOW + " : Reload plugin");
        } else if ("reload".equalsIgnoreCase(args[0])) {

            if (!sender.hasPermission("dropconfirm.reload")) {
                sender.sendMessage(unknownCmd);
                return true;
            }
            drop.reload();
            sender.sendMessage(prefix + ChatColor.GREEN + "DropConfirm Reloaded!");
        }
        return true;
    }

}
