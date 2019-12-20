package com.example.domain.Common.sharedvalueobject.numeric;

import com.example.domain.Common.baseclass.numeric.Numeric;
import com.example.domain.Common.calculator.Calculator;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.IntentionallyUnhandledError;
import com.example.domain.Common.errorhanding.pair.Pair;
import com.example.domain.Common.errorhanding.result.Result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MonetaryAmount implements Numeric<MonetaryAmount> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<MonetaryAmount, Err.Create> create(Amount amount) {
        if (Check.isNull(amount)) return Result.err(Err.Create.AMOUNT_NULL);
        if (amount.isNegative()) return Result.err(Err.Create.AMOUNT_NEGATIVE);
        if (amount.isZero()) return Result.err(Err.Create.AMOUNT_ZERO);

        return Result.ok(new MonetaryAmount(amount));
    }

    public static Result<MonetaryAmount, Err.Create> create(BigDecimal amountBig) {
        if (Check.isNull(amountBig)) return Result.err(Err.Create.AMOUNT_NULL);
        Amount amount = Amount.create(amountBig).getValue().orElseThrow(ImpossibleState::new);

        if (amount.isNegative()) return Result.err(Err.Create.AMOUNT_NEGATIVE);
        if (amount.isZero()) return Result.err(Err.Create.AMOUNT_ZERO);

        return Result.ok(new MonetaryAmount(amount));
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum Create {
            AMOUNT_NULL,
            AMOUNT_NEGATIVE,
            AMOUNT_ZERO
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final Amount mAmount;

    public MonetaryAmount(Amount amount) {
        mAmount = amount;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // --------------------------------------Static-------------------------------------------------
    public static Calculator<MonetaryAmount, Result<MonetaryAmount, Err.Create>> calculator() {
        return new MonetaryAmtCalculator();
    }

    // ------------------------------------------ Numeric ------------------------------------------

    public boolean isPositive() {
        return getAmount().isPositive();
    }

    public boolean isNegative() {
        return getAmount().isNegative();
    }

    public boolean isZero() {
        return getAmount().isZero();
    }

    public Answer<Compare> compareTo(MonetaryAmount amount) {
        return mAmount.compareTo(amount.getAmount());
    }

    @Override
    public MonetaryAmount abs() {
        return MonetaryAmount.create(mAmount.abs()).getValue().orElseThrow(ImpossibleState::new);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MonetaryAmount)) return false;
        MonetaryAmount amount = (MonetaryAmount) o;
        return this.getAmount().equals(amount.getAmount());
    }

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    public Amount getAmount() {
        return mAmount;
    }
    // endregion Getter ----------------------------------------------------------------------------

    // region calculator  --------------------------------------------------------------------------
    private static class MonetaryAmtCalculator implements Calculator<MonetaryAmount, Result<MonetaryAmount, Err.Create>> {
        @Override
        public MonetaryAmount sum(MonetaryAmount... amounts) {
            // Prepare
            List<MonetaryAmount> amountList = new ArrayList<>();
            Collections.addAll(amountList, amounts);

            // Delegate and Return
            return this.sum(amountList);
        }

        @Override
        public MonetaryAmount sum(List<MonetaryAmount> amounts) {
            // Prepare
            Amount amount = Amount.createZeroAmount();

            // Calculate
            for (MonetaryAmount monetaryAmt : amounts)
                amount = Amount.calculator().sum(amount, monetaryAmt.getAmount());

            // Return
            return MonetaryAmount.create(amount)
                    .getValue()
                    .orElseThrow(ImpossibleState::new);
        }

        @Override
        public MonetaryAmount multiply(MonetaryAmount amount, MonetaryAmount time) {
            // Delegate and Return
            return MonetaryAmount.create(Amount
                    .calculator()
                    .multiply(amount.getAmount(), time.getAmount()))
                    .getValue()
                    .orElseThrow(ImpossibleState::new);
        }

        @Override
        public Result<MonetaryAmount, Err.Create> subtract(MonetaryAmount from, MonetaryAmount to) {
            // Delegate and Return
            return MonetaryAmount.create(Amount
                    .calculator()
                    .subtract(from.getAmount(), to.getAmount()));
        }

        @Override
        public MonetaryAmount divide(MonetaryAmount divideThis, MonetaryAmount byThis) {
            // Delegate and Return
            return MonetaryAmount.create(Amount
                    .calculator()
                    .divide(divideThis.getAmount(), byThis.getAmount()))
                    .getValue()
                    .orElseThrow(ImpossibleState::new);
        }

        @Override
        public Pair<List<MonetaryAmount>, DivideResult> divideEqually(MonetaryAmount totalAmount, int shares) {
            // Delegate
            Pair<List<Amount>, DivideResult> result = Amount
                    .calculator()
                    .divideEqually(totalAmount.getAmount(), shares);

            // Return
            return Pair.make(
                    result.getLeft().stream()
                            .map(amount -> MonetaryAmount
                                    .create(amount)
                                    .getValue()
                                    .orElseThrow(() -> IntentionallyUnhandledError.maybe("Too small to be dividable")))
                            .collect(Collectors.toList()),
                    result.getRight()
            );
        }
    }
}
