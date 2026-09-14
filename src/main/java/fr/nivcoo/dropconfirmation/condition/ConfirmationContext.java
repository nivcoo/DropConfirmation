package fr.nivcoo.dropconfirmation.condition;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public record ConfirmationContext(ItemStack item, ItemMeta meta, List<String> whitelistedItems) {
}
