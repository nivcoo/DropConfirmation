package fr.nivcoo.dropconfirmation.condition;

import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public enum ConfirmationCondition {
    WHITELISTED {
        @Override
        public boolean matches(ConfirmationContext context) {
            return context.whitelistedItems().isEmpty()
                    || context.whitelistedItems().contains(context.item().getType().name());
        }
    },
    RENAMED {
        @Override
        public boolean matches(ConfirmationContext context) {
            ItemMeta meta = context.meta();
            return meta != null && (meta.hasCustomName() || meta.hasItemName());
        }
    },
    ENCHANTED {
        @Override
        public boolean matches(ConfirmationContext context) {
            ItemMeta meta = context.meta();
            return meta != null && (meta.hasEnchants()
                    || meta instanceof EnchantmentStorageMeta book && book.hasStoredEnchants());
        }
    },
    CUSTOM_LORE {
        @Override
        public boolean matches(ConfirmationContext context) {
            return context.meta() != null && context.meta().hasLore();
        }
    },
    ALWAYS {
        @Override
        public boolean matches(ConfirmationContext context) {
            return true;
        }
    };

    public abstract boolean matches(ConfirmationContext context);
}
