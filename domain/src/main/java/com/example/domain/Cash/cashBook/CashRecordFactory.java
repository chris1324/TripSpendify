package com.example.domain.Cash.cashBook;

import com.example.domain.Cash.cashRecord.CashRecord;
import com.example.domain.Cash.cashTransaction.CashTransaction;
import com.example.domain.Collectible.collectibleTransaction.CollectibleTransaction;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Trip.tripExpense.TripExpense;

public class CashRecordFactory {

    // ------------------------------------Collectible Transaction----------------------------------
    public CashRecord create(CollectibleTransaction trans) {
        return CashRecord
                .create(
                        trans.getId(),
                        this.getSourceFromCollTrans(trans),
                        trans.getAmount()
                )
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    // region helper method ------------------------------------------------------------------------
    private CashRecord.SourceType getSourceFromCollTrans(CollectibleTransaction trans) {
        // Prepare
        CollectibleTransaction.Type transactionType = trans.getTransactionType();
        boolean userIsPayer = trans.isUserPayer();

        // Compare and Return
        switch (transactionType) {
            case CONTRIBUTION:
                if (userIsPayer) return CashRecord.SourceType.COLL_TRANS_CONTRIBUTION_MADE;
                return CashRecord.SourceType.COLL_TRANS_CONTRIBUTION_ACCEPTED;
            case SETTLEMENT:
                if (userIsPayer) return CashRecord.SourceType.COLL_TRANS_SETTLEMENT_MADE;
                return CashRecord.SourceType.COLL_TRANS_SETTLEMENT_ACCEPTED;
            default:
                throw new UnexpectedEnumValue();
        }

    }
    // endregion helper method ---------------------------------------------------------------------

    // ------------------------------------TripExpense Transaction----------------------------------
    CashRecord create(TripExpense tripExpense) {
        return CashRecord
                .create(
                        tripExpense.getId(),
                        CashRecord.SourceType.TRIP_EXPENSE,
                        tripExpense.getPaymentDetail().getUserPayment().orElseThrow(ImpossibleState::new)
                )
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    // --------------------------------------Cash Transaction---------------------------------------

    CashRecord create(CashTransaction transaction) {
        return CashRecord
                .create(
                        transaction.getId(),
                        this.getSourceFromCashTrans(transaction),
                        transaction.getAmount()
                )
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    // region helper method ------------------------------------------------------------------------
    private CashRecord.SourceType getSourceFromCashTrans(CashTransaction transaction) {
        switch (transaction.getTransactionType()) {
            case WITHDRAWAL:
                return CashRecord.SourceType.CASH_TRANS_WITHDRAWAL;
            case DEPOSIT:
                return CashRecord.SourceType.CASH_TRANS_DEPOSIT;
            case ADJUSTMENT_UP:
                return CashRecord.SourceType.CASH_TRANS_ADJUST_UP;
            case ADJUSTMENT_DOWN:
                return CashRecord.SourceType.CASH_TRANS_ADJUST_DOWN;
            default:
                throw new UnexpectedEnumValue();
        }
    }
    // endregion helper method --------------------------------------------------------------------


}
