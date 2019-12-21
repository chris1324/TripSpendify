package com.example.domain.Collectible.collectibleRecord;

import com.example.domain.Common.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.baseclass.record.Record;
import com.example.domain.Common.sharedValueObject.numeric.MonetaryAmount;

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
        COLL_SETTLEMENT_MADE,
        COLL_SETTLEMENT_ACCEPTED,
        COLL_CONTRIBUTION_MADE,
        COLL_CONTRIBUTION_ACCEPTED;

        Record.Effect whatIsTheEffect() {
            switch (this) {
                case TRIP_EXPENSE_LENDING:
                case COLL_SETTLEMENT_MADE:
                case COLL_CONTRIBUTION_MADE:
                    return Effect.INCREASE;
                case TRIP_EXPENSE_BORROWING:
                case COLL_SETTLEMENT_ACCEPTED:
                case COLL_CONTRIBUTION_ACCEPTED:
                    return Effect.DECREASE;
                default:
                    throw new UnexpectedEnumValue();
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
