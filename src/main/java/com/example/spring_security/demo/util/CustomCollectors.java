package com.example.spring_security.demo.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toMap;


public class CustomCollectors {

    private CustomCollectors() {
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toLinkedMap(Function<? super T, ? extends K> keyMapper,
                                                                   Function<? super T, ? extends U> valueMapper) {
        return toLinkedMap(keyMapper, valueMapper, (u, u2) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        });
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>>
    toLinkedMap(Function<? super T, ? extends K> keyMapper,
                Function<? super T, ? extends U> valueMapper,
                BinaryOperator<U> mergeFunction) {
        return toMap(keyMapper, valueMapper, mergeFunction, LinkedHashMap::new);
    }
}
