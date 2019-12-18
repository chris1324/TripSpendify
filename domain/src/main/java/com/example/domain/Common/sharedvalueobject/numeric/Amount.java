package com.example.domain.Common.sharedvalueobject.numeric;

import com.example.domain.Common.baseclass.numeric.Numeric;
import com.example.domain.Common.calculator.Calculator;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.pair.Pair;
import com.example.domain.Common.errorhanding.result.Result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Amount implements Numeric<Amount> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<Amount, Err.Create> create(BigDecimal amount) {
        if (Check.isNull(amount)) return Result.err(Err.Create.NULL_AMOUNT);
        return Result.ok(new Amount(amount));
    }

    public static Amount createZeroAmount() {
        return new Amount(BigDecimal.ZERO);
    }
    // endregion Factory method---------------------------------------------------------------------


    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum Create {
            NULL_AMOUNT
        }
    }

    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final static int ROUNDING_DECIMAL = 2;
    private final static int BIG_DECIMAL_EQUAL = 0;
    private final static int BIG_DECIMAL_GREATER_THAN = 1;
    private final static int BIG_DECIMAL_LESS_THAN = -1;

    private final BigDecimal mAmount;

    protected Amount(BigDecimal amount) {
        mAmount = this.setScaleToTwo(amount);
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // --------------------------------------Static-------------------------------------------------
    public static Calculator<Amount, Amount> calculator() {
        return new AmtCalculator();
    }

    // ------------------------------------------ Numeric ------------------------------------------
    public boolean isPositive() {
        return getAmount().compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isZero() {
        return getAmount().compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isNegative() {
        return getAmount().compareTo(BigDecimal.ZERO) < 0;
    }

    public Answer<Compare> compareTo(Amount amount) {
        final int i = mAmount.compareTo(amount.getAmount());
        switch (i) {
            case BIG_DECIMAL_EQUAL:
                return Answer.make(Compare.EQUAL);
            case BIG_DECIMAL_GREATER_THAN:
                return Answer.make(Compare.GREATER_THAN);
            case BIG_DECIMAL_LESS_THAN:
                return Answer.make(Compare.LESS_THAN);
        }
        throw new ImpossibleState();
    }

    @Override
    public Amount abs() {
        return new Amount(mAmount.abs());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Amount)) return false;
        Amount amount = (Amount) o;
        BigDecimal thisBig = mAmount;
        BigDecimal amountBig = amount.getAmount();
        return thisBig.toString().equals(amountBig.toString());
    }

    // region helper method ------------------------------------------------------------------------
    private BigDecimal setScaleToTwo(BigDecimal amount) {
        return amount.setScale(ROUNDING_DECIMAL, BigDecimal.ROUND_UNNECESSARY);
    }
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    public BigDecimal getAmount() {
        return mAmount;
    }
    // endregion Getter ----------------------------------------------------------------------------

    // region calculator  --------------------------------------------------------------------------
    private static class AmtCalculator implements Calculator<Amount, Amount> {
        private static int DEFAULT_ERROR_ADJUSTED_LIST_POSITION = 0;

        @Override
        public Amount sum(Amount... amounts) {
            // Prepare
            List<Amount> amountList = new ArrayList<>();
            Collections.addAll(amountList, amounts);

            // Delegate and Return
            return this.sum(amountList);
        }

        @Override
        public Amount sum(List<Amount> amounts) {
            // Prepare
            BigDecimal bigDecimal = BigDecimal.ZERO;

            // Calculate
            for (Amount amount : amounts) bigDecimal = bigDecimal.add(amount.getAmount());

            // Return
            return new Amount(bigDecimal);
        }

        @Override
        public Amount multiply(Amount amount, Amount time) {
            // Prepare
            BigDecimal amountInBig = amount.getAmount();
            BigDecimal timeBig = time.getAmount();

            // Calculate and Return
            return new Amount(amountInBig.multiply(timeBig));
        }

        @Override
        public Amount subtract(Amount from, Amount to) {
            // Prepare
            BigDecimal fromBig = from.getAmount();
            BigDecimal toBig = to.getAmount();

            // Calculate and Return
            return new Amount(fromBig.subtract(toBig));
        }

        @Override
        public Amount divide(Amount divideThis, Amount byThis) {
            // Prepare
            BigDecimal divideThisBig = divideThis.getAmount();
            BigDecimal byThisBig = byThis.getAmount();

            // Return
            return new Amount(divideThisBig.divide(byThisBig,DEFAULT_ROUNDING_MODE));
        }

        @Override
        public Pair<List<Amount>, DivideResult> divideEqually(Amount totalAmount, int shares) {
            // Prepare
            BigDecimal amountInBig = totalAmount.getAmount();

            // Calculate
            BigDecimal amountPerShareInBig = amountInBig.divide(new BigDecimal(shares), DEFAULT_ROUNDING_MODE);

            // Reconstruct
            Amount amountPerShare = new Amount(amountPerShareInBig);
            List<Amount> result = new ArrayList<>();
            for (int i = 0; i < shares; i++) result.add(amountPerShare);

            // Delegate Error Handing and Return
            return this.checkAndHandleRoundingError(result, totalAmount, amountPerShare);
        }

        // region Calculator helper method ---------------------------------------------------------
        private Pair<List<Amount>, DivideResult> checkAndHandleRoundingError(List<Amount> result,
                                                                             Amount totalAmount,
                                                                             Amount amountPerShare) {
            // Check If has rounding Error
            Amount recalculatedTotalAmount = this.sum(result);
            Amount roundingError = this.subtract(totalAmount, recalculatedTotalAmount);

            // No Rounding Error
            if (roundingError.isZero()) return Pair.make(result, DivideResult.NO_ROUNDING_ERROR);

                // Rounding Error
            else {
                Amount errorAdjustedAmount = this.sum(amountPerShare, roundingError);
                result.set(DEFAULT_ERROR_ADJUSTED_LIST_POSITION, errorAdjustedAmount);
                return Pair.make(result, DivideResult.ROUNDING_ERROR);
            }
        }

        // endregion Calculator helper method ------------------------------------------------------

    }
    // endregion calculator  -----------------------------------------------------------------------
}
