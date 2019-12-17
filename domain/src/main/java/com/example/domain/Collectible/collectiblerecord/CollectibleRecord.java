package com.example.domain.Collectible.collectiblerecord;

import com.example.domain.Common.entity.ID;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.record.Record;

public class CollectibleRecord extends Record<CollectibleRecord.Source> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CollectibleRecord, Err.Create> creare(ID id, Source source, ID sourceTransId) {

        try {
            Guard.NotNull(id);
            Guard.NotNull(source);
            Guard.NotNull(sourceTransId);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CollectibleRecord(id, source, sourceTransId));
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
    public enum Source {
        TRIP_EXPENSE,
        COLLECTIBLE_TRANS
    }

    private CollectibleRecord(ID id, Source source, ID sourceTransId) {
        super(id, source, sourceTransId);
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    // endregion Getter ----------------------------------------------------------------------------


}
