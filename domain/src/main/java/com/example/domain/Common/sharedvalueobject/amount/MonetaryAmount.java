package com.example.domain.Common.sharedvalueobject.amount;

import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.result.Result;

import java.math.BigDecimal;


public class MonetaryAmount extends Amount {

    // region Factory method -----------------------------------------------------------------------
    public static Result<MonetaryAmount, Err.Create> create(BigDecimal amount) {
        if (Check.isNull(amount)) return Result.err(Err.Create.NULL_AMOUNT);
        if (isZero(amount)) return Result.err(Err.Create.ZERO_AMOUNT);
        if (isNegative(amount)) return Result.err(Err.Create.NEGATIVE_AMOUNT);
        if (isNotRoundedToTwo(amount)) return Result.err(Err.Create.NOT_ROUNDED_TO_TWO);

        amount = setScaleToTwo(amount);
        return Result.ok(new MonetaryAmount(amount));
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum Create {
            NULL_AMOUNT,
            ZERO_AMOUNT,
            NEGATIVE_AMOUNT,
            NOT_ROUNDED_TO_TWO
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    protected MonetaryAmount(BigDecimal amount) {
        super(amount);
    }
    // endregion Variables and Constructor ---------------------------------------------------------


    // region helper method ------------------------------------------------------------------------
    private static boolean isPositive(BigDecimal bigDecimal) {
        return bigDecimal.compareTo(BigDecimal.ZERO) > 0;
    }

    private static boolean isZero(BigDecimal bigDecimal) {
        return bigDecimal.compareTo(BigDecimal.ZERO) == 0;
    }

    private static boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.compareTo(BigDecimal.ZERO) < 0;
    }
    // endregion helper method ---------------------------------------------------------------------

    // ---------------------------------------------------------------------------------------------

}
