package com.example.domain.PersExpense.persexpbook;

import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.entity.book.Book;
import com.example.domain.Common.entity.transaction.Transaction;
import com.example.domain.Common.entitylist.EntityList;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.function.Function;
import com.example.domain.PersExpense.persexprecord.PersExpRecord;
import com.example.domain.Trip.tripexpense.TripExpense;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersExpBook extends Book {

    // region Factory method -----------------------------------------------------------------------
    public static Result<PersExpBook, Err.Create> create(ID id, ID tripBookID, List<PersExpRecord> persExpRecords) {

        try {
            Guard.NotNull(id);
            Guard.NotNull(tripBookID);
            Guard.NotNull(persExpRecords);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new PersExpBook(
                id,
                tripBookID,
                EntityList.newList(persExpRecords)
        ));
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum Create {
            NULL_ARGUMENT
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final EntityList<PersExpRecord> mPersExpRecords;

    private PersExpBook(ID id, ID tripBookID, EntityList<PersExpRecord> persExpRecords) {
        super(id, tripBookID);
        mPersExpRecords = persExpRecords;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------PersExpRecord-----------------------------------------

    void putPersExpRecord(PersExpRecord persExpRecord) {
        mPersExpRecords.put(persExpRecord);
    }

    void removePersExpRecord(ID sourceTransId){
        mPersExpRecords.remove(persExpRecord -> persExpRecord.getSourceTransId() == sourceTransId);
    }

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    public EntityList<PersExpRecord> getPersExpRecords() {
        return mPersExpRecords.unmodifiable();
    }
    // endregion Getter ----------------------------------------------------------------------------

}
