package fr.nivcoo.dropconfirmation.condition;

import fr.nivcoo.dropconfirmation.condition.ConditionExpression.All;
import fr.nivcoo.dropconfirmation.condition.ConditionExpression.Any;
import fr.nivcoo.dropconfirmation.condition.ConditionExpression.Leaf;
import fr.nivcoo.dropconfirmation.config.MainConfig;
import fr.nivcoo.dropconfirmation.config.MainConfig.ConfirmationGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static fr.nivcoo.dropconfirmation.condition.ConfirmationCondition.ALWAYS;
import static fr.nivcoo.dropconfirmation.condition.ConfirmationCondition.CUSTOM_LORE;
import static fr.nivcoo.dropconfirmation.condition.ConfirmationCondition.ENCHANTED;
import static fr.nivcoo.dropconfirmation.condition.ConfirmationCondition.RENAMED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class ConditionExpressionTest {

    @ParameterizedTest
    @CsvSource({
            "0, false, false",
            "1, true, true",
            "2, true, false",
            "3, true, true",
            "4, true, false",
            "5, true, true",
            "6, true, true",
            "7, true, true"
    })
    void preservesNestedAllAndAnyGroups(int mask, boolean expectedDefault, boolean expectedVariant) {
        List<ConfirmationCondition> conditions = List.of(RENAMED, ENCHANTED, CUSTOM_LORE);
        EnumSet<ConfirmationCondition> matched = EnumSet.noneOf(ConfirmationCondition.class);
        for (int index = 0; index < conditions.size(); index++) {
            if ((mask & (1 << index)) != 0) {
                matched.add(conditions.get(index));
            }
        }

        ConditionExpression defaultRule = new Any(List.of(
                new Leaf(RENAMED), new Leaf(ENCHANTED), new Leaf(CUSTOM_LORE)));
        ConditionExpression variant = new Any(List.of(
                new Leaf(RENAMED),
                new All(List.of(new Leaf(ENCHANTED), new Leaf(CUSTOM_LORE)))));

        assertEquals(expectedDefault, defaultRule.matches(matched::contains),
                () -> "RENAMED OR ENCHANTED OR CUSTOM_LORE, matched=" + matched);
        assertEquals(expectedVariant, variant.matches(matched::contains),
                () -> "RENAMED OR (ENCHANTED AND CUSTOM_LORE), matched=" + matched);
    }

    @Test
    void laterMatchingGroupCanProtectMaterialRejectedByEarlierConditions() {
        MainConfig config = new MainConfig();
        config.confirmationGroups = new LinkedHashMap<>();
        config.confirmationGroups.put("renamed", new ConfirmationGroup(
                List.of("SHULKER_BOX"), new Leaf(RENAMED)));
        config.confirmationGroups.put("always", new ConfirmationGroup(
                List.of("SHULKER_BOX"), new Leaf(ALWAYS)));

        assertTrue(config.requiresConfirmation(new ConfirmationContext("SHULKER_BOX", null)));
    }

    @Test
    void unconditionalGroupOnlyProtectsItsOwnMaterials() {
        MainConfig config = new MainConfig();
        config.confirmationGroups = Map.of("containers", new ConfirmationGroup(
                List.of("SHULKER_BOX", "BUNDLE"), new Leaf(ALWAYS)));

        assertFalse(config.requiresConfirmation(new ConfirmationContext("STONE", null)));
        assertFalse(config.requiresConfirmation(new ConfirmationContext("DIAMOND_SWORD", null)));
    }

    @Test
    void groupsKeepIndependentConditionsForEquipmentAndContainers() {
        MainConfig config = new MainConfig();
        config.confirmationGroups = Map.of(
                "equipment", new ConfirmationGroup(List.of("DIAMOND_SWORD"), new Any(List.of(
                        new Leaf(RENAMED), new Leaf(ENCHANTED), new Leaf(CUSTOM_LORE)))),
                "containers", new ConfirmationGroup(List.of("SHULKER_BOX", "BUNDLE"), new Leaf(ALWAYS)));

        assertFalse(config.requiresConfirmation(new ConfirmationContext("DIAMOND_SWORD", null)));
        assertTrue(config.requiresConfirmation(new ConfirmationContext("SHULKER_BOX", null)));
        assertTrue(config.requiresConfirmation(new ConfirmationContext("BUNDLE", null)));
    }
}
