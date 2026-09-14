package fr.nivcoo.dropconfirmation.config;

import fr.nivcoo.dropconfirmation.condition.ConditionExpression;
import fr.nivcoo.dropconfirmation.condition.ConfirmationCondition;
import fr.nivcoo.utilsz.core.conversion.Converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ConditionExpressionConverter implements Converter<ConditionExpression> {
    private static final int MAX_DEPTH = 32;

    @Override
    public ConditionExpression read(Object raw, ConditionExpression fallback, Field field) {
        return raw == null ? fallback : readNode(raw, "conditions", 0);
    }

    private ConditionExpression readNode(Object raw, String path, int depth) {
        if (depth >= MAX_DEPTH) {
            throw new IllegalArgumentException(path + " dépasse la profondeur maximale de " + MAX_DEPTH);
        }
        if (!(raw instanceof Map<?, ?> node) || node.size() != 1) {
            throw new IllegalArgumentException(path + " doit contenir exactement une clé : type, all ou any");
        }
        if (node.containsKey("type")) {
            Object value = node.get("type");
            if (!(value instanceof String name) || name.isBlank()) {
                throw new IllegalArgumentException(path + ".type doit être un nom de condition");
            }
            try {
                return new ConditionExpression.Leaf(ConfirmationCondition.valueOf(name.trim().toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException(path + ".type inconnu : " + name, exception);
            }
        }
        String group = node.containsKey("all") ? "all" : node.containsKey("any") ? "any" : null;
        if (group == null) {
            throw new IllegalArgumentException(path + " doit utiliser type, all ou any");
        }
        if (!(node.get(group) instanceof List<?> children) || children.isEmpty()) {
            throw new IllegalArgumentException(path + "." + group + " doit être une liste non vide de conditions");
        }
        List<ConditionExpression> compiled = new ArrayList<>(children.size());
        for (int index = 0; index < children.size(); index++) {
            compiled.add(readNode(children.get(index), path + "." + group + "[" + index + "]", depth + 1));
        }
        return group.equals("all") ? new ConditionExpression.All(compiled) : new ConditionExpression.Any(compiled);
    }

    @Override
    public Object write(ConditionExpression value, Field field) {
        if (value == null) return null;
        return switch (value) {
            case ConditionExpression.Leaf leaf -> Map.of("type", leaf.type().name());
            case ConditionExpression.All all -> Map.of("all", all.children().stream().map(child -> write(child, field)).toList());
            case ConditionExpression.Any any -> Map.of("any", any.children().stream().map(child -> write(child, field)).toList());
        };
    }
}
