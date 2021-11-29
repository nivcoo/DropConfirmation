package fr.nivcoo.dropconfirmation.commands;

import fr.nivcoo.dropconfirmation.DropConfirmation;
import fr.nivcoo.utilsz.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface CCommand extends Command {

    default void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        execute((DropConfirmation) plugin, sender, args);
    }

    default List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        return tabComplete((DropConfirmation) plugin, sender, args);
    }

    void execute(DropConfirmation plugin, CommandSender sender, String[] args);

    List<String> tabComplete(DropConfirmation plugin, CommandSender sender, String[] args);

}
