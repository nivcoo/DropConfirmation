package fr.nivcoo.dropconfirmation.commands;


import fr.nivcoo.dropconfirmation.DropConfirmation;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfoCMD implements CCommand {

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("info");
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

    public void execute(DropConfirmation plugin, CommandSender sender, String[] args) {
        DropConfirmation dp = DropConfirmation.get();
        sender.sendMessage(
                ChatColor.YELLOW + "-----------[" + ChatColor.GOLD + dp.getDescription().getName() + ChatColor.YELLOW + "]-----------");
        sender.sendMessage(ChatColor.AQUA + "Created by nivcoo");
        sender.sendMessage(ChatColor.GREEN + "v" + dp.getDescription().getVersion());
        sender.sendMessage(ChatColor.AQUA + "https://www.nivcoo.fr");
        sender.sendMessage(ChatColor.AQUA + "https://www.github.com/nivcoo");

    }

    @Override
    public List<String> tabComplete(DropConfirmation plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

}
