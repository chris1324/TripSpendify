package com.example.domain.Common.calculator;

import com.example.domain.Common.sharedvalueobject.monetaryamount.MonetaryAmount;

import java.util.List;

public interface Calculator {

    public static Calculator get() {
        return new CalculatorImpl();
    }

    MonetaryAmount sum(MonetaryAmount... amounts);

    MonetaryAmount sum(List<MonetaryAmount> amounts);



}
