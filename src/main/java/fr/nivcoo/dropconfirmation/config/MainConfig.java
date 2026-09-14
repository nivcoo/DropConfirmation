package fr.nivcoo.dropconfirmation.config;

import fr.nivcoo.dropconfirmation.condition.ConditionExpression;
import fr.nivcoo.dropconfirmation.condition.ConfirmationCondition;
import fr.nivcoo.dropconfirmation.condition.ConfirmationContext;
import fr.nivcoo.utilsz.core.config.annotations.Comment;
import fr.nivcoo.utilsz.core.config.annotations.Section;
import fr.nivcoo.utilsz.core.config.annotations.WithConverter;
import fr.nivcoo.utilsz.core.config.validation.Validatable;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public final class MainConfig implements Validatable {
    @Comment({
            "Groupes imbriquables : all = ET, any = OU. Chaque nœud contient type, all ou any.",
            "Types : WHITELISTED, RENAMED, ENCHANTED (y compris les livres), CUSTOM_LORE, ALWAYS.",
            "WHITELISTED utilise whitelisted_items ; une liste de matériaux vide accepte tous les matériaux.",
            "Par défaut : matériau listé ET (renommé OU enchanté OU lore personnalisé).",
            "Exemple : any: [{type: WHITELISTED}, {all: [{type: RENAMED}, {type: ENCHANTED}]}].",
            "Pour confirmer tous les objets : confirmation_conditions: {type: ALWAYS}."
    })
    @WithConverter(ConditionExpressionConverter.class)
    public ConditionExpression confirmationConditions = new ConditionExpression.All(List.of(
            new ConditionExpression.Leaf(ConfirmationCondition.WHITELISTED),
            new ConditionExpression.Any(List.of(
                    new ConditionExpression.Leaf(ConfirmationCondition.RENAMED),
                    new ConditionExpression.Leaf(ConfirmationCondition.ENCHANTED),
                    new ConditionExpression.Leaf(ConfirmationCondition.CUSTOM_LORE)))));

    @Comment("Utiliser [] pour accepter tous les matériaux ; utiliser les noms en majuscules.")
    public List<String> whitelistedItems = List.of(
            "DRAGON_EGG", "MOB_SPAWNER", "SPAWNER", "ELYTRA", "BLAZE_ROD", "BOW",
            "DIAMOND_SWORD", "DIAMOND_HELMET", "DIAMOND_CHESTPLATE", "DIAMOND_LEGGINGS",
            "DIAMOND_BOOTS", "DIAMOND_PICKAXE", "DIAMOND_AXE", "DIAMOND_HOE", "DIAMOND_SHOVEL",
            "NETHERITE_SWORD", "NETHERITE_HELMET", "NETHERITE_CHESTPLATE", "NETHERITE_LEGGINGS",
            "NETHERITE_BOOTS", "NETHERITE_PICKAXE", "NETHERITE_AXE", "NETHERITE_HOE", "NETHERITE_SHOVEL",
            "CROSSBOW", "MACE", "GOLD_HOE", "GOLDEN_HOE", "FISHING_ROD", "HOPPER", "CHEST",
            "CAULDRON", "LEATHER_HELMET", "LEATHER_CHESTPLATE", "LEATHER_LEGGINGS", "LEATHER_BOOTS",
            "STICK", "FURNACE", "CLOCK", "WATCH", "SKULL_ITEM", "PLAYER_HEAD", "WRITABLE_BOOK", "BOOK",
            "ENCHANTED_BOOK", "TRIPWIRE_HOOK");

    public int secondsBeforeReset = 5;
    public boolean perItemConfirmation = true;
    public boolean resetConfirmAfterDrop = false;
    public List<String> blacklistedWorld = List.of("pvp");
    public Messages messages = new Messages();

    public boolean requiresConfirmation(ItemStack item) {
        ConfirmationContext context = new ConfirmationContext(item, item.getItemMeta(), whitelistedItems);
        return confirmationConditions.matches(condition -> condition.matches(context));
    }

    @Override
    public void validate() {
        Objects.requireNonNull(confirmationConditions, "confirmation_conditions");
        Objects.requireNonNull(whitelistedItems, "whitelisted_items");
        Objects.requireNonNull(blacklistedWorld, "blacklisted_world");
        Objects.requireNonNull(messages, "messages");
        if (secondsBeforeReset < 1) {
            throw new IllegalArgumentException("seconds_before_reset doit être supérieur ou égal à 1");
        }
    }

    @Section
    public static final class Messages implements Validatable {
        public String prefix = "&7[&c&lConfirmation&7] ";
        @Comment("Utiliser {0} pour afficher le délai de confirmation en secondes.")
        public String cancelMessage = "Vous devez &aconfirmer &7le drop avant &a{0} secondes&7 ! Désactivable dans le &b/tools";
        public Commands commands = new Commands();

        @Override
        public void validate() {
            Objects.requireNonNull(prefix, "messages.prefix");
            Objects.requireNonNull(cancelMessage, "messages.cancel_message");
            Objects.requireNonNull(commands, "messages.commands");
        }
    }

    @Section
    public static final class Commands implements Validatable {
        public String incorrectUsage = "&fCorrect Usage : /{0}";
        public String noPermission = "&fCommande inconnue.";
        public String reloadSuccess = "&aConfiguration rechargée.";
        public String reloadFailed = "&cLe rechargement a échoué. Consultez la console.";
        public List<String> help = List.of(
                "&7&m------------------&8[&6Help Panel&8]&7&m------------------",
                "{!dropconfirmation.command.reload}&6/dropconfirmation reload &eReload the config",
                "&6/dropconfirmation info &eDisplay the plugin information",
                "&7&m----------------------------------------------");

        @Override
        public void validate() {
            Objects.requireNonNull(incorrectUsage, "messages.commands.incorrect_usage");
            Objects.requireNonNull(noPermission, "messages.commands.no_permission");
            Objects.requireNonNull(reloadSuccess, "messages.commands.reload_success");
            Objects.requireNonNull(reloadFailed, "messages.commands.reload_failed");
            Objects.requireNonNull(help, "messages.commands.help");
        }
    }
}
