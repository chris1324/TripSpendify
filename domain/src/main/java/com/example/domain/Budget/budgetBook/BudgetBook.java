package com.example.domain.Budget.budgetBook;

import com.example.domain.Budget.budgetTransaction.BudgetTransaction;
import com.example.domain.Budget.budgetRecord.BudgetRecord;
import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.entityList.EntityList;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedValueObject.date.Date;

import java.util.List;

public class BudgetBook extends Book {

    // region Factory method -----------------------------------------------------------------------
    public static Result<BudgetBook, Err.Create> create(ID id,
                                                        ID tripBookID,
                                                        List<BudgetRecord> budgetRecords,
                                                        List<BudgetTransaction> budgetTransactions) {

        try {
            Guard.NotNull(id);
            Guard.NotNull(tripBookID);
            Guard.NotNull(budgetRecords);
            Guard.NotNull(budgetTransactions);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new BudgetBook(
                id,
                tripBookID,
                EntityList.newList(budgetRecords),
                EntityList.newList(budgetTransactions)
        ));
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
    private final EntityList<BudgetRecord> mBudgetRecords;
    private final EntityList<BudgetTransaction> mBudgetTransactions;

    protected BudgetBook(ID id,
                         ID tripBookID,
                         EntityList<BudgetRecord> budgetRecords,
                         EntityList<BudgetTransaction> budgetTransactions) {
        super(id, tripBookID);
        mBudgetRecords = budgetRecords;
        mBudgetTransactions = budgetTransactions;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // -------------------------------------Budget Record-------------------------------------------
    void addBudgetRecord(BudgetRecord record) {
        mBudgetRecords.put(record);
    }

    void removeBudgetRecord(ID sourceTransId) {
        mBudgetRecords.remove(record -> record.getSourceTransId().equals(sourceTransId));
    }

    // -----------------------------------Budget Transaction----------------------------------------
    void addBudgetTransaction(BudgetTransaction transaction, BudgetRecord budgetRecord) {
        this.removeBudgetTransWithThisDay(transaction.getDate());
        mBudgetTransactions.put(transaction);
        this.addBudgetRecord(budgetRecord);
    }

    void removeBudgetTransaction(ID transactionId) {
        mBudgetTransactions.remove(transactionId);
        this.removeBudgetRecord(transactionId);
    }

    // region helper method ------------------------------------------------------------------------
    private void removeBudgetTransWithThisDay(Date date) {
        mBudgetTransactions
                .searchReturnFirst(budgetTransaction -> budgetTransaction.getDate().isSameDate(date))
                .map(BudgetTransaction::getId)
                .ifPresent(this::removeBudgetTransaction);
    }

    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    public EntityList<BudgetRecord> getBudgetRecords() {
        return mBudgetRecords.unmodifiable();
    }

    public EntityList<BudgetTransaction> getBudgetTransactions() {
        return mBudgetTransactions.unmodifiable();
    }

    // endregion Getter ----------------------------------------------------------------------------
}
