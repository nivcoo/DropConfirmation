package fr.nivcoo.dropconfirmation.manager;

import fr.nivcoo.dropconfirmation.config.MainConfig;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

public final class ConfirmationManager {
    private final Map<UUID, Map<ItemStack, Long>> pending = new HashMap<>();
    private final LongSupplier clock;

    public ConfirmationManager() {
        this(System::nanoTime);
    }

    ConfirmationManager(LongSupplier clock) {
        this.clock = clock;
    }

    public boolean shouldCancel(UUID playerId, ItemStack item, MainConfig config) {
        long now = clock.getAsLong();
        long timeout = TimeUnit.SECONDS.toNanos(config.secondsBeforeReset);
        Map<ItemStack, Long> items = pending.computeIfAbsent(playerId, ignored -> new HashMap<>());
        items.values().removeIf(started -> now - started >= timeout);
        ItemStack key = config.perItemConfirmation ? item.asOne() : null;
        Long previous = items.get(key);
        if (previous == null) {
            items.put(key, now);
            return true;
        }
        if (config.resetConfirmAfterDrop) {
            items.remove(key);
            if (items.isEmpty()) pending.remove(playerId);
        } else {
            items.put(key, now);
        }
        return false;
    }

    public void clear(UUID playerId) {
        pending.remove(playerId);
    }

    public void clear() {
        pending.clear();
    }
}
