package com.example.domain.Common.errorhanding.outcome;

import java.util.Optional;

class Ok<E> implements Outcome<E> {

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public Optional<E> getError() {
        return Optional.empty();
    }
}
