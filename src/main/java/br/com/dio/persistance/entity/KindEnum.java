package br.com.dio.persistance.entity;

import java.util.stream.Stream;

public enum KindEnum {
    INITIAL,
    PENDING,
    CANCEL,
    FINAL;

    public static KindEnum findByName(final String name) {
        return Stream.of(KindEnum.values())
                .filter(k -> k.name().equals(name))
                .findFirst().orElse(null);
    }
}
