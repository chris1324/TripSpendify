package com.example.domain.Collectible.collectibleRecord;

import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.commandBaseClass.record.BookRecord;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

import java.util.Map;

public class CollectibleRecord extends BookRecord<CollectibleRecord.SourceType, Map<ID, MonetaryAmount>> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CollectibleRecord, Err.Create> create(ID sourceTransId,
                                                               SourceType source,
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
    public enum SourceType implements BookRecord.Source {
        TRIP_EXPENSE_BORROWING,
        TRIP_EXPENSE_LENDING,
        COLL_SETTLEMENT_MADE,
        COLL_SETTLEMENT_ACCEPTED,
        COLL_CONTRIBUTION_MADE,
        COLL_CONTRIBUTION_ACCEPTED;

        @Override
        public Effect effectOnBalance() {
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

    protected CollectibleRecord(ID sourceTransId, SourceType source, Map<ID, MonetaryAmount> amounts) {
        super(sourceTransId, source, amounts);
    }


    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    // endregion Getter ----------------------------------------------------------------------------


}
