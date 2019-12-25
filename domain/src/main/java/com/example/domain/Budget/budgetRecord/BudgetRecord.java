package com.example.domain.Budget.budgetRecord;

import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.commandBaseClass.record.BookRecord;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

public class BudgetRecord extends BookRecord<BudgetRecord.SourceType, MonetaryAmount> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<BudgetRecord, Err.Create> create(ID sourceTransId,
                                                          SourceType source,
                                                          MonetaryAmount amount) {

        try {
            Guard.NotNull(sourceTransId);
            Guard.NotNull(source);
            Guard.NotNull(amount);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new BudgetRecord(sourceTransId, source, amount));
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
    public enum SourceType implements BookRecord.Source {
        TRIP_EXPENSE,
        BUDGET_TRANSACTION;

        @Override
        public Effect effectOnBalance() {
            switch (this) {
                case BUDGET_TRANSACTION:
                    return Effect.INCREASE;
                case TRIP_EXPENSE:
                    return Effect.DECREASE;
                default:
                    throw new UnexpectedEnumValue();
            }
        }
    }

    protected BudgetRecord(ID sourceTransId, SourceType source, MonetaryAmount amount) {
        super(sourceTransId, source, amount);
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    // region Getter -------------------------------------------------------------------------------

    // endregion Getter ----------------------------------------------------------------------------
}
