package com.example.domain.Cash.cashRecord;

import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Shared.commandBaseClass.record.BookRecord;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

public class CashRecord extends BookRecord<CashRecord.SourceType, MonetaryAmount> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CashRecord, Err.Create> create(ID sourceTransId,
                                                        SourceType source,
                                                        MonetaryAmount amount) {
        try {
            Guard.NotNull(sourceTransId);
            Guard.NotNull(source);
            Guard.NotNull(amount);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CashRecord(sourceTransId, source, amount));
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
        CASH_TRANS_DEPOSIT,
        CASH_TRANS_WITHDRAWAL,
        CASH_TRANS_ADJUST_UP,
        CASH_TRANS_ADJUST_DOWN,
        COLL_TRANS_SETTLEMENT_MADE,
        COLL_TRANS_SETTLEMENT_ACCEPTED,
        COLL_TRANS_CONTRIBUTION_MADE,
        COLL_TRANS_CONTRIBUTION_ACCEPTED;

        @Override
        public Effect effectOnBalance() {
            switch (this) {
                case CASH_TRANS_DEPOSIT:
                case CASH_TRANS_ADJUST_UP:
                case COLL_TRANS_SETTLEMENT_ACCEPTED:
                case COLL_TRANS_CONTRIBUTION_ACCEPTED:
                    return Effect.INCREASE;
                case TRIP_EXPENSE:
                case CASH_TRANS_WITHDRAWAL:
                case CASH_TRANS_ADJUST_DOWN:
                case COLL_TRANS_SETTLEMENT_MADE:
                case COLL_TRANS_CONTRIBUTION_MADE:
                    return Effect.DECREASE;
                default:
                    throw new UnexpectedEnumValue();
            }
        }
    }

    protected CashRecord(ID sourceTransId, SourceType source, MonetaryAmount amount) {
        super(sourceTransId, source, amount);
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    // region Getter -------------------------------------------------------------------------------
    // endregion Getter ----------------------------------------------------------------------------
}
