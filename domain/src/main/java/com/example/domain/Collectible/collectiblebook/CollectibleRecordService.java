package com.example.domain.Collectible.collectiblebook;

import com.example.domain.Collectible.collectiblerecord.CollectibleRecord;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.sharedvalueobject.numeric.Amount;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripbook.TripBook;
import com.example.domain.Trip.tripexpense.TripExpense;

import java.util.Optional;

public class CollectibleRecordService {

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum AddRecord {
            SOURCE_TRANS_NOT_EXIST,
            DIFFERENT_TRIP,
            NO_BORROWING_OR_LENDING
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ----------------------------------------- TripExpense ---------------------------------------
    public Outcome<Err.AddRecord> addRecord(TripBook tripBk,
                                            CollectibleBook collectibleBk,
                                            CollectibleRecordFactory factory,
                                            ID tripExpenseId) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, collectibleBk))
            return Outcome.err(Err.AddRecord.DIFFERENT_TRIP);

        // -- Validate Source Trans exist
        Optional<TripExpense> sourceTrans = tripBk.getTripExpenses().get(tripExpenseId);
        if (!sourceTrans.isPresent()) return Outcome.err(Err.AddRecord.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Borrowing / Lending occurs
        if (noBorrowingOrLending(sourceTrans.get()))
            return Outcome.err(Err.AddRecord.NO_BORROWING_OR_LENDING);

        // Success
        collectibleBk.addCollectibleRecord(factory.create(sourceTrans.get()));
        return Outcome.ok();
    }



    // region helper method ------------------------------------------------------------------------
    private boolean isDifferentTrip(Book book, Book... books) {
        return !book.isSameTrip(books);
    }

    private boolean noBorrowingOrLending(TripExpense tripExpense) {
        // Prepare
        Amount userPayment = tripExpense.getPaymentDetail()
                .getUserPayment()
                .map(MonetaryAmount::getAmount)
                .orElse(Amount.createZeroAmount());
        Amount userSpending = tripExpense.getSplittingDetail()
                .getUserSpending()
                .map(MonetaryAmount::getAmount)
                .orElse(Amount.createZeroAmount());

        return userPayment.equals(userSpending);
    }
    // endregion helper method ---------------------------------------------------------------------
}