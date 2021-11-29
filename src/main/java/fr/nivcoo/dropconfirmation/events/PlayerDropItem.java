package fr.nivcoo.dropconfirmation.events;

import fr.nivcoo.dropconfirmation.DropConfirmation;
import fr.nivcoo.utilsz.config.Config;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerDropItem implements Listener {

    private HashMap<String, HashMap<ItemStack, Long>> wait = new HashMap<>();
    private DropConfirmation dp = DropConfirmation.get();
    private Config config = dp.getConfiguration();
    int secondsBeforeReset = config.getInt("seconds_before_reset");

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItemEvent(PlayerDropItemEvent e) {

        Player p = e.getPlayer();
        ArrayList<InventoryType> it = new ArrayList<>();
        it.add(InventoryType.CRAFTING);
        it.add(InventoryType.PLAYER);
        it.add(InventoryType.CREATIVE);
        InventoryView iv = p.getOpenInventory();
        if (p.hasPermission("dropconfirmation.bypass") || !it.contains(iv.getType()))
            return;
        Item i = e.getItemDrop();
        ItemStack item = i.getItemStack();
        boolean hasPlace = p.getInventory().firstEmpty() >= 0 || hasPlace(p.getInventory(), item);
        boolean confirmOnlyName = config.getBoolean("confirm_only_renamed");
        List<String> whitelistedItems = config.getStringList("whitelisted_items");
        List<String> blacklistedWorlds = config.getStringList("blacklisted_world");
        if (!hasPlace || blacklistedWorlds.contains(p.getWorld().getName())
                || (!whitelistedItems.isEmpty() && !whitelistedItems.contains(item.getType().toString()))
                || (confirmOnlyName && !item.getItemMeta().hasDisplayName()))
            return;
        boolean perItemConfirmation = config.getBoolean("per_item_confirmation");
        boolean resetConfirmAfterDrop = config.getBoolean("reset_confirm_after_drop");
        HashMap<ItemStack, Long> items = wait.get(p.getUniqueId().toString());
        Long currentMillis = System.currentTimeMillis();
        if (!perItemConfirmation)
            item = null;
        if (items != null) {
            Long millis = items.get(item);
            if (millis == null) {
                items.put(item, currentMillis);
                sendCancelMessage(p);
                e.setCancelled(true);

            } else {
                if ((currentMillis - millis) / 1000 > secondsBeforeReset) {
                    sendCancelMessage(p);

                    e.setCancelled(true);
                } else if (resetConfirmAfterDrop) {
                    items.remove(item);
                }
                items.put(item, currentMillis);
            }

        } else {
            items = new HashMap<>();
            items.put(item, currentMillis);
            sendCancelMessage(p);
            e.setCancelled(true);
        }

        wait.put(p.getUniqueId().toString(), items);

    }

    private void sendCancelMessage(Player p) {
        p.sendMessage(config.getString("messages.prefix")
                + config.getString("messages.cancel_message").replace("{0}", String.valueOf(secondsBeforeReset)));
    }

    private boolean hasPlace(Inventory inventory, ItemStack is) {
        for (int i = 0; i < inventory.getSize() - 1; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null)
                continue;
            if (item.equals(is) && item.getAmount() < item.getMaxStackSize()) {
                return true;
            }

        }
        return false;
    }

}
