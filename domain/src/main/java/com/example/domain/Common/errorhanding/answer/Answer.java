package com.example.domain.Common.errorhanding.answer;

public class Answer<V> {

    private final V answer;

    public static <V> Answer<V> make(V answer) {
        return new Answer<>(answer);
    }

    private Answer(V answer) {
        if (answer == null) throw new RuntimeException("Answer must not be null");
        this.answer = answer;
    }

    public V getAnswer() {
        return answer;
    }
}
