package fr.nivcoo.dropconfirmation.events;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import fr.nivcoo.dropconfirmation.DropConfirmation;
import fr.nivcoo.dropconfirmation.utils.Config;

public class PlayerDropItem implements Listener {

	private HashMap<String, HashMap<ItemStack, Long>> wait = new HashMap<>();
	private DropConfirmation dp = DropConfirmation.get();
	private Config config = dp.getConfiguration();
	int secondsBeforeReset = config.getInt("seconds_before_reset");

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {

		Player p = e.getPlayer();
		if (p.hasPermission("dropconfirmation.bypass"))
			return;

		Item i = e.getItemDrop();
		ItemStack item = i.getItemStack();
		boolean confirmOnlyName = config.getBoolean("confirm_only_renamed");
		List<String> whitelistedItems = config.getStringList("whitelisted_items");
		List<String> blacklistedWorlds = config.getStringList("blacklisted_world");
		boolean perItemConfirmation = config.getBoolean("per_item_confirmation");

		if (blacklistedWorlds.contains(p.getWorld().getName())
				|| (!whitelistedItems.isEmpty() && !whitelistedItems.contains(item.getType().toString()))
				|| (confirmOnlyName && !item.getItemMeta().hasDisplayName()))
			return;

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
					items.put(item, currentMillis);
					sendCancelMessage(p);
					e.setCancelled(true);
				} else {
					items.remove(item);
				}
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

}
