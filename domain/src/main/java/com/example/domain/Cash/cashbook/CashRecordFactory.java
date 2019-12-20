package com.example.domain.Cash.cashbook;

import com.example.domain.Cash.cashrecord.CashRecord;
import com.example.domain.Cash.cashtransaction.CashTransaction;
import com.example.domain.Collectible.collectibletransaction.CollectibleTransaction;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Trip.tripexpense.TripExpense;

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
    private CashRecord.Source getSourceFromCollTrans(CollectibleTransaction trans) {
        // Prepare
        CollectibleTransaction.Type transactionType = trans.getTransactionType();
        boolean userIsPayer = trans.isUserPayer();

        // Compare and Return
        switch (transactionType) {
            case CONTRIBUTION:
                if (userIsPayer) return CashRecord.Source.COLL_TRANS_CONTRIBUTION_MADE;
                return CashRecord.Source.COLL_TRANS_CONTRIBUTION_ACCEPTED;
            case SETTLEMENT:
                if (userIsPayer) return CashRecord.Source.COLL_TRANS_SETTLEMENT_MADE;
                return CashRecord.Source.COLL_TRANS_SETTLEMENT_ACCEPTED;
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
                        CashRecord.Source.TRIP_EXPENSE,
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
    private CashRecord.Source getSourceFromCashTrans(CashTransaction transaction) {
        switch (transaction.getTransactionType()) {
            case WITHDRAWAL:
                return CashRecord.Source.CASH_TRANS_WITHDRAWAL;
            case DEPOSIT:
                return CashRecord.Source.CASH_TRANS_DEPOSIT;
            case ADJUSTMENT_UP:
                return CashRecord.Source.CASH_TRANS_ADJUST_UP;
            case ADJUSTMENT_DOWN:
                return CashRecord.Source.CASH_TRANS_ADJUST_DOWN;
            default:
                throw new UnexpectedEnumValue();
        }
    }
    // endregion helper method --------------------------------------------------------------------


}
