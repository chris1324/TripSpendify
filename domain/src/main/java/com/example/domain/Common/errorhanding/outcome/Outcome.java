package com.example.domain.Common.errorhanding.outcome;

import java.util.Optional;

public interface Outcome<E> {

    public static <E> Outcome<E> ok() {
        return new Ok<>();
    }

    public static <E> Outcome<E> err(E err) {
        return new Err<>(err);
    }

    boolean isOk();

    boolean isErr();

    Optional<E> getError();
}
