package com.example.domain.Common.function;

public interface Function<T, R> {
    R apply(T t);
}