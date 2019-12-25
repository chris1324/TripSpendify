package com.example.domain.Shared.errorhanding.result;

import java.util.Optional;

public interface Result<V, E> {

    //RecordSource https://codingwithglee.blogspot.com/2017/03/bringing-rusts-result-type-to-java.html

    static <V, E> Result<V, E> ok(final V value) {
        return new Ok<V, E>(value);
    }

    static <V, E> Result<V, E> err(final E error) {
        return new Err<V, E>(error);
    }

    Optional<V> getValue();

    Optional<E> getError();


    boolean isOk();

    boolean isErr();
}
