package com.example.domain.Cash.cashBook;

import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Collectible.collectibleTransaction.CollectibleTransaction;
import com.example.domain.Shared.commandBaseClass.book.Book;
import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripBook.TripBook;
import com.example.domain.Trip.tripExpense.TripExpense;
import com.example.domain.Trip.tripExpense.paymentdetail.PaymentDetail;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;

import java.util.Optional;

public class CashRecordService {

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public static class Save {
            public enum CollTrans {
                SOURCE_TRANS_NOT_EXIST,
                DIFFERENT_TRIP,
                INVALID_DATE

            }

            public enum TripExpense {
                SOURCE_TRANS_NOT_EXIST,
                DIFFERENT_TRIP,
                INVALID_DATE,
                USER_NOT_PAYING
            }
        }

        public enum Remove {
            DIFFERENT_TRIP,
            SOURCE_STILL_EXIST,
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ------------------------------------Collectible Transaction----------------------------------

    public Outcome<Err.Save.CollTrans> saveRecord(TripBook tripBk,
                                                  CashBook cashBk,
                                                  CollectibleBook collectBk,
                                                  CashRecordFactory factory,
                                                  ID collTransId) {
        // Validation
        // -- Validate RecordSource Trans exist
        Optional<CollectibleTransaction> sourceTrans = collectBk.getCollectibleTrans().get(collTransId);
        if (!sourceTrans.isPresent())
            return Outcome.err(Err.Save.CollTrans.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Trip & Date
        if (isDifferentTrip(tripBk, cashBk, collectBk))
            return Outcome.err(Err.Save.CollTrans.DIFFERENT_TRIP);
        if (notDuringTrip(tripBk, sourceTrans.get().getDate()))
            return Outcome.err(Err.Save.CollTrans.INVALID_DATE);

        // Success
        cashBk.saveCashRecord(factory.create(sourceTrans.get()));
        return Outcome.ok();

    }

    @Deprecated
    public Outcome<Err.Remove> removeRecord(CollectibleBook collectibleBook,
                                            CashBook cashBook,
                                            ID collTransId) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(cashBook, collectibleBook))
            return Outcome.err(Err.Remove.DIFFERENT_TRIP);

        // -- Validate RecordSource delete
        boolean sourceStillExist = !collectibleBook.getCollectibleTrans().contain(collTransId);
        if (sourceStillExist) return Outcome.err(Err.Remove.SOURCE_STILL_EXIST);

        // Success
        cashBook.removeCashRecord(collTransId);
        return Outcome.ok();
    }

    // ------------------------------------TripExpense Transaction----------------------------------


    public Outcome<Err.Save.TripExpense> saveRecord(TripBook tripBk,
                                                    CashBook cashBk,
                                                    CashRecordFactory factory,
                                                    ID tripExpenseId) {
        // Validation
        // -- Validate RecordSource Trans exist
        Optional<TripExpense> sourceTrans = tripBk.getTripExpenses().get(tripExpenseId);
        if (!sourceTrans.isPresent())
            return Outcome.err(Err.Save.TripExpense.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Trip & Date
        if (isDifferentTrip(tripBk, cashBk))
            return Outcome.err(Err.Save.TripExpense.DIFFERENT_TRIP);
        if (notDuringTrip(tripBk, sourceTrans.get().getDate()))
            return Outcome.err(Err.Save.TripExpense.INVALID_DATE);

        // -- Validate User is paying
        if (userNotPaying(sourceTrans.get()))
            return Outcome.err(Err.Save.TripExpense.USER_NOT_PAYING);

        // Success
        cashBk.saveCashRecord(factory.create(sourceTrans.get()));
        return Outcome.ok();
    }

    @Deprecated
    public Outcome<Err.Remove> removeRecord(TripBook tripBook,
                                            CashBook cashBook,
                                            ID tripExpenseId) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(cashBook, tripBook)) return Outcome.err(Err.Remove.DIFFERENT_TRIP);

        // -- Validate RecordSource delete
        boolean sourceStillExist = !tripBook.getTripExpenses().contain(tripExpenseId);
        if (sourceStillExist) return Outcome.err(Err.Remove.SOURCE_STILL_EXIST);

        // Success
        cashBook.removeCashRecord(tripExpenseId);
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
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
