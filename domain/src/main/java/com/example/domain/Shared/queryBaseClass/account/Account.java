package com.example.domain.Shared.queryBaseClass.account;

import com.example.domain.Shared.commandBaseClass.record.BookRecord;
import com.example.domain.Shared.queryBaseClass.acountRecord.AccountRecord;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.Amount;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Account {

    // region Calculator ---------------------------------------------------------------------------
    public static Calculator calculator() {
        return new BalanceCalculator();
    }

    public interface Calculator {
        <Record extends AccountRecord> Amount calculateBalance(List<Record> accRecords);
    }

    private static class BalanceCalculator implements Calculator {

        @Override
        public <Record extends AccountRecord> Amount calculateBalance(List<Record> accRecords) {
            Amount.Calculator calculator = Amount.calculator();

            List<Amount> positiveAmounts = this.filterAmount(accRecords, BookRecord.Source.Effect.INCREASE);
            List<Amount> negativeAmounts = this.filterAmount(accRecords, BookRecord.Source.Effect.DECREASE);

            Amount totalPositive = calculator.sum(positiveAmounts);
            Amount totalNegative = calculator.sum(negativeAmounts);
            return calculator.subtract(totalPositive, totalNegative);
        }

        // region helper method --------------------------------------------------------------------
        private <Record extends AccountRecord> List<Amount> filterAmount(List<Record> accRecords, BookRecord.Source.Effect effect) {
            return accRecords.stream()
                    .filter(recordDetail -> recordDetail.mSource.effectOnBalance() == effect)
                    .map(recordDetail -> recordDetail.mAmount)
                    .collect(Collectors.toList());
        }
        // endregion helper method -----------------------------------------------------------------
    }
    // endregion Calculator  -----------------------------------------------------------------------

    public final ID tripId;
    private final Calculator mCalculator;

    public Account(ID tripId) {
        this.tripId = tripId;
        mCalculator = calculator();
    }

    public <Record extends AccountRecord> Amount calculateBalance(List<Record> accRecords) {
        return mCalculator.calculateBalance(accRecords);
    }


}
