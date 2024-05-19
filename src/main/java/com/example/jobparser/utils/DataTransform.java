package com.example.jobparser.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataTransform {

    public static void map() {
        System.out.println("Performing action");
    }

    public static void mapWithoutNull(Object clazz, Object dto) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ mapWithoutNull started");

        try {
            var classPath = clazz.getClass();
            var classFields = classPath.getDeclaredFields();

            var dtoClassPath = dto.getClass();

            for (var field : classFields) {
                field.setAccessible(true);

                String fieldName = field.getName();
                var value = field.get(clazz); // Получаем значение поля объекта clazz

                if (value != null) {
                    var dtoField = dtoClassPath.getDeclaredField(fieldName);

                    dtoField.setAccessible(true);
                    dtoField.set(dto, value);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("We have a problem with: " + e.getLocalizedMessage());
        }
    }
}
