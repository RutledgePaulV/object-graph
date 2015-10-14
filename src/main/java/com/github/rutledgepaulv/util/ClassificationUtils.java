package com.github.rutledgepaulv.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ClassificationUtils {
    private ClassificationUtils() {
    }

    public static Set<Class<?>> primitives = new HashSet<>();

    static {
        primitives.add(Boolean.class);
        primitives.add(Short.class);
        primitives.add(Integer.class);
        primitives.add(Long.class);
        primitives.add(String.class);
        primitives.add(Character.class);
        primitives.add(Float.class);
        primitives.add(Double.class);
        primitives.add(Byte.class);
    }

    public static boolean isEnum(Class<?> clazz) {
        return clazz.isEnum();
    }

    public static boolean isEnum(Field field) {
        return field.isEnumConstant() || isEnum(field.getType());
    }

    private static boolean isPrimitiveReference(Class<?> clazz) {
        return clazz.isPrimitive() || primitives.contains(clazz) || isEnum(clazz);
    }

    private static boolean isPrimitiveArrayReference(Class<?> clazz) {
        return clazz.isArray() && isPrimitiveReference(clazz.getComponentType());
    }

    public static boolean isPrimitiveReference(Field field) {
        return isPrimitiveReference(field.getType()) || isPrimitiveArrayReference(field);
    }

    public static boolean isPrimitiveArrayReference(Field field) {
       return isPrimitiveArrayReference(field.getType());
    }

    public static List<Field> filterToObjects(Collection<Field> collection) {
        return filterSynthetic(collection).stream().filter(field -> !isPrimitiveReference(field)).collect(Collectors.toList());
    }

    public static List<Field> filterToPrimitives(Collection<Field> collection) {
        return filterSynthetic(collection).stream().filter(ClassificationUtils::isPrimitiveReference).collect(Collectors.toList());
    }

    public static List<Field> filterSynthetic(Collection<Field> collection) {
        return collection.stream().filter(field -> !field.isSynthetic()).collect(Collectors.toList());
    }

}
