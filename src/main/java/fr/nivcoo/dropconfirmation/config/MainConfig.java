package fr.nivcoo.dropconfirmation.config;

import fr.nivcoo.dropconfirmation.condition.ConditionExpression;
import fr.nivcoo.dropconfirmation.condition.ConfirmationCondition;
import fr.nivcoo.dropconfirmation.condition.ConfirmationContext;
import fr.nivcoo.utilsz.core.config.annotations.Comment;
import fr.nivcoo.utilsz.core.config.annotations.Section;
import fr.nivcoo.utilsz.core.config.annotations.WithConverter;
import fr.nivcoo.utilsz.core.config.validation.Validatable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class MainConfig implements Validatable {
    @Comment({
            "Chaque groupe combine sa liste materials ET ses conditions. Un seul groupe correspondant suffit.",
            "materials: [] accepte tous les matériaux pour ce groupe ; confirmation_groups: {} ne protège aucun objet.",
            "Les conditions permettent d'imbriquer all (ET) et any (OU). Chaque nœud contient type, all ou any.",
            "Types : RENAMED, ENCHANTED (y compris les livres), CUSTOM_LORE, ALWAYS.",
            "conditions: {type: ALWAYS} protège tous les objets correspondant aux matériaux du groupe.",
            "Un nouveau groupe peut être ajouté sous le nom de votre choix, avec ses propres materials et conditions."
    })
    public Map<String, ConfirmationGroup> confirmationGroups = defaultGroups();

    public int secondsBeforeReset = 5;
    public boolean perItemConfirmation = true;
    public boolean resetConfirmAfterDrop = false;
    public List<String> blacklistedWorld = List.of("pvp");
    public Messages messages = new Messages();

    public boolean requiresConfirmation(ConfirmationContext context) {
        return confirmationGroups.values().stream().anyMatch(group -> group.matches(context));
    }

    @Override
    public void validate() {
        Objects.requireNonNull(confirmationGroups, "confirmation_groups");
        confirmationGroups.forEach((id, group) -> Objects.requireNonNull(group, "confirmation_groups." + id));
        Objects.requireNonNull(blacklistedWorld, "blacklisted_world");
        Objects.requireNonNull(messages, "messages");
        if (secondsBeforeReset < 1) {
            throw new IllegalArgumentException("seconds_before_reset doit être supérieur ou égal à 1");
        }
    }

    private static Map<String, ConfirmationGroup> defaultGroups() {
        Map<String, ConfirmationGroup> groups = new LinkedHashMap<>();
        groups.put("custom_items", new ConfirmationGroup(List.of(
                "DRAGON_EGG", "MOB_SPAWNER", "SPAWNER", "ELYTRA", "BLAZE_ROD", "BOW",
                "DIAMOND_SWORD", "DIAMOND_HELMET", "DIAMOND_CHESTPLATE", "DIAMOND_LEGGINGS",
                "DIAMOND_BOOTS", "DIAMOND_PICKAXE", "DIAMOND_AXE", "DIAMOND_HOE", "DIAMOND_SHOVEL",
                "NETHERITE_SWORD", "NETHERITE_HELMET", "NETHERITE_CHESTPLATE", "NETHERITE_LEGGINGS",
                "NETHERITE_BOOTS", "NETHERITE_PICKAXE", "NETHERITE_AXE", "NETHERITE_HOE", "NETHERITE_SHOVEL",
                "CROSSBOW", "MACE", "GOLD_HOE", "GOLDEN_HOE", "FISHING_ROD", "HOPPER", "CHEST",
                "CAULDRON", "LEATHER_HELMET", "LEATHER_CHESTPLATE", "LEATHER_LEGGINGS", "LEATHER_BOOTS",
                "STICK", "FURNACE", "CLOCK", "WATCH", "SKULL_ITEM", "PLAYER_HEAD", "WRITABLE_BOOK", "BOOK",
                "ENCHANTED_BOOK", "TRIPWIRE_HOOK"),
                new ConditionExpression.Any(List.of(
                        new ConditionExpression.Leaf(ConfirmationCondition.RENAMED),
                        new ConditionExpression.Leaf(ConfirmationCondition.ENCHANTED),
                        new ConditionExpression.Leaf(ConfirmationCondition.CUSTOM_LORE)))));
        groups.put("containers", new ConfirmationGroup(List.of(
                "SHULKER_BOX", "WHITE_SHULKER_BOX", "ORANGE_SHULKER_BOX", "MAGENTA_SHULKER_BOX",
                "LIGHT_BLUE_SHULKER_BOX", "YELLOW_SHULKER_BOX", "LIME_SHULKER_BOX", "PINK_SHULKER_BOX",
                "GRAY_SHULKER_BOX", "LIGHT_GRAY_SHULKER_BOX", "CYAN_SHULKER_BOX", "PURPLE_SHULKER_BOX",
                "BLUE_SHULKER_BOX", "BROWN_SHULKER_BOX", "GREEN_SHULKER_BOX", "RED_SHULKER_BOX", "BLACK_SHULKER_BOX",
                "BUNDLE", "WHITE_BUNDLE", "ORANGE_BUNDLE", "MAGENTA_BUNDLE", "LIGHT_BLUE_BUNDLE", "YELLOW_BUNDLE",
                "LIME_BUNDLE", "PINK_BUNDLE", "GRAY_BUNDLE", "LIGHT_GRAY_BUNDLE", "CYAN_BUNDLE", "PURPLE_BUNDLE",
                "BLUE_BUNDLE", "BROWN_BUNDLE", "GREEN_BUNDLE", "RED_BUNDLE", "BLACK_BUNDLE"),
                new ConditionExpression.Leaf(ConfirmationCondition.ALWAYS)));
        return groups;
    }

    public static final class ConfirmationGroup implements Validatable {
        public List<String> materials = List.of();
        @WithConverter(ConditionExpressionConverter.class)
        public ConditionExpression conditions = new ConditionExpression.Leaf(ConfirmationCondition.ALWAYS);

        public ConfirmationGroup() {
        }

        public ConfirmationGroup(List<String> materials, ConditionExpression conditions) {
            this.materials = materials;
            this.conditions = conditions;
        }

        public boolean matches(ConfirmationContext context) {
            return (materials.isEmpty() || materials.contains(context.material()))
                    && conditions.matches(condition -> condition.matches(context));
        }

        @Override
        public void validate() {
            Objects.requireNonNull(materials, "materials");
            Objects.requireNonNull(conditions, "conditions");
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
