package com.example.domain.Collectible.collectiblerecord;

import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.baseclass.record.Record;
import com.example.domain.Common.sharedvalueobject.numeric.Amount;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;

import java.util.List;
import java.util.Map;

public class CollectibleRecord extends Record<CollectibleRecord.Source, Map<ID, MonetaryAmount>> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CollectibleRecord, Err.Create> create(ID sourceTransId,
                                                               Source source,
                                                               Map<ID, MonetaryAmount> amounts) {

        try {
            Guard.NotNull(sourceTransId);
            Guard.NotNull(source);
            Guard.NotNull(amounts);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CollectibleRecord(sourceTransId, source, amounts));
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
        TRIP_EXPENSE_BORROWING,
        TRIP_EXPENSE_LENDING,
        COLLECTIBLE_SETTLEMENT_MADE,
        COLLECTIBLE_SETTLEMENT_ACCEPTED,
        COLLECTIBLE_CONTRIBUTION_MADE,
        COLLECTIBLE_CONTRIBUTION_ACCEPTED;

        Record.Effect whatIsTheEffect(Source source) {
            switch (source) {
                case TRIP_EXPENSE_LENDING:
                case COLLECTIBLE_SETTLEMENT_MADE:
                case COLLECTIBLE_CONTRIBUTION_MADE:
                    return Effect.INCREASE;
                case TRIP_EXPENSE_BORROWING:
                case COLLECTIBLE_SETTLEMENT_ACCEPTED:
                case COLLECTIBLE_CONTRIBUTION_ACCEPTED:
                    return Effect.DECREASE;
                default:
                    throw new UnhandledError();
            }
        }
    }

    protected CollectibleRecord(ID sourceTransId, Source source, Map<ID, MonetaryAmount> amounts) {
        super(sourceTransId, source, amounts);
    }


    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    // endregion Getter ----------------------------------------------------------------------------


}
