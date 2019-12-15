package com.example.domain.Common.sharedvalueobject.monetaryamount;

import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.result.Result;

import java.math.BigDecimal;


public class MonetaryAmount {

    // region Factory method -----------------------------------------------------------------------
    private final static int ROUNDING_DECIMAL = 2;

    public static Result<MonetaryAmount, Err.Create> create(BigDecimal amount) {
        if (Check.isNull(amount)) return Result.err(Err.Create.NULL_AMOUNT);
        if (isZero(amount)) return Result.err(Err.Create.ZERO_AMOUNT);
        if (isNegative(amount)) return Result.err(Err.Create.NEGATIVE_AMOUNT);

        amount = setScaleToTwo(amount);
        return Result.ok(new MonetaryAmount(amount));
    }

    private static BigDecimal setScaleToTwo(BigDecimal amount) {
        return amount.setScale(ROUNDING_DECIMAL, BigDecimal.ROUND_UNNECESSARY);
    }

    private static boolean isNotRoundedToTwo(BigDecimal amount) {
        amount = amount.stripTrailingZeros();
        return !(amount.scale() <= ROUNDING_DECIMAL);
    }

    private  static  boolean isPositive(BigDecimal bigDecimal){
        return bigDecimal.compareTo(BigDecimal.ZERO) > 0;
    }

    private static boolean isZero(BigDecimal bigDecimal){
        return bigDecimal.compareTo(BigDecimal.ZERO) == 0;
    }

    private static boolean isNegative(BigDecimal bigDecimal){
        return bigDecimal.compareTo(BigDecimal.ZERO) < 0;
    }

    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum Create {
            NULL_AMOUNT,
            ZERO_AMOUNT,
            NEGATIVE_AMOUNT
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------

    private final BigDecimal mAmount;

    private MonetaryAmount(BigDecimal amount) {
        this.mAmount = amount;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MonetaryAmount)) return false;

        MonetaryAmount amount = (MonetaryAmount) o;
        String amountString = amount.getAmount().toString();
        String mAmountString = mAmount.toString();
        return amountString.equals(mAmountString);
    }

    public BigDecimal getAmount() {
        return mAmount;
    }

    // ---------------------------------------------------------------------------------------------

}
