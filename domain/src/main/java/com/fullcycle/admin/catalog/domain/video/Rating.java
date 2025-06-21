package com.fullcycle.admin.catalog.domain.video;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Rating {
    ER("ER"),
    L("L"),
    AGE_10("10"),
    AGE_12("12"),
    AGE_14("14"),
    AGE_16("16"),
    AGE_18("18");

    private final String name;

    public static Optional<Rating> of(final String name) {
       return Arrays.stream(Rating.values())
         .filter(it -> it.name.equalsIgnoreCase(name))
         .findFirst();
    }
}
