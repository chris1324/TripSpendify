package com.example.domain.Shared.errorhanding.answer;

public class Answer<V> {

    private final V answer;

    public static <V> Answer<V> make(V answer) {
        if (answer == null) throw new RuntimeException("Answer must not be null");
        return new Answer<>(answer);
    }

    private Answer(V answer) {
        this.answer = answer;
    }

    public V getAnswer() {
        return answer;
    }
}
