package com.example.domain.Common.calculator;

import com.example.domain.Common.sharedvalueobject.amount.Amount;

import java.util.List;

public interface Calculator {

    public static Calculator get() {
        return new CalculatorImpl();
    }

    <A extends Amount> A sum(A... amounts);

    <A extends Amount> A sum(List<A> amounts);



}
