package com.example.domain.Collectible.collectibletransaction;

import com.example.domain.Common.entity.ID;
import com.example.domain.Common.entity.transaction.Transaction;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.sharedvalueobject.amount.MonetaryAmount;
import com.example.domain.Common.sharedvalueobject.note.Note;

public class CollectibleTransaction extends Transaction<MonetaryAmount> {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CollectibleTransaction, Err.Create> create(ID id,
                                                                    Date date,
                                                                    Note note,
                                                                    MonetaryAmount amount,
                                                                    Type transactionType,
                                                                    ID involvedMemberId,
                                                                    Payer payer) {
        try {
            Guard.NotNull(id);
            Guard.NotNull(date);
            Guard.NotNull(note);
            Guard.NotNull(amount);
            Guard.NotNull(transactionType);
            Guard.NotNull(involvedMemberId);
            Guard.NotNull(payer);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CollectibleTransaction(
                id,
                date,
                note,
                amount,
                transactionType,
                involvedMemberId, payer));

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
    public enum Type {
        CONTRIBUTION,
        SETTLEMENT
    }

    public enum Payer {
        USER,
        MEMBER
    }

    private final Type mTransactionType;
    private final ID mInvolvedMemberId;
    private final Payer mPayer;

    public CollectibleTransaction(ID id,
                                  Date date,
                                  Note note,
                                  MonetaryAmount amount,
                                  Type transactionType,
                                  ID involvedMemberId,
                                  Payer payer) {
        super(id, date, note, amount);
        mTransactionType = transactionType;
        mInvolvedMemberId = involvedMemberId;
        mPayer = payer;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------

    public Type getTransactionType() {
        return mTransactionType;
    }

    public ID getInvolvedMemberId() {
        return mInvolvedMemberId;
    }

    public boolean isUserPayer() {
        return mPayer == Payer.USER;
    }

    // endregion Getter ----------------------------------------------------------------------------


}
