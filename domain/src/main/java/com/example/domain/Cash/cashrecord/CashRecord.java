package com.example.domain.Cash.cashrecord;

import com.example.domain.Common.entity.ID;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.record.Record;

public class CashRecord extends Record<CashRecord.Source> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CashRecord, Err.Create> create(ID id, Source source, ID sourceTransId) {
        try {
            Guard.NotNull(id);
            Guard.NotNull(source);
            Guard.NotNull(sourceTransId);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CashRecord(id, source, sourceTransId));
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
        CASH_TRANSACTION,
        TRIP_EXPENSE,
        COLLECTIBLE_TRANS
    }

    private CashRecord(ID id, Source source, ID sourceTransId) {
        super(id, source, sourceTransId);
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    // region Getter -------------------------------------------------------------------------------
    // endregion Getter ----------------------------------------------------------------------------
}
