package com.example.domain.Cash.cashTransaction;

import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedValueObject.date.Date;
import com.example.domain.Common.sharedValueObject.numeric.MonetaryAmount;
import com.example.domain.Common.sharedValueObject.note.Note;
import com.example.domain.Common.baseclass.transaction.Transaction;

public class CashTransaction extends Transaction {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CashTransaction, Err.Create> create(ID id,
                                                             Date date,
                                                             Note note,
                                                             MonetaryAmount amount,
                                                             Type transactionType) {
        try {
            Guard.NotNull(id);
            Guard.NotNull(date);
            Guard.NotNull(note);
            Guard.NotNull(amount);
            Guard.NotNull(transactionType);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CashTransaction(id, date, note, amount, transactionType));
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
        WITHDRAWAL,
        DEPOSIT,
        ADJUSTMENT_UP,
        ADJUSTMENT_DOWN
    }

    private final Type mTransactionType;

    private CashTransaction(ID id, Date date, Note note, MonetaryAmount amount, Type transactionType) {
        super(id, date, note, amount);
        mTransactionType = transactionType;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------

    public Type getTransactionType() {
        return mTransactionType;
    }
    // endregion Getter ----------------------------------------------------------------------------
}
