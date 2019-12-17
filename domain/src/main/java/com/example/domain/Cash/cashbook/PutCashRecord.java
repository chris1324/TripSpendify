package com.example.domain.Cash.cashbook;

import com.example.domain.Cash.cashrecord.CashRecord;
import com.example.domain.Cash.cashtransaction.CashTransaction;
import com.example.domain.Collectible.collectiblebook.CollectibleBook;
import com.example.domain.Collectible.collectibletransaction.CollectibleTransaction;
import com.example.domain.Common.entity.book.Book;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Trip.tripbook.TripBook;
import com.example.domain.Trip.tripexpense.TripExpense;
import com.example.domain.Trip.tripexpense.paymentdetail.PaymentDetail;

import java.util.Optional;

public class PutCashRecord {

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum CollectibleTrans {
            INVALID_SOURCE_TYPE,
            SOURCE_TRANS_NOT_EXIST,
            DIFFERENT_TRIP,
            INVALID_DATE
        }

        public enum TripExpense {
            INVALID_SOURCE_TYPE,
            SOURCE_TRANS_NOT_EXIST,
            DIFFERENT_TRIP,
            INVALID_DATE,
            USER_NOT_PAYING
        }

        public enum CashTrans {
            DIFFERENT_TRIP,
            INVALID_DATE
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ------------------------------------Collectible Transaction----------------------------------
    public Outcome<Err.CollectibleTrans> fromCollectibleTrans(TripBook tripBk,
                                                  CashBook cashBk,
                                                  CollectibleBook collectBk,
                                                  CashRecord cashRecord) {
        // Validation
        // -- Validate Source Type
        if (invalidSource(cashRecord, CashRecord.Source.COLLECTIBLE_TRANS))
            return Outcome.err(Err.CollectibleTrans.INVALID_SOURCE_TYPE);

        // -- Validate Source Trans exist
        Optional<CollectibleTransaction> sourceTrans = collectBk.getCollectibleTrans().get(cashRecord.getSourceTransId());
        if (!sourceTrans.isPresent()) return Outcome.err(Err.CollectibleTrans.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Trip & Date
        if (isDifferentTrip(tripBk, cashBk, collectBk))
            return Outcome.err(Err.CollectibleTrans.DIFFERENT_TRIP);
        if (notDuringTrip(tripBk, sourceTrans.get().getDate()))
            return Outcome.err(Err.CollectibleTrans.INVALID_DATE);

        // Success
        cashBk.putCashRecord(cashRecord);
        return Outcome.ok();
    }

    // ------------------------------------TripExpense Transaction----------------------------------
    public Outcome<Err.TripExpense> fromTripExpense(TripBook tripBk,
                                                    CashBook cashBk,
                                                    CashRecord cashRecord) {
        // Validation
        // -- Validate Source Type
        if (invalidSource(cashRecord, CashRecord.Source.TRIP_EXPENSE))
            return Outcome.err(Err.TripExpense.INVALID_SOURCE_TYPE);

        // -- Validate Source Trans exist
        Optional<TripExpense> sourceTrans = tripBk.getTripExpenses().get(cashRecord.getSourceTransId());
        if (!sourceTrans.isPresent()) return Outcome.err(Err.TripExpense.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Trip & Date
        if (isDifferentTrip(tripBk, cashBk))
            return Outcome.err(Err.TripExpense.DIFFERENT_TRIP);
        if (notDuringTrip(tripBk, sourceTrans.get().getDate()))
            return Outcome.err(Err.TripExpense.INVALID_DATE);

        // -- Validate User is paying
        if (userNotPaying(sourceTrans.get())) return Outcome.err(Err.TripExpense.USER_NOT_PAYING);

        // Success
        cashBk.putCashRecord(cashRecord);
        return Outcome.ok();
    }

    // --------------------------------------Cash Transaction---------------------------------------
    public Outcome<Err.CashTrans> fromCashTrans(TripBook tripBk,
                                                CashBook cashBk,
                                                CashTransaction cashTrans) {
        // Validation
        // -- Validate Trip & Date
        if (isDifferentTrip(tripBk, cashBk))
            return Outcome.err(Err.CashTrans.DIFFERENT_TRIP);
        if (notDuringTrip(tripBk, cashTrans.getDate()))
            return Outcome.err(Err.CashTrans.INVALID_DATE);

        // Success
        cashBk.putCashTransaction(cashTrans);
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    private boolean invalidSource(CashRecord cashRecord, CashRecord.Source source) {
        return cashRecord.getSource() != source;
    }

    private boolean notDuringTrip(TripBook tripBk, Date transDate) {
        return tripBk.isDuringTrip(transDate);
    }

    private boolean isDifferentTrip(Book book, Book... books) {
        return !book.isSameTrip(books);
    }

    private boolean userNotPaying(TripExpense tripExpense) {
        Answer<PaymentDetail.Payer> payerAnswer = tripExpense.getPaymentDetail().whoIsPaying();
        switch (payerAnswer.getAnswer()) {
            case USER:
            case USER_AND_MEMBER:
                return false;
            case MEMBER:
            case UNPAID:
                return true;
            default:
                throw new UnhandledError();
        }
    }
    // endregion helper method ---------------------------------------------------------------------
}
