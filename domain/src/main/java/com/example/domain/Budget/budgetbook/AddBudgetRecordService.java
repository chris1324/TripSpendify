package com.example.domain.Budget.budgetbook;

import com.example.domain.Budget.budgettransaction.BudgetTransaction;
import com.example.domain.Budget.budgetrecord.BudgetRecord;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Trip.tripbook.TripBook;
import com.example.domain.Trip.tripexpense.TripExpense;
import com.example.domain.Trip.tripexpense.splittingdetail.SplittingDetail;

import java.util.Optional;

public class AddBudgetRecordService {


    // region Error Class --------------------------------------------------------------------------\
    public static class Err {
        public enum TripExpense {
            INVALID_SOURCE_TYPE,
            SOURCE_TRANS_NOT_EXIST,
            DIFFERENT_TRIP,
            INVALID_DATE,
            USER_NOT_SPENDING
        }

        public enum BudgetTrans {
            DIFFERENT_TRIP,
            INVALID_DATE
        }
    }
    // endregion Error Class -----------------------------------------------------------------------


    // ---------------------------------------- TripExpense ----------------------------------------
    public Outcome<Err.TripExpense> fromTripExpense(TripBook tripBk,
                                                    BudgetBook budgetBk,
                                                    BudgetRecord record) {
        // Validation
        // -- Validate Source Type
        if (invalidSource(record, BudgetRecord.Source.TRIP_EXPENSE))
            return Outcome.err(Err.TripExpense.INVALID_SOURCE_TYPE);

        // -- Validate Source Trans exist
        Optional<TripExpense> sourceTrans = tripBk.getTripExpenses().get(record.getSourceTransId());
        if (!sourceTrans.isPresent()) return Outcome.err(Err.TripExpense.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Trip & Date
        if (isDifferentTrip(tripBk, budgetBk))
            return Outcome.err(Err.TripExpense.DIFFERENT_TRIP);
        if (notDuringTrip(tripBk, sourceTrans.get().getDate()))
            return Outcome.err(Err.TripExpense.INVALID_DATE);

        // -- Validate User is spending
        if (userNotSpending(sourceTrans.get())) return Outcome.err(Err.TripExpense.USER_NOT_SPENDING);

        // Success
        budgetBk.addBudgetRecord(record);
        return Outcome.ok();
    }


    // -------------------------------------- Budget Transaction -----------------------------------
    public Outcome<Err.BudgetTrans> fromBudgetTrans(TripBook tripBk,
                                                    BudgetBook budgetBk,
                                                    BudgetTransaction transaction){
        // Validation
        // -- Validate Trip & Date
        if (isDifferentTrip(tripBk, budgetBk))
            return Outcome.err(Err.BudgetTrans.DIFFERENT_TRIP);
        if (notDuringTrip(tripBk, transaction.getDate()))
            return Outcome.err(Err.BudgetTrans.INVALID_DATE);

        // Success
        budgetBk.addBudgetTransaction(transaction);
        return Outcome.ok();
    }


    // region helper method ------------------------------------------------------------------------
    private boolean invalidSource(BudgetRecord record, BudgetRecord.Source source) {
        return record.getSource() != source;
    }

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
    // endregion helper method ---------------------------------------------------------------------

}
