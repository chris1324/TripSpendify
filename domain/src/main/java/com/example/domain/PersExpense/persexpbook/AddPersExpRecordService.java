package com.example.domain.PersExpense.persexpbook;


import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.PersExpense.persexprecord.PersExpRecord;
import com.example.domain.Trip.tripbook.TripBook;
import com.example.domain.Trip.tripexpense.TripExpense;
import com.example.domain.Trip.tripexpense.splittingdetail.SplittingDetail;

import java.util.Optional;

public class AddPersExpRecordService {

    // region Error Class --------------------------------------------------------------------------
    public static enum Err {
        SOURCE_TRANS_NOT_EXIST,
        DIFFERENT_TRIP,
        USER_NOT_SPENDING
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ------------------------------------TripExpense Transaction----------------------------------
    public Outcome<Err> fromTripExpense(TripBook tripBk,
                                        PersExpBook persExpBk,
                                        PersExpRecord record) {
        // Validation
        // -- Validate Source Trans exist
        Optional<TripExpense> sourceTrans = tripBk.getTripExpenses().get(record.getSourceTransId());
        if (!sourceTrans.isPresent()) return Outcome.err(Err.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Trip
        if (isDifferentTrip(tripBk, persExpBk))
            return Outcome.err(Err.DIFFERENT_TRIP);

        // -- Validate User is spending
        if (userNotSpending(sourceTrans.get())) return Outcome.err(Err.USER_NOT_SPENDING);

        // Success
        persExpBk.addPersExpRecord(record);
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
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
