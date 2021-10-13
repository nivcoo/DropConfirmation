package com.tamrielnetwork.dropconfirm.listeners;

import com.tamrielnetwork.dropconfirm.DropConfirm;
import com.tamrielnetwork.dropconfirm.utils.Config;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PlayerDropItem implements Listener {
    // Map String(Player UUID) to a Map where ItemStack is mapped to Long (itemStackMap) -> player identifier
    private final HashMap<String, HashMap<ItemStack, Long>> uuidMap = new HashMap<>();
    // Define INSTANCE
    private final DropConfirm drop = DropConfirm.get();
    // Define Config
    private final Config config = drop.getConfiguration();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItemEvent(PlayerDropItemEvent playerDropItemEvent) {
        // Define player that dropped item
        Player player = playerDropItemEvent.getPlayer();
        // Define valid inventoryType
        ArrayList<InventoryType> inventoryType = new ArrayList<>();
        inventoryType.add(InventoryType.CRAFTING);
        inventoryType.add(InventoryType.PLAYER);
        inventoryType.add(InventoryType.CREATIVE);
        // Define inventoryView
        InventoryView inventoryView = player.getOpenInventory();
        // Skip if player has bypass permission or isn't in a valid inventoryType
        if (player.hasPermission("dropconfirm.bypass") || !inventoryType.contains(inventoryView.getType())) {
            return;
        }
        // Execute when player has empty inventory slot or has active item amount > 1 in inventory
        if (player.getInventory().firstEmpty() != -1 || (Objects.requireNonNull(player.getActiveItem()).getAmount() > 1 || player.getItemOnCursor().getAmount() > 1)) {
            // Define droppedItem
            Item droppedItem = playerDropItemEvent.getItemDrop();
            // Define droppedItemStack of droppedItem
            ItemStack droppedItemStack = droppedItem.getItemStack();
            // Get whitelisted_items and blacklisted_world from config
            List<String> whitelistedItems = config.getStringList("whitelisted_items");
            List<String> blacklistedWorlds = config.getStringList("blacklisted_world");
            // Skip if player is in blacklisted world, drops whitelisted item or (if configured) doesn't have renamed item
            if (blacklistedWorlds.contains(player.getWorld().getName()) || (!whitelistedItems.isEmpty() && !whitelistedItems.contains(droppedItemStack.getType().toString())) || (config.getBoolean("confirm_only_renamed") && !droppedItemStack.getItemMeta().hasDisplayName())) {
                return;
            }
            // Map ItemStack(droppedItemStack) to Long(currentMillis) -> item lifetime
            HashMap<ItemStack, Long> itemStackMap = uuidMap.get(player.getUniqueId().toString());
            // Define currentMillis (Current time)
            Long currentMillis = System.currentTimeMillis();
            // Set droppedItemStack to null if per_item_confirmation is false
            if (!config.getBoolean("per_item_confirmation")) {
                droppedItemStack = null;
            }
            // Start timing items if itemStackMap != null -> player is assigned an itemStack and currentMillis
            if (itemStackMap != null) {
                // Get lifetime of droppedItemStack
                Long millis = itemStackMap.get(droppedItemStack);
                // Cancel playerDropItemEvent if lifetime(millis) != null
                if (millis == null) {
                    itemStackMap.put(droppedItemStack, currentMillis);
                    sendCancelMessage(player);
                    playerDropItemEvent.setCancelled(true);
                } else {
                    // Cancel playerDropItemEvent if item isn't older than seconds_before_reset
                    if ((currentMillis - millis) / 1000 > config.getInt("seconds_before_reset")) {
                        sendCancelMessage(player);
                        playerDropItemEvent.setCancelled(true);
                    }
                    // Unmap ItemStack(droppedItemStack) from Long(currentMillis) -> item lifetime if reset_confirm_after_drop is true
                    else if (config.getBoolean("reset_confirm_after_drop")) {
                        itemStackMap.remove(droppedItemStack);
                    }
                    // Remap ItemStack(droppedItemStack) to Long(currentMillis) -> item lifetime
                    itemStackMap.put(droppedItemStack, currentMillis);
                }
            }
            // Clear itemStackMap and cancel playerDropItemEvent otherwise
            else {
                itemStackMap = new HashMap<>();
                itemStackMap.put(droppedItemStack, currentMillis);
                sendCancelMessage(player);
                playerDropItemEvent.setCancelled(true);
            }
            // Remap String(Player UUID) to a Map where ItemStack is mapped to Long (itemStackMap) -> player identifier
            uuidMap.put(player.getUniqueId().toString(), itemStackMap);
        }
    }

    // Define Message on cancelled playerDropItemEvent
    private void sendCancelMessage(Player player) {
        player.sendMessage(config.getString("messages.prefix") + config.getString("messages.cancel_message").replace("{0}", String.valueOf(config.getInt("seconds_before_reset"))));
    }

}
