package com.example.domain.PersExpense.persexprecord;

import com.example.domain.Common.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.baseclass.record.Record;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripexpense.TripExpense;

public class PersExpRecord extends Record<PersExpRecord.Source, MonetaryAmount> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<PersExpRecord, Err.Create> create(ID sourceTransId, Source source, MonetaryAmount amount) {
        try {
            Guard.NotNull(sourceTransId);
            Guard.NotNull(source);
            Guard.NotNull(amount);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new PersExpRecord(sourceTransId, source, amount));
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
        TRIP_EXPENSE;

        Record.Effect whatIsTheEffect() {
            if (this == Source.TRIP_EXPENSE) return Effect.INCREASE;
            throw new UnexpectedEnumValue();
        }
    }

    protected PersExpRecord(ID sourceTransId, Source source, MonetaryAmount amount) {
        super(sourceTransId, source, amount);
    }

    // endregion Variables and Constructor ---------------------------------------------------------


    // ---------------------------------------------------------------------------------------------


    // region Getter -------------------------------------------------------------------------------
    // endregion Getter ----------------------------------------------------------------------------
}
