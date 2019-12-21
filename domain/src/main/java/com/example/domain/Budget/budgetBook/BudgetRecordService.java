package com.example.domain.Budget.budgetBook;

import com.example.domain.Budget.budgetRecord.BudgetRecord;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.sharedValueObject.date.Date;
import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;
import com.example.domain.Trip.tripExpense.TripExpense;
import com.example.domain.Trip.tripExpense.splittingdetail.SplittingDetail;

import java.util.Optional;

public class BudgetRecordService {
    // region Error Class --------------------------------------------------------------------------\
    public static class Err {
        public enum AddRecord {
            SOURCE_TRANS_NOT_EXIST,
            DIFFERENT_TRIP,
            INVALID_DATE,
            USER_NOT_SPENDING
        }

        public enum RemoveRecord {
            DIFFERENT_TRIP,
            SOURCE_STILL_EXIST,
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    public Outcome<Err.AddRecord> addRecord(TripBook tripBk,
                                            BudgetBook budgetBk,
                                            ID tripExpenseId) {
        // Validation
        // -- Validate Source Trans exist
        Optional<TripExpense> sourceTrans = tripBk.getTripExpenses().get(tripExpenseId);
        if (!sourceTrans.isPresent()) return Outcome.err(Err.AddRecord.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Trip & Date
        if (isDifferentTrip(tripBk, budgetBk))
            return Outcome.err(Err.AddRecord.DIFFERENT_TRIP);
        if (notDuringTrip(tripBk, sourceTrans.get().getDate()))
            return Outcome.err(Err.AddRecord.INVALID_DATE);

        // -- Validate User is spending
        if (userNotSpending(sourceTrans.get())) return Outcome.err(Err.AddRecord.USER_NOT_SPENDING);

        // Success
        budgetBk.addBudgetRecord(createRecordFromTripExpense(sourceTrans.get()));
        return Outcome.ok();
    }


    public Outcome<Err.RemoveRecord> removeRecord(TripBook tripBk,
                                                  BudgetBook budgetBk,
                                                  ID tripExpenseID){
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, budgetBk)) return Outcome.err(Err.RemoveRecord.DIFFERENT_TRIP);

        // -- Validate Source delete
        boolean sourceStillExist = !tripBk.getTripExpenses().contain(tripExpenseID);
        if (sourceStillExist) return Outcome.err(Err.RemoveRecord.SOURCE_STILL_EXIST);

        //Success
        budgetBk.removeBudgetRecord(tripExpenseID);
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    private boolean notDuringTrip(TripBook tripBk, Date transDate) {
        return tripBk.isDuringTrip(transDate);
    }

    private boolean isDifferentTrip(Book book, Book... books) {
        return !book.isSameTrip(books);
    }

    private boolean userNotSpending(TripExpense tripExpense) {
        Answer<SplittingDetail.Spender> spenderAnswer = tripExpense.getSplittingDetail().whoIsSpending();
        switch (spenderAnswer.getAnswer()) {
            case USER:
            case USER_AND_MEMBER:
                return false;
            case MEMBER:
                return true;
            default:
                throw new UnhandledError();
        }
    }


    private BudgetRecord createRecordFromTripExpense(TripExpense tripExpense) {
        return BudgetRecord
                .create(
                        tripExpense.getId(),
                        BudgetRecord.Source.TRIP_EXPENSE,
                        tripExpense.getSplittingDetail().getUserSpending().orElseThrow(IllegalAccessError::new))
                .getValue()
                .orElseThrow(IllegalAccessError::new);
    }
    // endregion helper method ---------------------------------------------------------------------
}
