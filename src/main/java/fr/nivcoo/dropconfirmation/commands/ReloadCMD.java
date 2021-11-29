package fr.nivcoo.dropconfirmation.commands;

import fr.nivcoo.dropconfirmation.DropConfirmation;
import fr.nivcoo.utilsz.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReloadCMD implements CCommand {

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("reload");
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

    public void execute(DropConfirmation plugin, CommandSender sender, String[] args) {

        DropConfirmation dp = DropConfirmation.get();
        Config config = dp.getConfiguration();
        String prefix = config.getString("messages.prefix");
        sender.sendMessage(prefix + ChatColor.GREEN + "Reloading ...");
        dp.reload();
        sender.sendMessage(prefix + ChatColor.GREEN + "Reloaded");

    }

    @Override
    public List<String> tabComplete(DropConfirmation plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

}
