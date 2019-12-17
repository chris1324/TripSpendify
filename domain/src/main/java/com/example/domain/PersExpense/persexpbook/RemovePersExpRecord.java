package com.example.domain.PersExpense.persexpbook;

import com.example.domain.Cash.cashbook.RemoveCashRecord;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.entity.book.Book;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripbook.TripBook;

public class RemovePersExpRecord {

    // region Error Class --------------------------------------------------------------------------
    public static enum Err {
        DIFFERENT_TRIP,
        SOURCE_STILL_EXIST,
    }

    // endregion Error Class -----------------------------------------------------------------------

    // ------------------------------------TripExpense Transaction----------------------------------
    public Outcome<Err> withTripExpenseID(TripBook tripBk,
                                          PersExpBook persExpBk,
                                          ID tripExpenseId) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, persExpBk)) return Outcome.err(Err.DIFFERENT_TRIP);

        // -- Validate Source delete
        boolean sourceStillExist = !tripBk.getTripExpenses().contain(tripExpenseId);
        if (sourceStillExist) return Outcome.err(Err.SOURCE_STILL_EXIST);

        // Success
        persExpBk.removePersExpRecord(tripExpenseId);
        return Outcome.ok();
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
