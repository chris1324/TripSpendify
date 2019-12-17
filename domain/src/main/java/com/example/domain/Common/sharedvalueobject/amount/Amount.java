package com.example.domain.Common.sharedvalueobject.amount;

import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.result.Result;

import java.math.BigDecimal;

public  class Amount {

    // region Factory method -----------------------------------------------------------------------
    public final static int ROUNDING_DECIMAL = 2;

    public static Result<Amount, Err.Create> createAmount(BigDecimal amount) {
        if (Check.isNull(amount)) return Result.err(Err.Create.NULL_AMOUNT);
        if (isNotRoundedToTwo(amount)) return Result.err(Err.Create.NOT_ROUNDED_TO_TWO);

        amount = setScaleToTwo(amount);
        return Result.ok(new Amount(amount));
    }

    public static Amount createZeroAmount(){
        return new Amount(BigDecimal.ZERO);
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum Create {
            NULL_AMOUNT,
            NOT_ROUNDED_TO_TWO
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final BigDecimal mAmount;

    protected Amount(BigDecimal amount) {
        mAmount = amount;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Amount)) return false;

        Amount amount = (Amount) o;
        String amountString = amount.getAmount().toString();
        String mAmountString = mAmount.toString();
        return amountString.equals(mAmountString);
    }

    protected static BigDecimal setScaleToTwo(BigDecimal amount) {
        return amount.setScale(ROUNDING_DECIMAL, BigDecimal.ROUND_UNNECESSARY);
    }

    protected static boolean isNotRoundedToTwo(BigDecimal amount) {
        amount = amount.stripTrailingZeros();
        return !(amount.scale() <= ROUNDING_DECIMAL);
    }

    // region Getter -------------------------------------------------------------------------------
    public BigDecimal getAmount() {
        return mAmount;
    }
    // endregion Getter ----------------------------------------------------------------------------

}
