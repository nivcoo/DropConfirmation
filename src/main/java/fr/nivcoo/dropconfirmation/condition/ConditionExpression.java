package fr.nivcoo.dropconfirmation.condition;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public sealed interface ConditionExpression permits ConditionExpression.Leaf,
        ConditionExpression.All, ConditionExpression.Any {
    boolean matches(Predicate<ConfirmationCondition> predicate);

    record Leaf(ConfirmationCondition type) implements ConditionExpression {
        public Leaf {
            Objects.requireNonNull(type, "type");
        }

        @Override
        public boolean matches(Predicate<ConfirmationCondition> predicate) {
            return predicate.test(type);
        }
    }

    record All(List<ConditionExpression> children) implements ConditionExpression {
        public All {
            children = List.copyOf(children);
            if (children.isEmpty()) throw new IllegalArgumentException("all doit contenir au moins une condition");
        }

        @Override
        public boolean matches(Predicate<ConfirmationCondition> predicate) {
            return children.stream().allMatch(child -> child.matches(predicate));
        }
    }

    record Any(List<ConditionExpression> children) implements ConditionExpression {
        public Any {
            children = List.copyOf(children);
            if (children.isEmpty()) throw new IllegalArgumentException("any doit contenir au moins une condition");
        }

        @Override
        public boolean matches(Predicate<ConfirmationCondition> predicate) {
            return children.stream().anyMatch(child -> child.matches(predicate));
        }
    }
}
