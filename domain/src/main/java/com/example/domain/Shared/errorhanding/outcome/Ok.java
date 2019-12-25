package com.example.domain.Shared.errorhanding.outcome;

import java.util.Optional;

class Ok<E> implements Outcome<E> {

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public boolean isErr() {
        return false;
    }

    @Override
    public Optional<E> getError() {
        return Optional.empty();
    }
}
