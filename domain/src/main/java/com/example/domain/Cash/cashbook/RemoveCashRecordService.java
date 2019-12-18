package com.example.domain.Cash.cashbook;

import com.example.domain.Collectible.collectiblebook.CollectibleBook;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripbook.TripBook;

public class RemoveCashRecordService {

    // region Error Class --------------------------------------------------------------------------
    public static enum Err {
        DIFFERENT_TRIP,
        SOURCE_STILL_EXIST,
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ------------------------------------Collectible Transaction----------------------------------
    public Outcome<Err> withSettlementId(CashBook cashBook, CollectibleBook collectibleBook, ID settlementId) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(cashBook, collectibleBook)) return Outcome.err(Err.DIFFERENT_TRIP);

        // -- Validate Source delete
        boolean sourceStillExist = !collectibleBook.getCollectibleTrans().contain(settlementId);
        if (sourceStillExist) return Outcome.err(Err.SOURCE_STILL_EXIST);

        // Success
        cashBook.removeCashRecord(settlementId);
        return Outcome.ok();
    }

    // ------------------------------------TripExpense Transaction----------------------------------
    public Outcome<Err> withTripExpenseId(CashBook cashBook, TripBook tripBook, ID tripExpenseId) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(cashBook, tripBook)) return Outcome.err(Err.DIFFERENT_TRIP);

        // -- Validate Source delete
        boolean sourceStillExist = !tripBook.getTripExpenses().contain(tripExpenseId);
        if (sourceStillExist) return Outcome.err(Err.SOURCE_STILL_EXIST);

        // Success
        cashBook.removeCashRecord(tripExpenseId);
        return Outcome.ok();
    }

    // --------------------------------------Cash Transaction---------------------------------------
    public void withCashTransId(CashBook cashBook, ID cashTransId) {
        cashBook.removeCashTransaction(cashTransId);
    }

    // region helper method ------------------------------------------------------------------------
    private boolean isDifferentTrip(Book book, Book... books) {
        for (Book b : books) {
            if (!book.getTripBookID().equals(b.getTripBookID())) return true;
        }
        return false;
    }
    // endregion helper method ---------------------------------------------------------------------

}
