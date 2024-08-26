package com.supernova.ssagrissakssak.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum StatValueType {
    VIEW("view_count"),
    LIKE("like_count"),
    SHARE("share_count"),
    DEFAULT("count")
    ;

    private final String value;

    public static StatValueType fromValue(String value) {
        return Arrays.stream(StatValueType.values())
                .filter(type -> type.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElse(DEFAULT);
    }

}