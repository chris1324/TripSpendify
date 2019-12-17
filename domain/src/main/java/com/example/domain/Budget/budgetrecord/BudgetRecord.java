package com.example.domain.Budget.budgetrecord;

import com.example.domain.Common.entity.ID;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.record.Record;
import com.example.domain.Common.sharedvalueobject.date.Date;

public class BudgetRecord extends Record<BudgetRecord.Source> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<BudgetRecord, Err.Create> create(ID id,
                                                                Source source,
                                                                ID sourceTransId) {

        try {
            Guard.NotNull(id);
            Guard.NotNull(source);
            Guard.NotNull(sourceTransId);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new BudgetRecord(id, source, sourceTransId));
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
        BUDGET_TRANSACTION
    }

    protected BudgetRecord(ID id, Source source, ID sourceTransId) {
        super(id, source, sourceTransId);
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    // region Getter -------------------------------------------------------------------------------

    // endregion Getter ----------------------------------------------------------------------------
}
