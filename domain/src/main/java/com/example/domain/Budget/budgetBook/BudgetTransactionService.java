package com.example.domain.Budget.budgetBook;

import com.example.domain.Budget.budgetRecord.BudgetRecord;
import com.example.domain.Budget.budgetTransaction.BudgetTransaction;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.sharedValueObject.date.Date;
import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

public class BudgetTransactionService {


    // region Error Class --------------------------------------------------------------------------\
    public static enum Err {
        TRIP_DIFFERENT,
        DATE_INVALID
    }

    // endregion Error Class -----------------------------------------------------------------------
    public Outcome<Err> addBudgetTrans(TripBook tripBk,
                                       BudgetBook budgetBk,
                                       BudgetTransaction transaction) {
        // Validation
        // -- Validate Trip & Date
        if (isDifferentTrip(tripBk, budgetBk))
            return Outcome.err(Err.TRIP_DIFFERENT);
        if (notDuringTrip(tripBk, transaction.getDate()))
            return Outcome.err(Err.DATE_INVALID);

        // Success
        budgetBk.addBudgetTransaction(transaction, createRecordFromBudgetTrans(transaction));
        return Outcome.ok();
    }

    public void removeBudgetTrans(BudgetBook budgetBook,
                                  ID budgetTransId){
        budgetBook.removeBudgetTransaction(budgetTransId);
    }
     // region helper method ------------------------------------------------------------------------
    private BudgetRecord createRecordFromBudgetTrans(BudgetTransaction transaction) {
        return BudgetRecord
                .create(
                        transaction.getId(),
                        BudgetRecord.Source.BUDGET_TRANSACTION,
                        transaction.getAmount()
                )
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    private boolean notDuringTrip(TripBook tripBk, Date transDate) {
        return tripBk.isDuringTrip(transDate);
    }

    private boolean isDifferentTrip(Book book, Book... books) {
        return !book.isSameTrip(books);
    }
    // endregion helper method ---------------------------------------------------------------------
}
