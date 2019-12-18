package com.example.domain.Budget.budgettransaction;

import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.baseclass.transaction.Transaction;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.sharedvalueobject.note.Note;

public class BudgetTransaction extends Transaction {

    // region Factory method -----------------------------------------------------------------------

    public static Result<BudgetTransaction, Err.Create> create(ID id,
                                                               Date date,
                                                               Note note,
                                                               MonetaryAmount amount,
                                                               ID categoryId) {
        try {
            Guard.NotNull(id);
            Guard.NotNull(date);
            Guard.NotNull(note);
            Guard.NotNull(amount);
            Guard.NotNull(categoryId);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new BudgetTransaction(id,
                date,
                note,
                amount,
                categoryId));
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
    private final ID mCategoryId;

    protected BudgetTransaction(ID id,
                                Date date,
                                Note note,
                                MonetaryAmount amount,
                                ID categoryId) {
        super(id, date, note, amount);
        mCategoryId = categoryId;

    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    public ID getCategoryId() {
        return mCategoryId;
    }


    // endregion Getter ----------------------------------------------------------------------------
}
