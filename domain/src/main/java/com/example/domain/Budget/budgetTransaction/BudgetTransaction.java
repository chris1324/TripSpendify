package com.example.domain.Budget.budgetTransaction;

import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.commandBaseClass.transaction.Transaction;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.note.Note;

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
