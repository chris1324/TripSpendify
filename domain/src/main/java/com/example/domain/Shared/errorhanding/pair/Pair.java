package com.example.domain.Shared.errorhanding.pair;

public class Pair<left, right> {

    private final left valueA;
    private final right valueB;

    public static <V_A, V_B> Pair<V_A, V_B> make(V_A valueA, V_B valueB) {
        if (valueA == null) throw new RuntimeException("Answer must not be null");
        if (valueB == null) throw new RuntimeException("Answer must not be null");
        return new Pair<>(valueA, valueB);
    }

    private Pair(left valueA, right valueB) {
        this.valueA = valueA;
        this.valueB = valueB;
    }


    public left getLeft() {
        return valueA;
    }

    public right getRight() {
        return valueB;
    }
}
