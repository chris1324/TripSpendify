package com.example.domain.Common.baseclass.numeric;

import com.example.domain.Common.errorhanding.answer.Answer;

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

}
