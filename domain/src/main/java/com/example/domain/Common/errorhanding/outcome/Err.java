package com.example.domain.Common.errorhanding.outcome;

import java.util.Optional;

class Err<E> implements Outcome<E>{

    private final E error;

    public Err(E error) {
        this.error = error;
    }

    @Override
    public boolean isOk() {
        return false;
    }


    @Override
    public Optional<E> getError() {
        return Optional.of(error);
    }
}
