package com.github.rutledgepaulv.util;

import com.github.rutledgepaulv.exceptions.InvalidFieldReference;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class FieldUtils {
    private FieldUtils() {
    }


    public static List<Field> getUniqueFieldsWithRespectForInheritanceChain(Class<?> clazz) {
        Class<?> current = clazz;
        Map<String, Field> fields = new HashMap<>();

        while (!current.equals(Object.class)) {

            List<Field> fieldsFromCurrent = Arrays.asList(current.getDeclaredFields());
            fieldsFromCurrent.forEach(field -> {
                if(!fields.containsKey(field.getName())) {
                    fields.put(field.getName(), field);
                }
            });

            current = current.getSuperclass();
        }

        return fields.values().stream().collect(Collectors.toList());
    }

    public synchronized static Object resolveValueForFieldOnInstance(Field field, Object instance) {
        boolean original = field.isAccessible();
        try {
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new InvalidFieldReference("Cannot access field on instance.");
        } finally {
            field.setAccessible(original);
        }
    }
}
