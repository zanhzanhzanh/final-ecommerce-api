package org.tdtu.ecommerceapi.utils;

import lombok.SneakyThrows;
import org.tdtu.ecommerceapi.exception.BadRequestException;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HelperUtils {

    private static final Pattern filterPattern = Pattern.compile(RegexUtils.FILTER_REQUEST_PATTERN);

    public static List<SearchCriteria> formatSearchCriteria(String[] filter) {
        List<SearchCriteria> criteria = new ArrayList<>();
        if (!Objects.isNull(filter)) {
            Collection<SearchCriteria> collect =
                    Arrays.asList(filter).parallelStream()
                            .map(HelperUtils::validateFilterPattern)
                            .toList();
            criteria.addAll(collect);
        }
        return criteria;
    }

    public static SearchCriteria validateFilterPattern(String filter) {
        Matcher m = filterPattern.matcher(filter + "|");
        if (m.find()) {
            return SearchCriteria.builder()
                    .key(m.group(1))
                    .operator(m.group(2))
                    .value(m.group(3))
                    .build();
        } else {
            throw new BadRequestException("Invalid Filter format");
        }
    }

    public static Class<?> getPropertyType(Class<?> parent, String property) {
        List<String> propertyList = new LinkedList<>(Arrays.asList(property.split("\\.")));
        return getRecursiveType(parent, propertyList);
    }

    @SneakyThrows(NoSuchFieldException.class)
    public static Class<?> getRecursiveType(Class<?> parent, List<String> propertyList) {
        if (propertyList.size() > 1) {
            Field field = parent.getDeclaredField(propertyList.get(0));
            Class<?> child = field.getType();
            propertyList.remove(propertyList.get(0));
            if ("List".equals(child.getSimpleName())) {
                return child;
                // ParameterizedType type = (ParameterizedType) field.getGenericType();
                // return getRecursiveType((Class<?>) type.getActualTypeArguments()[0],
                // propertyList);
            }
            return getRecursiveType(child, propertyList);
        } else {
            if (parent.getSuperclass() != null) {
                for (Field field : parent.getSuperclass().getDeclaredFields()) {
                    if (field.getName().equals(propertyList.get(0))) {
                        return field.getType();
                    }
                }
            }
            return parent.getDeclaredField(propertyList.get(0)).getType();
        }
    }

    public static String getEntityVariable(String simpleClassName) {
        char[] c = simpleClassName.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    public static String[] getValueRange(String value) {
        if (value.startsWith(";")) {
            value = " ".concat(value);
        } else if (value.endsWith(";")) {
            value = value.concat(" ");
        }
        String[] valueRange = value.split(";");
        if (valueRange.length > 2) {
            throw new RuntimeException("Invalid Filter format");
        }
        return valueRange;
    }

    @SneakyThrows(IllegalAccessException.class)
    public static Map<String, Object> inspect(Object object) {
        if (object == null) {
            return new HashMap<>();
        }
        Map<String, Object> map = new HashMap<>();
        List<Class<?>> superClassList = getSuperClasses(object);
        superClassList.add(object.getClass());
        for (Class<?> clazz : superClassList) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(object));
                } catch (InaccessibleObjectException e) {
                    // Skip inaccessible fields
                    System.err.println("Skipping inaccessible field: " + field.getName());
                }
            }
        }
        return map;
    }

    public static List<Class<?>> getSuperClasses(Object object) {
        if (object == null) {
            return Collections.emptyList();
        }
        List<Class<?>> superClassList = new ArrayList<>();
        Class<?> superClass = object.getClass().getSuperclass();
        while (superClass != null) {
            superClassList.add(0, superClass);
            superClass = superClass.getSuperclass();
        }
        return superClassList;
    }

    public static String getRandomString() {
        UUID uuid = UUID.randomUUID();
        long timestamp = System.currentTimeMillis() / 1000;
        return uuid.toString().replace("-", "").concat(String.valueOf(timestamp));
    }

    /**
     * Remove unnecessary headers which doesn't begin with x-amz- Format value String "[value]" ->
     * "value"
     */
    public static Map<String, String> formatS3SignedHeaders(Map<String, String> signedHeaders) {
        signedHeaders.entrySet().removeIf(e -> !e.getKey().startsWith("x-amz-"));
        signedHeaders.replaceAll((k, v) -> v.substring(1, v.length() - 1));
        return signedHeaders;
    }

    public static Class<?> getWrapperType(Class<?> primitiveType) {
        return switch (primitiveType.getName()) {
            case "int" -> Integer.class;
            case "double" -> Double.class;
            case "long" -> Long.class;
            case "float" -> Float.class;
            case "boolean" -> Boolean.class;
            case "char" -> Character.class;
            case "byte" -> Byte.class;
            case "short" -> Short.class;
            default -> primitiveType;
        };
    }
}
