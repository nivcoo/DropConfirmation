package fr.nivcoo.dropconfirmation.condition;

import org.bukkit.inventory.meta.ItemMeta;

public record ConfirmationContext(String material, ItemMeta meta) {
}
