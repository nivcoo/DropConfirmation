package fr.nivcoo.dropconfirmation.listener;

import fr.nivcoo.dropconfirmation.DropConfirmation;
import fr.nivcoo.dropconfirmation.config.MainConfig;
import fr.nivcoo.utilsz.core.config.ConfigManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class PlayerDropListener implements Listener {
    private final DropConfirmation plugin;

    public PlayerDropListener(DropConfirmation plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.isDead() || player.getHealth() <= 0.0) return;
        if (event.getItemDrop().getThrower() == null) return;
        if (player.getGameMode() == GameMode.SPECTATOR || player.hasPermission("dropconfirmation.bypass")) return;
        InventoryType inventoryType = player.getOpenInventory().getType();
        if (inventoryType != InventoryType.CRAFTING && inventoryType != InventoryType.PLAYER
                && inventoryType != InventoryType.CREATIVE) return;

        MainConfig config = plugin.getConfiguration();
        ItemStack item = event.getItemDrop().getItemStack();
        if (config.blacklistedWorld.contains(player.getWorld().getName())
                || !config.requiresConfirmation(item)
                || !hasPlace(player.getInventory(), item)) return;

        if (plugin.getConfirmationManager().shouldCancel(player.getUniqueId(), item, config)) {
            event.setCancelled(true);
            player.sendMessage(ConfigManager.parseDynamic(config.messages.prefix
                    + config.messages.cancelMessage.replace("{0}", String.valueOf(config.secondsBeforeReset))));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getConfirmationManager().clear(event.getPlayer().getUniqueId());
    }

    private boolean hasPlace(PlayerInventory inventory, ItemStack dropped) {
        int remaining = dropped.getAmount();
        for (ItemStack item : inventory.getStorageContents()) {
            if (item == null || item.getType().isAir()) return true;
            if (item.isSimilar(dropped)) {
                remaining -= Math.max(0, Math.min(item.getMaxStackSize(), inventory.getMaxStackSize()) - item.getAmount());
                if (remaining <= 0) return true;
            }
        }
        return false;
    }
}
