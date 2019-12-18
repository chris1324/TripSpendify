package com.example.domain.Cash.cashrecord;

import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.baseclass.record.Record;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;

public class CashRecord extends Record<CashRecord.Source, MonetaryAmount> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CashRecord, Err.Create> create(ID sourceTransId, Source source, MonetaryAmount amount) {
        try {
            Guard.NotNull(sourceTransId);
            Guard.NotNull(source);
            Guard.NotNull(amount);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CashRecord(sourceTransId, source,amount));
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

    protected CashRecord(ID sourceTransId, Source source, MonetaryAmount amount) {
        super(sourceTransId, source, amount);
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    // region Getter -------------------------------------------------------------------------------
    // endregion Getter ----------------------------------------------------------------------------
}
