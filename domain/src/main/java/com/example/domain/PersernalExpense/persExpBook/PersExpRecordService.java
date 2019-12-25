package com.example.domain.PersernalExpense.persExpBook;

import com.example.domain.Shared.commandBaseClass.book.Book;
import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.PersernalExpense.persExpRecord.PersExpRecord;
import com.example.domain.Trip.tripBook.TripBook;
import com.example.domain.Trip.tripExpense.TripExpense;
import com.example.domain.Trip.tripExpense.splittingdetail.SplittingDetail;
import com.example.domain.Shared.valueObject.id.ID;

import java.util.Optional;

public class PersExpRecordService {
    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum SaveRecord {
            SOURCE_TRANS_NOT_EXIST,
            DIFFERENT_TRIP,
            USER_NOT_SPENDING
        }

        public enum RemoveRecord {
            DIFFERENT_TRIP,
            SOURCE_STILL_EXIST,
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    public Outcome<Err.SaveRecord> saveRecord(TripBook tripBk,
                                              PersExpBook persExpBk,
                                              ID tripExpenseID) {
        // Validation
        // -- Validate RecordSource Trans exist
        Optional<TripExpense> sourceTrans = tripBk.getTripExpenses().get(tripExpenseID);
        if (!sourceTrans.isPresent()) return Outcome.err(Err.SaveRecord.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Trip
        if (isDifferentTrip(tripBk, persExpBk))
            return Outcome.err(Err.SaveRecord.DIFFERENT_TRIP);

        // -- Validate User is spending
        if (userNotSpending(sourceTrans.get())) return Outcome.err(Err.SaveRecord.USER_NOT_SPENDING);

        // Success
        persExpBk.savePersExpRecord(createRecordFromTripExpense(sourceTrans.get()));
        return Outcome.ok();
    }

    @Deprecated
   public Outcome<Err.RemoveRecord> removeRecord(TripBook tripBk,
                                                 PersExpBook persExpBk,
                                                 ID tripExpenseId) {
       // Validation
       // -- Validate Trip
       if (isDifferentTrip(tripBk, persExpBk)) return Outcome.err(Err.RemoveRecord.DIFFERENT_TRIP);

       // -- Validate RecordSource delete
       boolean sourceStillExist = !tripBk.getTripExpenses().contain(tripExpenseId);
       if (sourceStillExist) return Outcome.err(Err.RemoveRecord.SOURCE_STILL_EXIST);

       // Success
       persExpBk.removePersExpRecord(tripExpenseId);
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

    private PersExpRecord createRecordFromTripExpense(TripExpense tripExpense) {
        return PersExpRecord
                .create(
                        tripExpense.getId(),
                        PersExpRecord.SourceType.TRIP_EXPENSE,
                        tripExpense.getSplittingDetail().getUserSpending().orElseThrow(ImpossibleState::new)
                )
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }
    // endregion helper method ---------------------------------------------------------------------
}
