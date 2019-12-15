package com.example.domain.Common.errorhanding.result;

import java.util.Optional;

 class Ok<V, E> implements Result<V, E> {

    private final V value;

    Ok(final V value) {
        super();
        this.value = value;
    }

    @Override
    public Optional<V> getValue() {
        return Optional.of(value);
    }

    @Override
    public Optional<E> getError() {
        return Optional.empty();
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public boolean isErr() {
        return false;
    }
}
