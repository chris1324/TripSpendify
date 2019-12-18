package com.example.domain.Common.calculator;

import com.example.domain.Common.errorhanding.pair.Pair;
import com.example.domain.Common.baseclass.numeric.Numeric;

import java.math.BigDecimal;
import java.util.List;

public interface Calculator<N extends Numeric, R> {

    int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    enum DivideResult {
        NO_ROUNDING_ERROR,
        ROUNDING_ERROR
    }

    N sum(N... amounts);

    N sum(List<N> amounts);

    N multiply(N amount, N time);

    R subtract(N from, N to);

    Pair<List<N>, DivideResult> divideEqually(N totalAmount, int shares);

    N divide(N divideThis, N byThis);

}
