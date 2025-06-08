package org.tdtu.ecommerceapi.utils;

import com.querydsl.core.types.dsl.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PredicateUtils {

    public static BooleanExpression getBooleanExpression(List<SearchCriteria> criteria, Class<?> classType) {
//        TODO: remove this line later
//        BooleanExpression exp = Expressions.asBoolean(true).isTrue();
//        for (SearchCriteria cr : criteria) {
//            exp = exp.and(PredicateUtils.getPredicate(cr.getKey(), cr.getOperator(), cr.getValue(), classType));
//        }

        BooleanExpression exp = null;
        for (SearchCriteria cr : criteria) {
            BooleanExpression predicate = PredicateUtils.getPredicate(cr.getKey(), cr.getOperator(), cr.getValue(), classType);
            if (predicate != null) {
                exp = (exp == null) ? predicate : exp.and(predicate);
            }
        }

        return exp;
    }

    public static BooleanExpression getPredicate(String key, String operator, String value, Class<?> classType) {
        PathBuilder<?> entityPath =
                new PathBuilder<>(classType, HelperUtils.getEntityVariable(classType.getSimpleName()));
        Class<?> propertyType = HelperUtils.getPropertyType(classType, key);
        return switch (propertyType.getSimpleName()) {
            case "UUID" -> getUUIDPredicate(key, operator, value, entityPath);
            case "String" -> getStringPredicate(key, operator, value, entityPath);
            case "Integer" -> getIntegerPredicate(key, operator, value, entityPath);
            case "Double" -> getDoublePredicate(key, operator, value, entityPath);
            case "LocalDate" -> getDatePredicate(key, operator, value, entityPath);
            case "LocalDateTime" -> getDateTimePredicate(key, operator, value, entityPath);
            default -> null;
        };
    }

    public static BooleanExpression getStringPredicate(
            String key, String operator, String value, PathBuilder<?> entityPath) {
        StringPath path = entityPath.getString(key);
        return switch (operator) {
            case "=" -> path.equalsIgnoreCase(value);
            case "-" -> path.containsIgnoreCase(value);
            case "%" -> path.startsWithIgnoreCase(value);
            case ":" -> path.in(Stream.of(value.split(";")).collect(Collectors.toList()));
            default -> null;
        };
    }

    public static BooleanExpression getUUIDPredicate(
            String key, String operator, String value, PathBuilder<?> entityPath) {
        ComparablePath<UUID> path = entityPath.getComparable(key, UUID.class);
        if (operator.equals("=")) {
            return path.eq(UUID.fromString(value));
        }
        return null;
    }

    public static BooleanExpression getIntegerPredicate(
            String key, String operator, String value, PathBuilder<?> entityPath) {
        NumberPath<Integer> path = entityPath.getNumber(key, Integer.class);
        return switch (operator) {
            case "=" -> path.eq(Integer.parseInt(value));
            case "!=" -> path.ne(Integer.parseInt(value));
            case ">" -> path.gt(Integer.parseInt(value));
            case "<" -> path.lt(Integer.parseInt(value));
            case ">=" -> path.goe(Integer.parseInt(value));
            case "<=" -> path.loe(Integer.parseInt(value));
            case ":" -> path.in(Stream.of(value.split(";")).map(Integer::parseInt).toArray(Integer[]::new));
            case "()" -> {
                String[] valueRange = HelperUtils.getValueRange(value);
                yield path.between(
                        valueRange[0].equals(" ") ? null : Integer.parseInt(valueRange[0]),
                        valueRange[1].equals(" ") ? null : Integer.parseInt(valueRange[1]));
            }
            default -> null;
        };
    }

    public static BooleanExpression getDoublePredicate(
            String key, String operator, String value, PathBuilder<?> entityPath) {
        NumberPath<Double> path = entityPath.getNumber(key, Double.class);
        return switch (operator) {
            case "=" -> path.eq(Double.parseDouble(value));
            case "!=" -> path.ne(Double.parseDouble(value));
            case ">" -> path.gt(Double.parseDouble(value));
            case "<" -> path.lt(Double.parseDouble(value));
            case ">=" -> path.goe(Double.parseDouble(value));
            case "<=" -> path.loe(Double.parseDouble(value));
            case ":" -> path.in(Stream.of(value.split(";")).map(Double::parseDouble).toArray(Double[]::new));
            case "()" -> {
                String[] valueRange = HelperUtils.getValueRange(value);
                yield path.between(
                        valueRange[0].equals(" ") ? null : Double.parseDouble(valueRange[0]),
                        valueRange[1].equals(" ") ? null : Double.parseDouble(valueRange[1]));
            }
            default -> null;
        };
    }

    public static BooleanExpression getDatePredicate(
            String key, String operator, String value, PathBuilder<?> entityPath) {
        DatePath<LocalDate> path = entityPath.getDate(key, LocalDate.class);
        return switch (operator) {
            case "=" -> path.eq(LocalDate.parse(value));
            case ">" -> path.gt(LocalDate.parse(value));
            case "<" -> path.lt(LocalDate.parse(value));
            case ">=" -> path.goe(LocalDate.parse(value));
            case "<=" -> path.loe(LocalDate.parse(value));
            case "()" -> {
                String[] valueRange = HelperUtils.getValueRange(value);
                yield path.between(
                        valueRange[0].equals(" ") ? null : LocalDate.parse(valueRange[0]),
                        valueRange[1].equals(" ") ? null : LocalDate.parse(valueRange[1]));
            }
            default -> null;
        };
    }

    public static BooleanExpression getDateTimePredicate(
            String key, String operator, String value, PathBuilder<?> entityPath) {
        DatePath<LocalDateTime> path = entityPath.getDate(key, LocalDateTime.class);
        return switch (operator) {
            case "=" -> path.eq(LocalDateTime.parse(value));
            case ">" -> path.gt(LocalDateTime.parse(value));
            case "<" -> path.lt(LocalDateTime.parse(value));
            case ">=" -> path.goe(LocalDateTime.parse(value));
            case "<=" -> path.loe(LocalDateTime.parse(value));
            case "()" -> {
                String[] valueRange = HelperUtils.getValueRange(value);
                yield path.between(
                        valueRange[0].equals(" ") ? null : LocalDateTime.parse(valueRange[0]),
                        valueRange[1].equals(" ") ? null : LocalDateTime.parse(valueRange[1]));
            }
            default -> null;
        };
    }
}
