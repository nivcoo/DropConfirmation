package fr.nivcoo.dropconfirmation.condition;

import fr.nivcoo.dropconfirmation.condition.ConditionExpression.All;
import fr.nivcoo.dropconfirmation.condition.ConditionExpression.Any;
import fr.nivcoo.dropconfirmation.condition.ConditionExpression.Leaf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.EnumSet;
import java.util.List;

import static fr.nivcoo.dropconfirmation.condition.ConfirmationCondition.CUSTOM_LORE;
import static fr.nivcoo.dropconfirmation.condition.ConfirmationCondition.ENCHANTED;
import static fr.nivcoo.dropconfirmation.condition.ConfirmationCondition.RENAMED;
import static fr.nivcoo.dropconfirmation.condition.ConfirmationCondition.WHITELISTED;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class ConditionExpressionTest {

    @ParameterizedTest
    @CsvSource({
            "0, false, false",
            "1, false, true",
            "2, false, false",
            "3, true, true",
            "4, false, false",
            "5, true, true",
            "6, false, true",
            "7, true, true",
            "8, false, false",
            "9, true, true",
            "10, false, true",
            "11, true, true",
            "12, false, false",
            "13, true, true",
            "14, false, true",
            "15, true, true"
    })
    void preservesNestedAllAndAnyGroups(int mask, boolean expectedDefault, boolean expectedVariant) {
        List<ConfirmationCondition> conditions = List.of(WHITELISTED, RENAMED, ENCHANTED, CUSTOM_LORE);
        EnumSet<ConfirmationCondition> matched = EnumSet.noneOf(ConfirmationCondition.class);
        for (int index = 0; index < conditions.size(); index++) {
            if ((mask & (1 << index)) != 0) {
                matched.add(conditions.get(index));
            }
        }

        ConditionExpression defaultRule = new All(List.of(
                new Leaf(WHITELISTED),
                new Any(List.of(new Leaf(RENAMED), new Leaf(ENCHANTED), new Leaf(CUSTOM_LORE)))));
        ConditionExpression variant = new Any(List.of(
                new Leaf(WHITELISTED),
                new All(List.of(
                        new Leaf(RENAMED),
                        new Any(List.of(new Leaf(ENCHANTED), new Leaf(CUSTOM_LORE)))))));

        assertEquals(expectedDefault, defaultRule.matches(matched::contains),
                () -> "WHITELISTED AND (RENAMED OR ENCHANTED OR CUSTOM_LORE), matched=" + matched);
        assertEquals(expectedVariant, variant.matches(matched::contains),
                () -> "WHITELISTED OR (RENAMED AND (ENCHANTED OR CUSTOM_LORE)), matched=" + matched);
    }
}
