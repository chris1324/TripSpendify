package com.example.domain.PersernalExpense.persExpBook;

import com.example.domain.PersernalExpense.persExpRecord.PersExpRecord;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.commandBaseClass.book.Book;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.result.Result;

import java.util.List;

public class PersExpBook extends Book {

    // region Factory method -----------------------------------------------------------------------
    public static Result<PersExpBook, Err.Create> create(ID tripBookID, List<PersExpRecord> persExpRecords) {

        try {
            Guard.NotNull(tripBookID);
            Guard.NotNull(persExpRecords);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new PersExpBook(
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

    private PersExpBook(ID tripBookID, EntityList<PersExpRecord> persExpRecords) {
        super(tripBookID);
        mPersExpRecords = persExpRecords;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------PersExpRecord-----------------------------------------

    void savePersExpRecord(PersExpRecord persExpRecord) {
        mPersExpRecords.put(persExpRecord);
    }

    @Deprecated
    void removePersExpRecord(ID sourceTransId) {
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
