package com.example.domain.Shared.errorhanding.result;

import java.util.Optional;

class Err<V, E> implements Result<V, E> {
     private final E error;

    Err(final E error) {
        super();
        this.error = error;
    }

    @Override
    public Optional<V> getValue() {
        return Optional.empty();
    }

    @Override
    public Optional<E> getError() {
        return Optional.of(error);
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public boolean isErr() {
        return true;
    }

}
