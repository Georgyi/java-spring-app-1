package com.example.jobparser.database.specification;

import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class CustomSpecification {
    public static <T> Specification<T> hasString(String key, String value, MethodsEnum type) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) return null;

            switch (type) {
                case LIKE:
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), "%" + value.toLowerCase() + "%");
                default:
                    return criteriaBuilder.equal(root.get(key), value);
            }
        };
    }

    public static <T> Specification<T> hasLong(String key, Long value) {
        return (root, query, criteriaBuilder) -> value == null ? null : criteriaBuilder.equal(root.get(key), value);
    }

    public static <T> Specification<T> hasTime(String key, Instant value, MethodsEnum type) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) return null;

            switch (type) {
                case GT:
                    return criteriaBuilder.greaterThan(root.get(key), value);
                case LT:
                    return criteriaBuilder.lessThan(root.get(key), value);
                default:
                    return criteriaBuilder.equal(root.get(key), value);
            }
        };
    }
}
