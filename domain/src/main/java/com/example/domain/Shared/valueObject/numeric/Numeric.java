package com.example.domain.Shared.valueObject.numeric;

import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.errorhanding.pair.Pair;

import java.math.BigDecimal;
import java.util.List;

public interface Numeric<N extends Numeric> {

    // region answer -------------------------------------------------------------------------------
    public enum Compare {
        EQUAL,
        GREATER_THAN,
        LESS_THAN
    }

    // endregion answer ----------------------------------------------------------------------------

     boolean isPositive();

     boolean isZero();

     boolean isNegative();

     Answer<Compare> compareTo(N amount);

     N abs();

    // region Calculator  --------------------------------------------------------------------------
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
    // endregion Calculator  -----------------------------------------------------------------------
}
