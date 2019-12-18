package com.example.domain.Budget.budgetbook;

import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripbook.TripBook;

public class RemoveBudgetRecordService {

    // region Error Class --------------------------------------------------------------------------
    public static enum Err {
        DIFFERENT_TRIP,
        SOURCE_STILL_EXIST,
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ---------------------------------------- TripExpense ----------------------------------------
    public Outcome<Err> withTripExpenseID(TripBook tripBk,
                                          BudgetBook budgetBk,
                                          ID tripExpenseID) {

        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, budgetBk)) return Outcome.err(Err.DIFFERENT_TRIP);

        // -- Validate Source delete
        boolean sourceStillExist = !tripBk.getTripExpenses().contain(tripExpenseID);
        if (sourceStillExist) return Outcome.err(Err.SOURCE_STILL_EXIST);

        //Success
        budgetBk.removeBudgetRecord(tripExpenseID);
        return Outcome.ok();
    }

    // -------------------------------------- Budget Transaction -----------------------------------
    public void withCashTransId(BudgetBook budgetBook, ID budgetTransId){
        budgetBook.removeBudgetTransaction(budgetTransId);
    }


    // region helper method ------------------------------------------------------------------------
    private boolean isDifferentTrip(Book book, Book... books) {
        return !book.isSameTrip(books);
    }
    // endregion helper method ---------------------------------------------------------------------
}
