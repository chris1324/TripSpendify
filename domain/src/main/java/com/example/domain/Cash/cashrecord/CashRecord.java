package com.example.domain.Cash.cashrecord;

import com.example.domain.Common.entity.ID;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.guard.NullArgumentException;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.record.Record;

public class CashRecord extends Record<CashRecord.Source> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CashRecord, Err.Create> create(ID id, Source source, ID sourceDocId) {
        try {
            Guard.NotNull(id);
            Guard.NotNull(source);
            Guard.NotNull(sourceDocId);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CashRecord(id, source, sourceDocId));
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
        CASH_DEPOSIT,
        CASH_WITHDRAWAL,
        CASH_ADJUSTMENT,
        TRIP_EXPENSE,
        SETTLEMENT
    }

    private final ID sourceDocId;

    public CashRecord(ID id, Source source, ID sourceDocId) {
        super(id, source);
        this.sourceDocId = sourceDocId;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    // region Getter -------------------------------------------------------------------------------
    public ID getSourceDocId() {
        return sourceDocId;
    }
    // endregion Getter ----------------------------------------------------------------------------
}
