package com.example.domain.Budget.budgetallocation;

import com.example.domain.Common.entity.ID;
import com.example.domain.Common.entity.transaction.Transaction;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.amount.Amount;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.sharedvalueobject.note.Note;

public class BudgetTransaction extends Transaction<Amount> {

    // region Factory method -----------------------------------------------------------------------

    public static Result<BudgetTransaction, Err.Create> create(ID id,
                                                               Date date,
                                                               Note note,
                                                               Amount amount,
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
                                Amount amount,
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
